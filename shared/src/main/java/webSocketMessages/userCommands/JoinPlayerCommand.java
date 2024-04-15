package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {

    ChessGame.TeamColor playerColor;
    Integer gameID;
    public JoinPlayerCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public Integer getGameID(){
        return gameID;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

}
