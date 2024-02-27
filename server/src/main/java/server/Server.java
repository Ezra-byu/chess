package server;

import com.google.gson.Gson;
import model.AuthData;
import model.BaseResponse;
import model.RegisterResponse;
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
//        UserData user = new Gson().fromJson(req.body(), UserData.class); //UserData (a data model class) or just User?
//        RegisterResponse response = UserService.register(user);
//        res.status(response.responsecode());
//        return new Gson().toJson(response);
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        BaseResponse response = UserService.register(user);
        res.status(response.getStatusCode());
        return new Gson().toJson(response);
    }
}

