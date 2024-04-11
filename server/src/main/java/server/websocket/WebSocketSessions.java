//package server.websocket;
//
//import org.eclipse.jetty.websocket.api.Session;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class WebSocketSessions {
//    //!concurrent hash map!
//    public final ConcurrentHashMap<Integer, HashMap<String, Session>> connections = new ConcurrentHashMap<Integer, HashMap<String, Session>>();
//    public final Map<String, Session> innerMap = new HashMap<String, Session>();
//
//    public void addSessionToGame(Integer AuthToken, Session session) {
//        var connection = new Connection(AuthToken, session);
//        connections.put(AuthToken, connection);
//    }
//    public void removeSessionFromGame(String visitorName) {
//        connections.remove(visitorName);
//    }
//}
