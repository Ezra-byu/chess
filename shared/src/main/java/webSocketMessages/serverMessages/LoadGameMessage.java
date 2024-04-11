package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage{
    public GameData game;
    public ChessGame.TeamColor playerColor;
    public ServerMessage.ServerMessageType type;

    public LoadGameMessage(ServerMessageType type, GameData game) {
        super(type);
        this.type = ServerMessageType.LOAD_GAME;
        this.game = game;
    }
}
