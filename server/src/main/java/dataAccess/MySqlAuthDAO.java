package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AuthDAO{
    public MySqlAuthDAO() {
        try {
            configureDatabaseForAuth(); //creates db & tables if not created
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

            executeUpdateAuth(statement, newauth.authToken(), newauth.username());
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
            executeUpdateAuth(statement, auth.authToken());
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
    }

    @Override
    public void clearAuth() {
        try {
            var statement = "TRUNCATE auth";
            executeUpdateAuth(statement);
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

    private int executeUpdateAuth(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var psAuth = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) psAuth.setString(i + 1, p);
                    else if (param instanceof Integer p) psAuth.setInt(i + 1, p);
                    else if (param == null) psAuth.setNull(i + 1, NULL);
                }
                psAuth.executeUpdate();

                var rs = psAuth.getGeneratedKeys();
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


    private void configureDatabaseForAuth() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatementAuth = conn.prepareStatement(statement)) {
                    preparedStatementAuth.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
