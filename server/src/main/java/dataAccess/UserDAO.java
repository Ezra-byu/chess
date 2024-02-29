package dataAccess;

import exception.ResponseException;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData user); // somehow I need this to return AuthData
    UserData createUser(UserData user);
    void clearUser();
    //listUser() needed?
}
