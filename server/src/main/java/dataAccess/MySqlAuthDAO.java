package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AuthDAO{
    public MySqlAuthDAO() {
        try {
            configureDatabase(); //creates db & tables if not created
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
    }

    @Override
    public AuthData createAuth(UserData user) {
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";

            AuthData newauth = new AuthData(UUID.randomUUID().toString(), user.username());

            executeUpdate(statement, newauth.authToken(), newauth.username());
            return newauth;
            //should return new User with Hashed Password. This is used in the Login Service
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthData(rs);//password will be hashed
                    }
                }
            }
        } catch (Exception e) {
            //throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
            System.out.println("Something went wrong." + e);
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData auth) {
        try {
            var statement = "DELETE FROM auth WHERE authToken=?";
            executeUpdate(statement, auth.authToken());
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
    }

    @Override
    public void clearAuth() {
        try {
            var statement = "TRUNCATE auth";
            executeUpdate(statement);
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
    }

    @Override
    public Boolean checkAuth(String authToken) {
        if (getAuth(authToken) == null){
            return false;
        }
        else{
            return true;
        }
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

    private AuthData readAuthData(ResultSet rs) throws SQLException {
        //authToken, username
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
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
