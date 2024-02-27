package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import dataAccess.MemoryAuthDAO;
import model.*;

//The Service classes implement the actual functionality of the server.
//More specifically, the Service classes implement the logic associated with the web endpoints.
public class UserService {
    static UserDAO my_userDAO = new MemoryUserDAO(); //change upon completion of SQL database
    static AuthDAO my_authDAO = new MemoryAuthDAO();

    public static BaseResponse register(UserData user) { //should return AuthData?
        if ((user.username() == null) || (user.password() == null) || (user.email() == null)) {
            return new ErrorResponse(403, "Error: bad request");
        }
        //run getUser
        //if getUser returns null run createUser. else return fail [403]
        //run createAuth
        //return the AuthData from ^
        UserData returneduser = my_userDAO.getUser(user);
        if (returneduser == null) {
            UserData createduser = my_userDAO.createUser(user);
            AuthData createdauth = my_authDAO.createAuth(user);
            return new RegisterResponse(200, createduser.username(), createdauth.authToken());
        } else {//handle exception and return error code
            return new ErrorResponse(403, "Error: already taken");
        }
    }
}
