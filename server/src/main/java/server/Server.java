package server;

import com.google.gson.Gson;
import model.*;
import spark.*;
import service.UserService;

public class Server {

    private final UserService registerService = new UserService(); // on Pet service what does this do?
    //private final WebSocketHandler webSocketHandler;

    public int run(int desiredPort) { //this one returns an int. Know that pet returns PetServer
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listgames);
        Spark.post("/game", this::creategame);
        Spark.put("/game", this::joingame);


        //catch undirected calls
        //server.createContext("/", new FileHandler());



        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        BaseResponse response = UserService.clear();
        res.status(response.getStatusCode());
        return new Gson().toJson(response);
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

    private Object login(Request req, Response res){
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        BaseResponse response = UserService.login(user);
        res.status(response.getStatusCode());
        return new Gson().toJson(response);
    }

    private Object logout(Request req, Response res){
        String authToken = req.headers("authorization");
        BaseResponse response = UserService.logout(authToken);
        res.status(response.getStatusCode());
        return new Gson().toJson(response);
    }

    private Object listgames(Request req, Response res){
        String authToken = req.headers("authorization");
        BaseResponse response = UserService.listgames(authToken);
        res.status(response.getStatusCode());
        return new Gson().toJson(response);
    }
    private Object creategame(Request req, Response res){
        String authToken = req.headers("authorization");
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        BaseResponse response = UserService.creategame(game, authToken);
        res.status(response.getStatusCode());
        return new Gson().toJson(response);
    }

    private Object joingame(Request req, Response res){
        String authToken = req.headers("authorization");
        JoinGameRequest joinrequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
        BaseResponse response = UserService.joingame(joinrequest.playerColor(), joinrequest.gameID(), authToken);
        res.status(response.getStatusCode());
        return new Gson().toJson(response);
    }
}

