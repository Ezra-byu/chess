package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

    public String getErrorMessage() {
        return errorMessage;
    }

    public String errorMessage;
    public ServerMessage.ServerMessageType type;
    public ErrorMessage(ServerMessageType type, String message) {
        super(type);
        this.errorMessage = message;
        this.type = ServerMessageType.ERROR;
    }
}
