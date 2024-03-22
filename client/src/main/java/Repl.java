
//georgewash martha
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;
import serverFacade.ServerFacade;
import ui.ChessBoardUIDOWN;
import ui.ChessBoardUIUP;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Repl {
//    private final PostLoginMenu postLogin;
//    private final PreLoginMenu preLogin;
    private State state = State.SIGNEDOUT;
    static ServerFacade serverFacade;
    AuthData sessionAuth;
    Integer sessionGameInt;
    HashMap<Integer, GameData> sessionGames= new HashMap<Integer, GameData>();

    public Repl(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
        sessionGameInt = 0;
//        this.preLogin = preLogin;
//        this.postLogin = postLogin;
//        postLogin = new PostLoginMenu(serverUrl);
//        preLogin = new PreLoginMenu(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
                case "register" -> register(params);
                case "login" -> logIn(params);
                case "logout" -> logOut(params);
                case "creategame" -> createGame(params);
                case "listgames" -> listGames();
                case "joingame" -> joinGame(params);
//                case "join observer" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
            case "quit" -> "quit";
            default -> help();
        };
    }
    public String register(String... params){
        if(state == State.SIGNEDIN){
            return "Can not register. You are already logged in";
        }
        if (params.length == 3) {
            try {
                state = State.SIGNEDIN;
                String username = params[0];
                String password = params[1];
                String email = params[2];
                UserData userdata = new UserData(username, password, email);
                sessionAuth = serverFacade.register(userdata);
                return String.format(" You signed in as %s.", username);
            }
            catch(ResponseException e){
                return(e.toString());
            }
        }
        else {
            return "Please enter a username, password, and email";
        }
    }

    public String logIn(String... params){
        if (params.length == 2) {
            try {
                state = State.SIGNEDIN;
                String username = params[0];
                String password = params[1];
                UserData userdata = new UserData(username, password, null);
                sessionAuth = serverFacade.login(userdata);
                return String.format(" You signed in as %s.", username);
            }
            catch(ResponseException e){
                return(e.toString());
            }
        }
        else {
            return " Please enter a username and password";
        }

    }
    public String logOut(String... params){
        state = State.SIGNEDOUT;
        try {
            serverFacade.logout(sessionAuth.authToken());
            return("Now logged out");
        }catch(ResponseException e){
            return(e.toString());
        }
    }

    public String createGame(String... params){
        if (params.length == 1) {
            if (state == State.SIGNEDOUT) {
                return "Can not perform command";
            }
            try {
                String gameName = params[0];
                GameData GameObject = new GameData(0, null, null, gameName, null);

                //hashmap
                var createdGame = serverFacade.createGame(GameObject, sessionAuth.authToken());
//                sessionGame += 1;
//                sessionGames.put(sessionGame, createdGame);

                return ("game " + gameName + " created");
            } catch (ResponseException e) {
                //return (e.toString());
                return(" ");
            }
        }else{
            return "Please enter a game name";
        }
    }

    public String listGames(){
        if (state == State.SIGNEDOUT) {
            return "Can not perform command";
        }
        try {
            var gameList = serverFacade.listGames(sessionAuth.authToken());
            sessionGameInt += 1;
            for (int i = 0; i < gameList.length; i++) {
                sessionGameInt += 1;
                sessionGames.put(sessionGameInt, gameList[i]);
            }
            for (Integer i : sessionGames.keySet()) {
                System.out.println("game: " + i + " value: " + sessionGames.get(i).gameName());
            }
            return ("<end of games>");
        } catch (ResponseException e) {
            return (e.toString());
        }
    }

    public String joinGame(String... params){
        if (params.length == 2) {
            if (state == State.SIGNEDOUT) {
                return "Can not perform command";
            }
            try {
                String color = params[0];
                Integer gameNum = Integer.parseInt(params[1]);
                GameData SelectedGameData = sessionGames.get(gameNum);
                var createdGameRequest = new JoinGameRequest(color, SelectedGameData.gameID());
                serverFacade.joinGame(createdGameRequest, sessionAuth.authToken());
                ChessBoardUIUP.main();
                ChessBoardUIDOWN.main();
                return ("game " + gameNum + " joined");
            } catch (ResponseException e) {
                return (e.toString());
            }
        }else{
            return "Please enter a color WHITE or BLACK and game number";
        }
    }

    public String observeGame(String... params){
        if (params.length == 2) {
            if (state == State.SIGNEDOUT) {
                return "Can not perform command";
            }
            try {
                Integer gameNum = Integer.parseInt(params[0]);
                GameData SelectedGameData = sessionGames.get(gameNum);
                var createdGameRequest = new JoinGameRequest(null, SelectedGameData.gameID());
                serverFacade.joinGame(createdGameRequest, sessionAuth.authToken());
                ChessBoardUIUP.main();
                ChessBoardUIDOWN.main();
                return ("observing game " + gameNum);
            } catch (ResponseException e) {
                return (e.toString());
            }
        }else{
            return "Please enter a color WHITE or BLACK and game number";
        }
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - quit
                    - help
                    """;
        }
        return """
                - help
                - logout
                - creategame <gamename>
                - listgames
                - join game <WHITE/BLACK> <#>
                - join observer <#>
                """;
    }
    private void printPrompt() {
        System.out.print("\n" + "\u001b[" + "0m" + ">>> ");
    }

}
