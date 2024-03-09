package dataAccess.memoryDAO;

import dataAccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
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

    //"public void deleteUser(UserData user) {users.remove(user.username());}"

    @Override
    public void clearUser() {
        users.clear();
    }

    @Override
    public boolean passwordMatch(String password, String password1) { //untested
        return Objects.equals(password, password1);
    }

}
