package serviceTests;

import exception.ResponseException;
import model.*;
import model.responses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    //test for username already taken
    //test_user_service = new UserService();

    @BeforeEach
    void clear() throws ResponseException {
        Service.clear();
    }

    @Test
    void clearSuccess() throws ResponseException {
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        response = Service.clear();
        assertInstanceOf(ClearResponse.class, response);
    }


    @Test
    void registerSuccess() throws ResponseException {
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        assertInstanceOf(RegisterResponse.class, response);
        RegisterResponse regResponse = (RegisterResponse) response;
    }
    @Test
    void registerFailure() throws ResponseException{
        UserData user1 = new UserData("sameusername","password1" , "user1");
        UserData user2 = new UserData("sameusername","password2" , "user2");
        BaseResponse response1 = Service.register(user1);
        BaseResponse response2 = Service.register(user2);
        assertInstanceOf(RegisterResponse.class, response1);
        assertInstanceOf(ErrorResponse.class, response2);
    }


    @Test
    void loginSuccess()  throws ResponseException{
        UserData user1 = new UserData("sameusername","password1" , "user1");
        BaseResponse response = Service.register(user1);
        response = Service.login(user1);
        assertInstanceOf(LoginResponse.class, response);
    }
    @Test
    void loginFailure()  throws ResponseException{
        UserData user1 = new UserData("sameusername",null , "user1");
        BaseResponse response = Service.register(user1);
        response = Service.login(user1);
        assertInstanceOf(ErrorResponse.class, response);
    }


    @Test
    void logoutSuccess()  throws ResponseException{
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        assertInstanceOf(RegisterResponse.class, response);
        RegisterResponse regResponse = (RegisterResponse) response;
        String authToken = regResponse.getAuthToken();

        response = Service.logout(authToken);
        assertInstanceOf(LogoutResponse.class, response);
    }
    @Test
    void logoutFailure() throws ResponseException{
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        assertInstanceOf(RegisterResponse.class, response);
        RegisterResponse regResponse = (RegisterResponse) response;
        String authToken = regResponse.getAuthToken();

        response = Service.logout(authToken);
        assertInstanceOf(LogoutResponse.class, response);

        response = Service.logout(authToken);
        assertInstanceOf(ErrorResponse.class, response);
    }


    @Test
    void listGamesSuccess() throws ResponseException{
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        RegisterResponse regResponse = (RegisterResponse) response;
        String authToken = regResponse.getAuthToken();

        response = Service.creategame(new GameData(0,null, null, "abe's game1", null), authToken);
        response = Service.creategame(new GameData(1,null, null, "abe's game2", null), authToken);
        response = Service.listgames(authToken);
        assertInstanceOf(ListGameResponse.class, response);
        ListGameResponse listGameResponse = (ListGameResponse) response;
        assertEquals(2, listGameResponse.getGames().size());
    }
    @Test
    void listGamesFailure() throws ResponseException{
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        RegisterResponse regResponse = (RegisterResponse) response;
        String authToken = regResponse.getAuthToken();

        response = Service.creategame(new GameData(0,null, null, "abe's game", null), authToken);
        response = Service.listgames(null);
        assertInstanceOf(ErrorResponse.class, response);

        response = Service.listgames(authToken);
        assertInstanceOf(ListGameResponse.class, response);
        ListGameResponse listGameResponse = (ListGameResponse) response;
        assertEquals(1, listGameResponse.getGames().size());
    }


    @Test
    void createGameSuccess() throws ResponseException{
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        assertInstanceOf(RegisterResponse.class, response);
        RegisterResponse regResponse = (RegisterResponse) response;

        String authToken = regResponse.getAuthToken();
        response = Service.creategame(new GameData(0,null, null, "abe's game", null), authToken);
        assertInstanceOf(CreateGameResponse.class, response);
        CreateGameResponse createGameResponse = (CreateGameResponse) response;

        response = Service.listgames(authToken);
        assertInstanceOf(ListGameResponse.class, response);
        ListGameResponse listGameResponse = (ListGameResponse) response;
        assertEquals(1, listGameResponse.getGames().size());

//        BaseResponse response = UserSevice. assertEquals(1, games.size()); //how do I actually access the list of games?
//        assertTrue(games.contains(game));
    }
    @Test
    void createGameFailure() throws ResponseException{
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user);
        assertInstanceOf(RegisterResponse.class, response);
        RegisterResponse regResponse = (RegisterResponse) response;

        String authToken = regResponse.getAuthToken();
        response = Service.creategame(new GameData(0,null, null, null, null), authToken);
        assertInstanceOf(ErrorResponse.class, response);
        ErrorResponse errorResponse = (ErrorResponse) response;

        response = Service.listgames(authToken);
        assertInstanceOf(ListGameResponse.class, response);
        ListGameResponse listGameResponse = (ListGameResponse) response;
        assertEquals(0, listGameResponse.getGames().size());

        Service.clear();
    }


    @Test
    void joinGameSuccess() throws ResponseException{
        UserData user1 = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user1);
        RegisterResponse regResponse = (RegisterResponse) response;
        String lincolnauthToken = regResponse.getAuthToken();

        response = Service.creategame(new GameData(0,null, null, "abe's game", null), lincolnauthToken);

        UserData user2 = new UserData("george","washingoton" , "iluvmartha");
        response = Service.register(user2);
        regResponse = (RegisterResponse) response;
        String washingtonauthToken = regResponse.getAuthToken();

        response = Service.joingame("WHITE", 1, lincolnauthToken);
        assertInstanceOf(JoinGameResponse.class, response);

        response = Service.joingame("BLACK", 1, washingtonauthToken);
        assertInstanceOf(JoinGameResponse.class, response);
    }

    @Test
    void joinGameFailure() throws ResponseException{
        UserData user1 = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = Service.register(user1);
        RegisterResponse regResponse = (RegisterResponse) response;
        String lincolnauthToken = regResponse.getAuthToken();

        response = Service.creategame(new GameData(0,null, null, "abe's game", null), lincolnauthToken);

        UserData user2 = new UserData("george","washingoton" , "iluvmartha");
        response = Service.register(user2);
        regResponse = (RegisterResponse) response;
        String washingtonauthToken = regResponse.getAuthToken();

        response = Service.joingame("WHITE", 1, lincolnauthToken);
        assertInstanceOf(JoinGameResponse.class, response);

        response = Service.joingame("WHITE", 1, washingtonauthToken);
        assertInstanceOf(ErrorResponse.class, response);
    }
}