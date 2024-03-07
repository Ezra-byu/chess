package dataAccess;

import exception.ResponseException;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData user); //I need this to return AuthData
    UserData createUser(UserData user);
    void clearUser();
    boolean passwordMatch(String password, String password1);
    //listUser() needed?
}
