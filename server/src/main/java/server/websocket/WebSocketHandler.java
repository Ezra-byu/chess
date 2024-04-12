package server.websocket;

import com.google.gson.Gson;
import dataAccess.GameDAO;
import dataAccess.MySqlGameDAO;
import dataAccess.MySqlUserDAO;
import dataAccess.UserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {
    private GameDAO myGameDAO = new MySqlGameDAO();
    private UserDAO myUserDAO = new MySqlUserDAO();

    private final ConnectionManager connections = new ConnectionManager();
    //@OnWebSocketConnect
    //onConnect
    //@OnWebSocketClose
    //

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        //GameCommand command = readJson(msg, UserGameCommand.class);
        UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
        var conn = connections.getConnection(command.getAuthString(), session);
        //var conn = connections(command.authToken, session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join(conn, msg);
                //case JOIN_OBSERVER -> observe(conn, msg);
                //case MAKE_MOVE -> move(conn, msg));
                //case LEAVE -> leave(conn, msg);
                //case RESIGN -> resign(conn, msg);
            }
        } else {
            //Connection.sendError(session.getRemote(), "unknown user");
        }
    }


//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }

    private void join(Connection conn, String msg){
        try {
            JoinPlayerCommand joinCommand = new Gson().fromJson(msg, JoinPlayerCommand.class);
            String authToken = conn.authToken;
            Integer gameID = conn.gameID;
            connections.add(joinCommand.getGameID(), authToken, conn.session);
            //Game DAO call
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, myGameDAO.getGame(joinCommand.getGameID()));
            //Server sends a LOAD_GAME message back to the root client.
            connections.rootusersend(authToken, gameID, loadGameMessage);
            //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
            var message = String.format("so and so has joined the game");
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            //connections.rootusersend(authToken, loadGameMessage);
            connections.broadcast(authToken, gameID, notification);
        }
        catch (IOException e){
            System.out.println("Something went wrong in websocket handler join." + e);
        }

    }

}