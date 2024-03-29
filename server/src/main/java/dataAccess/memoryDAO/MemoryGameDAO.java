package dataAccess.memoryDAO;

import chess.ChessGame;
import dataAccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();//{gameID, GameData}
    @Override
    public GameData createGame(GameData game) {
        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        GameData newgame = new GameData(nextId++, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        games.put(newgame.gameID(), newgame);
        return newgame;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public GameData updateGame(GameData game) {
        int savedID = game.gameID();
        games.remove(game.gameID());
        GameData newgame = new GameData(savedID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        games.put(newgame.gameID(), newgame);
        return newgame;
    }

    @Override
    public void clearGame(){
        games.clear();
        nextId = 1;
    }
}
