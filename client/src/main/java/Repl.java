
//georgewash martha
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;
import serverFacade.ServerFacade;
import ui.TestFillUIDown;
import webSocket.NotificationHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import ui.TestFillUI;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Repl implements NotificationHandler{
//    private final PostLoginMenu postLogin;
//    private final PreLoginMenu preLogin;
    private State state = State.SIGNEDOUT;
    private GamePLayState gamePlayState = GamePLayState.NOTINGAME;
    static ServerFacade serverFacade;
    private final String serverUrl;

    AuthData sessionAuth;
    Integer sessionGameInt = 0;
    HashMap<Integer, GameData> sessionGames= new HashMap<Integer, GameData>();

    public Repl(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;

        sessionGameInt = 0;
        state = State.SIGNEDOUT;
        gamePlayState = GamePLayState.NOTINGAME;

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
                case "joinobserver" -> observeGame(params);

                case "drawtest" -> drawtest();
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
                state = State.SIGNEDOUT;
                return(e.toString());
            }
        }
        else {
            return "Please enter a username, password, and email";
        }
    }

    public String drawtest(){
        try {
            var gameList = serverFacade.listGames(sessionAuth.authToken());
            sessionGames.clear();
            sessionGameInt = 0;
            for (int i = 0; i < gameList.length; i++) {
                sessionGameInt += 1;
                sessionGames.put(sessionGameInt, gameList[i]);
            }
        } catch (ResponseException e) {
            return (e.toString());
        }
        GameData selectedGameData = sessionGames.get(1);
        //var createdGameRequest = new JoinGameRequest(null, selectedGameData.gameID());

        //TestFillUI.main();//was put in ChessBoardUIDOWN.main();
        selectedGameData.game().getBoard().toString2();
        TestFillUI.fillUI(selectedGameData.game().getBoard()); //put your board in here
        TestFillUIDown.fillUI(selectedGameData.game().getBoard());
        return "test over";
    }
    public String logIn(String... params){
        if (state == State.SIGNEDIN) {
            return "Can not perform command";
        }
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
                state = State.SIGNEDOUT;
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
                GameData gameObject = new GameData(0, null, null, gameName, null);

                //System.out.print("game " + gameName + " created ");
                //hashmap
                var createdGame = serverFacade.createGame(gameObject, sessionAuth.authToken());
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
            sessionGames.clear();
            sessionGameInt = 0;
            for (int i = 0; i < gameList.length; i++) {
                sessionGameInt += 1;
                sessionGames.put(sessionGameInt, gameList[i]);
            }
            for (Integer i : sessionGames.keySet()) {
                System.out.println("game: " + i + " value: " + sessionGames.get(i).gameName() + " Black Player: " + sessionGames.get(i).blackUsername() + " White Player: " + sessionGames.get(i).whiteUsername());
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
                GameData selectedGameData = sessionGames.get(gameNum);
                var createdGameRequest = new JoinGameRequest(color.toUpperCase(), selectedGameData.gameID());
                //Call the server join API to join them to the game (or verify the game exists if they are observing).
                serverFacade.joinGame(createdGameRequest, sessionAuth.authToken());

                //In phase 6 websocket server send LOAD_GAME which takes care of this
//                ChessBoardUIUP.main();
//                ChessBoardUIDOWN.main();
                InGameMenu inGameMenu = new InGameMenu(serverUrl, selectedGameData.gameID(), sessionAuth, color);
                inGameMenu.run();
                gamePlayState = GamePLayState.INGAME;


                return ("game " + gameNum + " " + selectedGameData.gameName() + " joined and left");
            } catch (ResponseException e) {
                return (e.toString());
            }
        }else{
            return "Please enter a color WHITE or BLACK and game number";
        }
    }

    public String observeGame(String... params){
        if (params.length == 1) {
            if (state == State.SIGNEDOUT) {
                return "Can not perform command";
            }
            try {
                Integer gameNum = Integer.parseInt(params[0]);
                GameData selectedGameData = sessionGames.get(gameNum);
                var createdGameRequest = new JoinGameRequest(null, selectedGameData.gameID());
                serverFacade.joinGame(createdGameRequest, sessionAuth.authToken());

                //TestFillUI.main();//was put in ChessBoardUIDOWN.main();
                InGameMenu inGameMenu = new InGameMenu(serverUrl, selectedGameData.gameID(), sessionAuth, null);
                inGameMenu.run();
                gamePlayState = GamePLayState.INGAME;

                return ("observed and left game " + gameNum);
            } catch (ResponseException e) {
                return (e.toString());
            }
        }else{
            return "Please enter a game number to join";
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
                - joingame <WHITE/BLACK> <#>
                - joinobserver <#>
                """;
    }

    private void printPrompt() {
        System.out.print("\n" + "\u001b[" + "0m" + ">>> ");
    }

    @Override
    public void notify(String message) {
        ServerMessage serverMessage= new Gson().fromJson(message, ServerMessage.class);
        System.out.print(message);

    }
}
