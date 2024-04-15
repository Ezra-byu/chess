package webSocket;

//import webSocketMessages.Notification;
import webSocketMessages.serverMessages.NotificationMessage;

public interface NotificationHandler {
    void notify(String message);//sends to the repl
}
