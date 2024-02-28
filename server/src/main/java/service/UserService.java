package service;

import dataAccess.*;
import model.*;

import java.util.Collection;
import java.util.Objects;

//The Service classes implement the actual functionality of the server.
//More specifically, the Service classes implement the logic associated with the web endpoints.
public class UserService {
    static UserDAO my_userDAO = new MemoryUserDAO(); //change upon completion of SQL database
    static AuthDAO my_authDAO = new MemoryAuthDAO();
    static GameDAO my_gameDAO = new MemoryGameDAO();

    public static BaseResponse clear(){
        my_userDAO.clearUser();
        my_authDAO.clearAuth();
        my_gameDAO.clearGame();
        return new ClearResponse(200);
    }

    public static BaseResponse register(UserData user) { //should return AuthData?
        if ((user == null) || (user.username() == null) || (user.password() == null) || (user.email() == null)) {
            return new ErrorResponse(400, "Error: bad request");
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

    public static BaseResponse login(UserData user) {
        if ((user.username() == null) || (user.password() == null)) {
            return new ErrorResponse(500, "Error: bad request");
        }
        //getuser
        //if no user or wrong password, return 401 unauthorized
        //createAuth
        //return the AuthData from ^
        UserData returneduser = my_userDAO.getUser(user);
        if ((returneduser != null) && (Objects.equals(returneduser.password(), user.password()))) {
            AuthData createdauth = my_authDAO.createAuth(user);
            return new LoginResponse(200, user.username(), createdauth.authToken());
        }
        else {
            return new ErrorResponse(401, "Error: unauthorized");
        }
    }

    public static BaseResponse logout(String authToken) {
        //if checkAuth auth
        //deleteAuth with that auth token
        //return logoutResponse
        if(my_authDAO.checkAuth(authToken)){
            my_authDAO.deleteAuth(my_authDAO.getAuth(authToken));
            return new LogoutResponse(200);
        }
        else{
            return new ErrorResponse(401, "Error: unauthorized");
        }

    }

    public static BaseResponse listgames(String authToken) {
        //checkAuth
        //listGames()
        //return ListGame response()
        if(my_authDAO.checkAuth(authToken)){
            Collection<GameData> my_games = my_gameDAO.listGames();
            return new ListGameResponse(200, my_games);
        }
        else{
            return new ErrorResponse(401, "Error: unauthorized");
        }
    }
}
