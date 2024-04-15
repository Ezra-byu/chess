import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.GameDAO;
import dataAccess.MySqlGameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import ui.TestFillUI;
import webSocket.NotificationHandler;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class InGameMenu implements NotificationHandler {

    private GameDAO myGameDAO = new MySqlGameDAO();
        
    private final String serverUrl;
    private final Integer gameID;
    private final String color;
    AuthData sessionAuth;

    WebSocketFacade ws;
    ChessGame.TeamColor enumColor;


    public InGameMenu(String serverUrl, Integer gameID, AuthData sessionAuth, String color) {
        this.serverUrl = serverUrl;
        this.gameID = gameID;
        this.sessionAuth = sessionAuth;
        this.color = color;

        try {
            ws = new WebSocketFacade(serverUrl, this);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

//    public InGameMenu(String serverUrl) {
//        this.serverUrl = serverUrl;
//    }



    public void run() {
        System.out.println("Entering gameplay UI");
        if (color != null){
            System.out.print(join());
        }else{
            System.out.print(observe());
        }

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
            case "redraw" -> redraw();
            //case "leave" -> leave(params);
            //case "makemove" -> makeMove(params);
            //case "resign" -> resign(params);
            //case "highlight" -> highlightMoves();
            //case "quit" -> "quit";
            default -> help();
        };
    }

    public String redraw(){
        GameData sessionGame = myGameDAO.getGame(gameID);
        ChessBoard myBoard = sessionGame.game().getBoard();
        if(color.equalsIgnoreCase("black")){TestFillUI.fillUI(myBoard);}
        else{TestFillUI.fillUI(myBoard);}
        return "Board has been drawn";
        //debug helps
        //System.out.println(myBoard.toString2());
    }
    public String observe(){
        try {
            ws.wsObserve(sessionAuth.authToken(), gameID);
            return "you have joined " + myGameDAO.getGame(gameID).gameName();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public String join(){
        try {
            if(color.equalsIgnoreCase("white")){enumColor = ChessGame.TeamColor.WHITE;}
            else{enumColor = ChessGame.TeamColor.BLACK;}

            ws.wsJoin(sessionAuth.authToken(), gameID, enumColor);
            return "you have joined " + myGameDAO.getGame(gameID).gameName();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public String help() {
        return """
                - redraw
                - leave
                - makemove <piece> <move>
                - resign
                - highlight <piece>
                """;
    }
    private void printPrompt() {
        System.out.print("\n" + "\u001b[" + "0m" + ">>> ");
    }


    @Override
    public void notify(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        var messageType = serverMessage.getServerMessageType();
        switch(messageType) {
            case LOAD_GAME -> processLoadGame(message);
            case ERROR -> processError(message);
            case NOTIFICATION -> processNotification(message);
        }
    }

    private void processNotification(String message){
        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
        System.out.print(notificationMessage.getMessage()); //prints the message contained in the notification object
    }

    private void processError(String message){
        ErrorMessage notificationMessage = new Gson().fromJson(message, ErrorMessage.class);
        System.out.print(notificationMessage.getErrorMessage()); //prints the message contained in the error object
    }

    private void processLoadGame(String message){
        redraw();
    }

}
