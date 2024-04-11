package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {

    public Integer getGameID(){
        return gameID;
    }
    Integer gameID;

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    ChessGame.TeamColor playerColor;
    public JoinPlayerCommand(String authToken) {
        super(authToken);
    }

}
