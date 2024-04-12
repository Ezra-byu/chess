package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

    public String message;
    public ServerMessage.ServerMessageType type;
    public ErrorMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
        this.type = ServerMessageType.ERROR;
    }
}
