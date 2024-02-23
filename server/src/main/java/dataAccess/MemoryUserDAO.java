package dataAccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(UserData user) {
        return users.get(user.username()); // see if someone with that username is in the database, else null
    }

    @Override
    public UserData createUser(UserData user) {
        users.put(user.username(), user);
        return user;
    }

    @Override
    public UserData updateUser(UserData user) {
        return null;
    }

    @Override
    public void deleteUser(UserData user) {

    }

    @Override
    public void clearUser() {

    }

}
