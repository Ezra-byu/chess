package webSocket;

//import webSocketMessages.Notification;
import webSocketMessages.serverMessages.NotificationMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notification);//sends to the repl
}
