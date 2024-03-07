package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlGameDAO implements GameDAO {
    //add constructor to call createstatements
    //chess database initializer

    @Override
    public GameData createGame(GameData game) {
        //Insert into gameID (auto incremented)
        //Insert into whiteusername, black username, gamename,
        //serializeGame(game)
        //Insert serialized game
        //insert into user values ("abe", "lincoln", "abe@link")
        var statement = "INSERT INTO game (GameID, whiteUsername, blackUsername, gameName, chess) VALUES (?, ?, ?, ?, ?)";
        var jsonGame = serializeGame(game.game());
        var GameID = executeUpdate(statement, game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), jsonGame);
        return new GameData(GameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    }

    @Override
    public GameData getGame(int gameID) throws ResponseException { //my interface does not throw a response exception
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT GameID, whiteUsername, blackUsername, gameName, chess FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws ResponseException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM pet";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData updateGame(GameData game) {
        return null;
    }

    @Override
    public void clearGame() {

    }

    private GameData readGameData(ResultSet rs) throws SQLException {
        //GameID, whiteUsername, blackUsername, gameName, chess
        var GameID = rs.getInt("GameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var jsonGame = rs.getString("chess");
        var game = new Gson().fromJson(jsonGame, ChessGame.class);
        return new GameData(GameID, whiteUsername, blackUsername, gameName, game);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof GameData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
              `Game ID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar DEFAULT NULL,
              `blackUsername` varchar DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `chess` TEXT DEFAULT NULL,
              PRIMARY KEY (`GameID`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }
    private ChessGame deSerializeGame(String jsonGame) {
        return new Gson().fromJson(jsonGame, ChessGame.class);
    }
}
