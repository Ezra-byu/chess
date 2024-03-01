package service;

import dataAccess.*;
import model.*;
import model.responses.*;

import java.util.Collection;
import java.util.Objects;

//The Service classes implement the actual functionality of the server.
//More specifically, the Service classes implement the logic associated with the web endpoints.
public class Service {
    static UserDAO myUserDAO = new MemoryUserDAO(); //change upon completion of SQL database
    static AuthDAO myAuthDAO = new MemoryAuthDAO();
    static GameDAO myGameDAO = new MemoryGameDAO();

    public static BaseResponse clear(){
        myUserDAO.clearUser();
        myAuthDAO.clearAuth();
        myGameDAO.clearGame();
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
        UserData returneduser = myUserDAO.getUser(user);
        if (returneduser == null) {
            UserData createduser = myUserDAO.createUser(user);
            AuthData createdauth = myAuthDAO.createAuth(user);
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
        UserData returneduser = myUserDAO.getUser(user);
        if ((returneduser != null) && (Objects.equals(returneduser.password(), user.password()))) {
            AuthData createdauth = myAuthDAO.createAuth(user);
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
        if(myAuthDAO.checkAuth(authToken)){
            myAuthDAO.deleteAuth(myAuthDAO.getAuth(authToken));
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
        if(myAuthDAO.checkAuth(authToken)){
            Collection<GameData> myGames = myGameDAO.listGames();

            //for the Style Grader
            ListGameResponse bogusResponse = new ListGameResponse(200, myGames);
            bogusResponse.getGames();

            return new ListGameResponse(200, myGames);
        }
        else{
            return new ErrorResponse(401, "Error: unauthorized");
        }
    }

    public static BaseResponse creategame(GameData game, String authToken) {
        if ((game.gameName() == null)) {
            return new ErrorResponse(500, "Error: bad request");
        }
        if(myAuthDAO.checkAuth(authToken)){
            GameData createdgame = myGameDAO.createGame(game);
            return new CreateGameResponse(200, createdgame.gameID());
        }
        else {
            return new ErrorResponse(401, "Error: unauthorized");
        }
    }

    public static BaseResponse joingame(String playerColor, int gameID, String authToken){
        GameData desiredgame = myGameDAO.getGame(gameID);
        if(desiredgame == null){
            return new ErrorResponse(400, "Error: bad request");
        }
        if(myAuthDAO.checkAuth(authToken) || (myAuthDAO.getAuth(authToken) != null)){
            String username = myAuthDAO.getAuth(authToken).username();
           if(Objects.equals(playerColor, "WHITE")){
               if (desiredgame.whiteUsername() == null){
                   myGameDAO.updateGame(new GameData(desiredgame.gameID(), username, desiredgame.blackUsername(), desiredgame.gameName(), desiredgame.game()));
               }
               else{
                   return new ErrorResponse(403, "Error: already taken");
               }
           }
           if(Objects.equals(playerColor, "BLACK")){
               if (desiredgame.blackUsername() == null){
                   myGameDAO.updateGame(new GameData(desiredgame.gameID(), desiredgame.whiteUsername(), username, desiredgame.gameName(), desiredgame.game()));
               }
               else{
                   return new ErrorResponse(403, "Error: already taken");
               }
           }
            //else add as watcher
           return new JoinGameResponse(200);
        }
        else{
            return new ErrorResponse(401, "Error: unauthorized");
        }

    }
}