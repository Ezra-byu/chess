package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
//import webSocketMessages.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager { //organizes session objects

    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>(); //authToken, Connection(gameID, AuthToken, Session)

    public void add(Integer gameID, String authToken, Session session) {
        var connection = new Connection(gameID, authToken, session);
        connections.put(authToken, connection);
    }
    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    //public void broadcast(String excludeVisitorName, Notification notification) throws IOException {
    //notification = ServerMessage
    public void broadcast(String visitorName, Integer gameID, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(visitorName)) {
                    //make sure same game c.gameID.equals(gameID)
                    if (c.gameID.equals(gameID)) {
                        c.send(notification.toString());
                    }
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void rootusersend(String visitorName, Integer GameID, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.authToken.equals(visitorName)) {
                    //make sure same game c.gameID.equals(GameID)
                    if (c.gameID.equals(GameID)) {
                        c.send(notification.toString());
                        System.out.print(c.toString());
                    }
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }
//    public Connection getConnection(Integer gameID, String authToken, Session session) {
//        //return connections.get(authToken);
//        return new Connection(gameID, authToken, session);
//    }

    public Connection getConnection(String authToken, Session session) {
        return new Connection(null, authToken, session);
    }
}
