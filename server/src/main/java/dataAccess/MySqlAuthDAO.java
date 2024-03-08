package dataAccess;

import model.AuthData;
import model.UserData;

public class MySqlAuthDAO implements AuthDAO{
    @Override
    public AuthData createAuth(UserData user) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData auth) {

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
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
