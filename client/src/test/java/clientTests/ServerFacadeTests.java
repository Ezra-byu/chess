package clientTests;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;
import model.responses.ListGameResponse;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;
import service.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        serverFacade = new ServerFacade(url);
    }
    @BeforeEach
    void clear() throws ResponseException {
        Service.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerSuccess() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));
        Assertions.assertNotNull(returnedAuth);
        Assertions.assertNotNull(returnedAuth.authToken());
    }

    @Test
    void registerFailure() {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));
        Assertions.assertNotNull(returnedAuth);
        Assertions.assertNotNull(returnedAuth.authToken());

        var response = assertThrows(ResponseException.class, () -> serverFacade.register(createdUser));


    }

    @Test
    void loginSuccess() {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        //try login without register
        var response = assertThrows(ResponseException.class, () -> serverFacade.login(createdUser));

        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));
        returnedAuth = assertDoesNotThrow(() -> serverFacade.login(createdUser));
        Assertions.assertNotNull(returnedAuth);
        Assertions.assertNotNull(returnedAuth.authToken());

    }

    @Test
    void loginFailure() {
        var createdUser = new UserData("john", "jacob", null);
        //try login without register
        var response = assertThrows(ResponseException.class, () -> serverFacade.login(createdUser));
    }

    @Test
    void logoutSuccess() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));

        Assertions.assertNotNull(returnedAuth);
        Assertions.assertNotNull(returnedAuth.authToken());

        assertDoesNotThrow(() -> serverFacade.logout(returnedAuth.authToken()));
    }

    @Test
    void logoutFailure() throws ResponseException {
        var createdUser = new UserData("john", "jacob", null);
        //try logout without login
        var response = assertThrows(ResponseException.class, () -> serverFacade.login(createdUser));
    }

    @Test
    void listGamesSuccess() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));

        var gameList = assertDoesNotThrow(() -> serverFacade.listGames(returnedAuth.authToken()));
        assertEquals(0, gameList.length);

        GameData createdGame = new GameData(0, null, null, "mygame", null);
        var returnedGame = serverFacade.createGame(createdGame, returnedAuth.authToken());
        var createdGameRequest = new JoinGameRequest("WHITE", returnedGame.gameID());
        assertDoesNotThrow(() -> serverFacade.joinGame(createdGameRequest, returnedAuth.authToken()));
    }
    @Test
    void listGamesFailure() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));

        GameData createdGame1 = new GameData(0, null, null, "mygame1", null);
        GameData createdGame2 = new GameData(0, null, null, "mygame2", null);
        var returnedGame1 = serverFacade.createGame(createdGame1, returnedAuth.authToken());
        var returnedGame2 = serverFacade.createGame(createdGame2, returnedAuth.authToken());
        var gameList = assertDoesNotThrow(() -> serverFacade.listGames(returnedAuth.authToken()));
        assertEquals(2, gameList.length);
        assertNotEquals(1, gameList.length);
    }

    @Test
    void createGameSuccess() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));

        GameData createdGame1 = new GameData(0, null, null, "mygame1", null);
        GameData createdGame2 = new GameData(0, null, null, "mygame2", null);

        var returned = serverFacade.createGame(createdGame1, returnedAuth.authToken());
        assertEquals(1, returned.gameID());

        returned = serverFacade.createGame(createdGame2, returnedAuth.authToken());
        assertEquals(2, returned.gameID());

    }

    @Test
    void createGameFailure() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));


        GameData createdGame1 = new GameData(0, null, null, null, null);
        var badResponse = assertThrows(ResponseException.class, () -> serverFacade.createGame(createdGame1, returnedAuth.authToken()));

        GameData createdGame2 = new GameData(0, null, null, "mygame", null);
        var goodResponse = assertDoesNotThrow(() -> serverFacade.createGame(createdGame2, returnedAuth.authToken()));
    }

    @Test
    void joinGameSuccess() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));

        GameData createdGame1 = new GameData(0, null, null, "mygame1", null);
        var returnedGame = serverFacade.createGame(createdGame1, returnedAuth.authToken());
        var createdGameRequest = new JoinGameRequest("WHITE", returnedGame.gameID());
        assertDoesNotThrow(() -> serverFacade.joinGame(createdGameRequest, returnedAuth.authToken()));
    }

    @Test
    void joinGameFailure() throws ResponseException {
        var createdUser = new UserData("john", "jacob", "jingheimer@gmail");
        var returnedAuth = assertDoesNotThrow(() -> serverFacade.register(createdUser));

        var createdGameRequest = new JoinGameRequest("WHITE", 1);
        var response = assertThrows(ResponseException.class, () -> serverFacade.joinGame(createdGameRequest, returnedAuth.authToken()));

        GameData createdGame = new GameData(0, null, null, "mygame1", null);
        var returnedGame = serverFacade.createGame(createdGame, returnedAuth.authToken());
        assertDoesNotThrow(() -> serverFacade.joinGame(createdGameRequest, returnedAuth.authToken()));
    }

}
