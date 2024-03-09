package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTests{

    //    static UserDAO myUserDAO = new MemoryUserDAO(); //change upon completion of SQL database
    static UserDAO myUserDAO = new MySqlUserDAO();

    //    static AuthDAO myAuthDAO = new MemoryAuthDAO();
    static AuthDAO myAuthDAO = new MySqlAuthDAO();

    //    static GameDAO myGameDAO = new MemoryGameDAO();
    static GameDAO myGameDAO = new MySqlGameDAO();

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
}
