package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public GameData createGame(GameData game) {
        games.put(game.gameID(), game);
        return game;
    }

    @Override
    public GameData getGame(GameData game) {
        return games.get(game.gameID());
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public GameData updateGame(GameData game) {
        return null;
    }

    @Override
    public void clearGame(){
        games.clear();
    }
}
