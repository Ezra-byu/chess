package dataAccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData user); // somehow I need this to return AuthData
    UserData createUser(UserData user);
    UserData updateUser(UserData user);
    void deleteUser(UserData user);
    void clearUser();
    //listUser() needed?
}
