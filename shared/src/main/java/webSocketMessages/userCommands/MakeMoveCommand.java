package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    ChessMove move;
    Integer gameID;
    public MakeMoveCommand(String authToken) {
        super(authToken);
    }
    public Integer getGameID(){
        return gameID;
    }
    public ChessMove getMove() {
        return move;
    }
}
