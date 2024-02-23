package server;

import com.google.gson.Gson;
import spark.*;
import model.UserData;
import service.UserService;

public class Server {

    private final UserService registerService = new UserService(); // on Pet service what does this do?
    //private final WebSocketHandler webSocketHandler;

    public int run(int desiredPort) { //this one returns an int. Know that pet returns PetServer
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //register
        Spark.post("/user", this::register);

        //catch undirected calls
        //server.createContext("/", new FileHandler());



        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class); //UserData (a data model class) or just User?
        UserService.register(user);
        return new Gson().toJson(user);
    }
}
