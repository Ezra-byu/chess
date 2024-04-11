package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
//import webSocketMessages.*;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager { //organizes session objects
    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>(); //gameID, Connection(authToken, Session)

    public void add(Integer gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(gameID, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    //public void broadcast(String excludeVisitorName, Notification notification) throws IOException {
    //notification = ServerMessage
    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
    var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.AuthToken.equals(excludeVisitorName)) {
                    c.send(notification.toString()); //should be Server message body?
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.AuthToken);
        }
    }


    public Connection getConnection(String authToken, Session session) {
        return new Connection(authToken, session);
    }
}
