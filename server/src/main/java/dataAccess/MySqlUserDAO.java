package dataAccess;

import model.UserData;

public class MySqlUserDAO implements UserDAO{
    @Override
    public UserData getUser(UserData user) {
        return null;
    }

    @Override
    public UserData createUser(UserData user) {
        return null;
    }

    @Override
    public void clearUser() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
