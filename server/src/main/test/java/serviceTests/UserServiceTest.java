package serviceTests;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    //test for username already taken
    //test_user_service = new UserService();

    @BeforeEach
    void clear() throws ResponseException {
        UserService.clear();
    }

    @Test
    void registerSuccess() throws ResponseException {
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = UserService.register(user);
        assertInstanceOf(RegisterResponse.class, response);
        RegisterResponse regResponse = (RegisterResponse) response;
    }
//    @Test
//    void registerFail() throws ResponseException {
//
//    }


    void creategame() throws ResponseException{
        UserData user = new UserData("abelincoln","abelincoln1" , "emancip8r");
        BaseResponse response = UserService.register(user);
        assertInstanceOf(RegisterResponse.class, response);
        RegisterResponse regResponse = (RegisterResponse) response;

        String authToken = regResponse.getAuthToken();
        response = UserService.creategame(new GameData(0,null, null, "abe's game", null), authToken);
        assertInstanceOf(CreateGameResponse.class, response);
        CreateGameResponse createGameResponse = (CreateGameResponse) response;

        response = UserService.listgames(authToken);
        assertInstanceOf(ListGameResponse.class, response);
        createGameResponse = (CreateGameResponse) response;

//        BaseResponse response = UserSevice. assertEquals(1, games.size()); //how do I actually access the list of games?
//        assertTrue(games.contains(game));

    }
}