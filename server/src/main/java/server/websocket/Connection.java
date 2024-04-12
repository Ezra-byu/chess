package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.JoinPlayerCommand;

import java.io.IOException;

public class Connection {
    public Integer gameID;
    public String authToken;
    public Session session;


    public Connection(Integer gameID,  String authToken, Session session) {
        this.gameID = gameID;
        this.session = session;
        this.authToken = authToken;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(new Gson().toJson(msg)); //Converts msg to json
    }
//    public static void sendError(String msg) throws IOException{
//        session.getRemote().sendString(msg);
//    }

//    @Override
//    public String toString() {
//        return "Connection{" +
//                "gameID=" + gameID +
//                ", authToken='" + authToken + '\'' +
//                ", session=" + session +
//                '}';
//    }
}