package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    //createAuth: Create a new authorization.
    //getAuth: Retrieve an authorization given an authToken.
    //deleteAuth: Delete an authorization so that it is no longer valid.
    AuthData createAuth(UserData user);
    AuthData getAuth(String authToken);
    void deleteAuth(AuthData auth);
    void clearAuth();
    Boolean checkAuth(String authToken);
}
