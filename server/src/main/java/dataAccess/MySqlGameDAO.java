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

public class MySqlGameDAO implements GameDAO{
    private int nextId = 1;//put in constructor?
    public MySqlGameDAO() {
        try {
            configureDatabase(); //creates db & tables if not created
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
    }



    @Override
    public GameData createGame(GameData game) {
        //Insert into gameID (auto incremented)
        //Insert into whiteusername, black username, gamename,
        //serializeGame(game)
        //Insert serialized game
        //insert into user values ("abe", "lincoln", "abe@link")
        try {
            var statement = "INSERT INTO game (GameID, whiteUsername, blackUsername, gameName, chess) VALUES (?, ?, ?, ?, ?)";
            var jsonGame = serializeGame(game.game());
            var GameID = nextId++;
            executeUpdate(statement, GameID, game.whiteUsername(), game.blackUsername(), game.gameName(), jsonGame);
            //added for phase 5 debug
            System.out.println("Database Game ID " + GameID + " " + game.gameName() + " Database game.whiteUsername " + game.whiteUsername());

            return new GameData(GameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
        return null;
    }

    @Override
    public GameData getGame(int gameID) { //my interface does not throw a response exception
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
            //throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
            System.out.println("Something went wrong." + e);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT GameID, whiteUsername, blackUsername, gameName, chess FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            //throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
            System.out.println("Something went wrong." + e);
        }
        return result;
    }

    @Override
    public GameData updateGame(GameData game) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, chess=? WHERE GameID =?";
            var jsonGame = serializeGame(game.game());
            try (var ps = conn.prepareStatement(statement)) {
                //ps.setString(1, game.whiteUsername());
                if (game.whiteUsername() != null){
                    ps.setString(1, game.whiteUsername());
                } else {
                    ps.setNull(1,  NULL);
                }
                if (game.blackUsername() != null){
                    ps.setString(2, game.blackUsername());
                } else {
                    ps.setNull(2,  NULL);
                }
                ps.setString(3, serializeGame(game.game()));
                ps.setInt(4, game.gameID());
                ps.executeUpdate();
                //added for phase 5 debug
                System.out.println("Database gameName " + game.gameName() + " Database white username " + game.whiteUsername()+ " Database black username: " + game.whiteUsername());

                return game;
            }
        }catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }catch (SQLException e) {
            System.out.println("Something went wrong." + e);
        }
        return null;
    }

    @Override
    public void clearGame() {
        try {
            var statement = "TRUNCATE game";
            executeUpdate(statement);
        }
        catch (DataAccessException e){
            System.out.println("Something went wrong." + e);
        }
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
            try (var psGame = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) psGame.setString(i + 1, p);
                    else if (param instanceof Integer p) psGame.setInt(i + 1, p);
                    else if (param instanceof GameData p) psGame.setString(i + 1, p.toString());
                    else if (param == null) psGame.setNull(i + 1, NULL);
                }
                psGame.executeUpdate();

                var rs = psGame.getGeneratedKeys();
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
              `GameID` int NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `chess` TEXT DEFAULT NULL,
              PRIMARY KEY (`GameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }
    private ChessGame deSerializeGame(String jsonGame) {
        return new Gson().fromJson(jsonGame, ChessGame.class);
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
