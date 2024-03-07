package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlUserDAO implements UserDAO{
    public MySqlUserDAO() {
        try {
            configureDatabase(); //creates db & tables if not created
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
    }
    @Override
    public UserData getUser(UserData user) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUserData(rs);//password will be hashed  hashed
                    }
                }
            }
        } catch (Exception e) {
            //throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
            return null;
        }
        return null;
    }

    @Override
    public UserData createUser(UserData user) {
        try {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(user.password());

            executeUpdate(statement, user.username(), hashedPassword, user.email());
            return new UserData(user.username(), hashedPassword, user.email());//should return new User with Hashed Password. This is used in the Login Service
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
        return null;
    }

    @Override
    public void clearUser() {

    }

    @Override
    public boolean passwordMatch(String password, String password1) {
        //var hashedPassword = readHashedPasswordFromDatabase(username);
        var hashedPassword = password1;
        var providedClearTextPassword = password;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(providedClearTextPassword, hashedPassword);
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        //username, password, email
        var username = rs.getString("username");
        var hashedPassword = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, hashedPassword, email);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
