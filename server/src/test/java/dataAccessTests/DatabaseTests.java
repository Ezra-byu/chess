package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DatabaseTests{

    //    static UserDAO myUserDAO = new MemoryUserDAO(); //change upon completion of SQL database
    static UserDAO myUserDAO = new MySqlUserDAO();

    //    static AuthDAO myAuthDAO = new MemoryAuthDAO();
    static AuthDAO myAuthDAO = new MySqlAuthDAO();

//    static GameDAO myGameDAO = new MemoryGameDAO();
    static GameDAO myGameDAO = new MySqlGameDAO();

    @BeforeEach
    void clear() throws ResponseException {
        Service.clear();
    }

    //game tests
    //  createGame
    //  getGame
    //  listGames
    //  updateGame
    //  clearGame
    @Test
    void createGameSuccess() {
        //create a game
        //using the returned games ID get the game
        //makes sure same dang game
        ChessGame testGame1 = new ChessGame();
        GameData testGame = new GameData(0, "white", null, "myGame1", testGame1);//GameID is ignored
        GameData returnedGame = myGameDAO.createGame(testGame);
        int returnedGameID = returnedGame.gameID();
        GameData verifyGame = myGameDAO.getGame(returnedGameID);
        assertEquals(returnedGame.gameID(), verifyGame.gameID());
    }

    @Test
    void createGameFailure() {
        ChessGame testGame1 = new ChessGame();
        GameData testGame = new GameData(125, "white", null, "myGame1", testGame1);//GameID is ignored
        GameData returnedGame = myGameDAO.createGame(testGame);
        int returnedGameID = returnedGame.gameID();

        assertNotEquals(125, returnedGameID); // make sure returned GameID is generated by createGame incr
    }

    @Test
    void getGameSuccess() {
        ChessGame testGame3 = new ChessGame();
        GameData testGame = new GameData(0, "white", null, "myGame3", testGame3);//GameID is ignored
        GameData returnedGame = myGameDAO.createGame(testGame);
        int returnedGameID = returnedGame.gameID();
        GameData verifyGame = myGameDAO.getGame(returnedGameID);
        assertEquals(returnedGame.gameID(), verifyGame.gameID());
    }

    @Test
    void getGameFailure() {
        ChessGame testGame3 = new ChessGame();
        GameData testGame = new GameData(0, "white", null, "myGame3", testGame3);//GameID is ignored
        GameData returnedGame = myGameDAO.createGame(testGame);
        int returnedGame3ID = returnedGame.gameID();

        ChessGame testGame4 = new ChessGame();
        GameData testGameData4 = new GameData(0, "white", null, "myGame4", testGame4);//GameID is ignored
        GameData returnedGame4 = myGameDAO.createGame(testGameData4);
        int returnedGame4ID = returnedGame4.gameID();

        GameData verifyGame3 = myGameDAO.getGame(returnedGame3ID); //can the first game be got after others added?
        GameData verifyGame4 = myGameDAO.getGame(returnedGame4ID);
        assertNotEquals(verifyGame3.gameID(), verifyGame4.gameID());
    }

    @Test
    void  listGamesSuccess() {
        ChessGame testGame = new ChessGame();
        GameData testGameData1 = new GameData(0, "white", null, "myGame", testGame);//GameID is ignored
        GameData returnedGame1 = myGameDAO.createGame(testGameData1);

        ChessGame testGame2 = new ChessGame();
        GameData testGameData2 = new GameData(0, "white", null, "myGame2", testGame2);//GameID is ignored
        GameData returnedGame2 = myGameDAO.createGame(testGameData2);

        var users = myGameDAO.listGames();
        assertEquals(2, users.size());
    }

    @Test
    void  listGamesFailure() {
        ChessGame testGame = new ChessGame();
        GameData testGameData1 = new GameData(0, "white", null, "myGame", testGame);//GameID is ignored
        GameData returnedGame1 = myGameDAO.createGame(testGameData1);

        ChessGame testGame2 = new ChessGame();
        GameData testGameData2 = new GameData(0, "white", null, "myGame2", testGame2);//GameID is ignored
        GameData returnedGame2 = myGameDAO.createGame(testGameData2);

        var users = myGameDAO.listGames();
        assertNotEquals(3, users.size());
    }

    @Test
    void updateGameSuccess() {
        ChessGame testGame = new ChessGame();
        GameData testGameData1 = new GameData(0, null, null, "myGame", testGame);//GameID is ignored
        GameData enteredGame1 = myGameDAO.createGame(testGameData1);

        GameData wantedUpdates = new GameData(enteredGame1.gameID(), "white", null, "myGame", testGame);
        GameData updatedGame1 = myGameDAO.updateGame(wantedUpdates);

        //assertEquals(enteredGame1.whiteUsername(), updatedGame1.whiteUsername());
        assertEquals("white", updatedGame1.whiteUsername());
    }

    @Test
    void updateGameFailure() {
        ChessGame testGame = new ChessGame();
        GameData testGameData1 = new GameData(0, null, null, "myGame", testGame);//GameID is ignored
        GameData enteredGame1 = myGameDAO.createGame(testGameData1);

        GameData wantedUpdates = new GameData(enteredGame1.gameID(), "white", null, "myGame", testGame);
        GameData updatedGame1 = myGameDAO.updateGame(wantedUpdates);

        //white username shouldn't be the same before and after, if truly updated
        assertNotEquals(enteredGame1.whiteUsername(), updatedGame1.whiteUsername());
    }

    @Test
    void clearGameSuccess() {
        ChessGame testGame1 = new ChessGame();
        GameData testGameData = new GameData(0, "white", null, "myGame3", testGame1);//GameID is ignored
        GameData returnedGame = myGameDAO.createGame(testGameData);

        var users = myGameDAO.listGames();
        assertEquals(1, users.size());

        ChessGame testGame2 = new ChessGame();
        GameData testGameData2 = new GameData(0, "white", null, "myGame3", testGame2);//GameID is ignored
        GameData returnedGame2 = myGameDAO.createGame(testGameData2);

        users = myGameDAO.listGames();
        assertEquals(2, users.size());

        myGameDAO.clearGame();
        users = myGameDAO.listGames();
        assertEquals(0, users.size());
    }



}