package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String AuthToken;
    public Session session;

    public Connection(String authtoken, Session session) {
        this.AuthToken = authtoken;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
//    public static void sendError(String msg) throws IOException{
//        session.getRemote().sendString(msg);
//    }
}