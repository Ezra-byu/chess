//package server;
//
//import com.google.gson.Gson;
//import spark.*;
//import java.util.*;
//
//public class ServerExample {
//
//    private String username;
//    private String password;
//    private String email;
//    private ArrayList<String> names = new ArrayList<>();
//
//    public static void main(String[] args) {
//        new ServerExample().run();
//    }
//
//    private void run() {
//        // Specify the port you want the server to listen on
//        Spark.port(8080);
//
//        // Register a directory for hosting static files
//        Spark.externalStaticFileLocation("public");
//
//        // Register handlers for each endpoint using the method reference syntax
//        Spark.post("/user", this::register);
//        Spark.get("/name", this::listNames);
//        Spark.delete("/name/:name", this::deleteName);
//    }
//
//    private Object register(Request req, Response res) throws ResponseException{
//        var user = new Gson().fromJson(req.body(), UserData.class); //UserData (a data model class) or just User?
//        user = service.register(user);
//        webSocketHandler.registrationHandler(user.username(), user.password(), user.email()); //what is this?
//        return new Gson().toJson(user);
//    }
//
//    private Object listNames(Request req, Response res) {
//        res.type("application/json");
//        return new Gson().toJson(Map.of("name", names));
//    }
//
//    private Object deleteName(Request req, Response res) {
//        names.remove(req.params(":name"));
//        return listNames(req, res);
//    }
//}