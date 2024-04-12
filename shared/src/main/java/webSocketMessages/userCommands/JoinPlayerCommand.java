package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {

    ChessGame.TeamColor playerColor;
    Integer gameID;
    public JoinPlayerCommand(String authToken) {
        super(authToken);
    }

    public Integer getGameID(){
        return gameID;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

}
