package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import server.websocket.ui.TestFillUI;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {
    private GameDAO myGameDAO = new MySqlGameDAO();
    private UserDAO myUserDAO = new MySqlUserDAO();
    private AuthDAO myAuthDAO = new MySqlAuthDAO();

    private final ConnectionManager connections = new ConnectionManager();
    //@OnWebSocketConnect
    //@OnWebSocketClose

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        //GameCommand command = readJson(msg, UserGameCommand.class);
        UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
        //make sure that the authToken and session are not null(does not test GameID)
        var conn = connections.getConnection(command.getAuthString(), session);
        //var conn = connections(command.authToken, session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join(conn, msg);
                case JOIN_OBSERVER -> observe(conn, msg);
                case MAKE_MOVE -> move(conn, msg);
                case LEAVE -> leave(conn, msg);
                case RESIGN -> resign(conn, msg);
            }
        } else {
            //Connection.sendError(session.getRemote(), "unknown user");
        }
    }


    private void join(Connection conn, String msg){
        try {
            JoinPlayerCommand joinCommand = new Gson().fromJson(msg, JoinPlayerCommand.class);
            String authToken = joinCommand.getAuthString();
            Integer gameID = joinCommand.getGameID();
            ChessGame.TeamColor playerColor = joinCommand.getPlayerColor();
            connections.add(joinCommand.getGameID(), authToken, conn.session);

            //Game DAO call, get the game, see if the usernames are the same
            GameData gameToJoin = myGameDAO.getGame(gameID);

            if (gameToJoin == null){
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: no game exists");
                connections.rootusersend(authToken, gameID, errorMessage);
                return;
            }
            if (!myAuthDAO.checkAuth(authToken)){
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad auth");
                connections.rootusersend(authToken, gameID, errorMessage);
                return;
            }

            String joinCommandUsername = myAuthDAO.getAuth(authToken).username();
            //if requesting black, joincommandUsername should match httpBlackUsername
            //if requesting white, joincommand username should match http WhiteUsername
            if(playerColor == ChessGame.TeamColor.WHITE){
                if(!Objects.equals(joinCommandUsername, gameToJoin.whiteUsername())){
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: wrong color selected");
                    connections.rootusersend(authToken, gameID, errorMessage);
                    return;
                }
            } else if(playerColor == ChessGame.TeamColor.BLACK){
                if(!Objects.equals(joinCommandUsername, gameToJoin.blackUsername())){
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: wrong color selected");
                    connections.rootusersend(authToken, gameID, errorMessage);
                    return;
                }
            }
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, myGameDAO.getGame(joinCommand.getGameID()));
            //Server sends a LOAD_GAME message back to the root client.
            connections.rootusersend(authToken, gameID, loadGameMessage);
            //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
            var message = String.format("Joined game: " + joinCommandUsername);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            //connections.rootusersend(authToken, loadGameMessage);
            connections.broadcast(authToken, gameID, notification);
        }
        catch (IOException e){
            System.out.println("Something went wrong in websocket handler join: " + e);
        }

    }
    private void observe(Connection conn, String msg){
        try {
            JoinObserverCommand observeCommand = new Gson().fromJson(msg, JoinObserverCommand.class);
            String authToken = observeCommand.getAuthString();
            Integer gameID = observeCommand.getGameID();
            connections.add(gameID, authToken, conn.session);

            GameData gameToJoin = myGameDAO.getGame(gameID);
            if (gameToJoin == null){
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: no game exists");
                connections.rootusersend(authToken, gameID, errorMessage);
                return;
            }
            if (!myAuthDAO.checkAuth(authToken)){
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad auth");
                connections.rootusersend(authToken, gameID, errorMessage);
                return;
            }

            String observeCommandUsername = myAuthDAO.getAuth(authToken).username();

            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, myGameDAO.getGame(observeCommand.getGameID()));
            //Server sends a LOAD_GAME message back to the root client.
            connections.rootusersend(authToken, gameID, loadGameMessage);


            var message = String.format("joinied game:  " + observeCommandUsername);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(authToken, gameID, notification);


        } catch (IOException e){
            System.out.println("Something went wrong in websocket handler observe: " + e);
        }
    }

    private void move(Connection conn, String msg) {
        try{
            MakeMoveCommand makeMoveCommand = new Gson().fromJson(msg, MakeMoveCommand.class);
            String authToken = makeMoveCommand.getAuthString();
            Integer gameID = makeMoveCommand.getGameID();
            ChessMove move = makeMoveCommand.getMove();
            GameData game = myGameDAO.getGame(gameID);
            ChessGame myGame = game.game();
            String userName = myAuthDAO.getAuth(authToken).username();
            var currentTurn = myGame.getTeamTurn();


            ChessBoard myBoard = game.game().getBoard();
            TestFillUI.fillUI(myBoard);
            //System.out.println(myBoard.toString2());

            if(myGame.isInCheck(ChessGame.TeamColor.WHITE) || myGame.isInCheck(ChessGame.TeamColor.BLACK)){
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: In check");
                connections.rootusersend(authToken, gameID, errorMessage);
                return;
            }

            if(currentTurn == ChessGame.TeamColor.WHITE){
                if (!Objects.equals(userName, game.whiteUsername())){
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad move");
                    connections.rootusersend(authToken, gameID, errorMessage);
                    return;
                }
            }
            if(currentTurn == ChessGame.TeamColor.BLACK){
                if (!Objects.equals(userName, game.blackUsername())){
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad move");
                    connections.rootusersend(authToken, gameID, errorMessage);
                    return;
                }
            }

            //Server verifies the validity of the move.
            //Game is updated to represent the move. Game is updated in the database.
            try {
                myGame.makeMove(move);
                myGameDAO.updateGame(game);
            } catch (InvalidMoveException e) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad move");
                connections.rootusersend(authToken, gameID, errorMessage);
                return;
            }



            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, myGameDAO.getGame(makeMoveCommand.getGameID()));
            //Server sends a LOAD_GAME message back to the root client.
            connections.rootusersend(authToken, gameID, loadGameMessage);
            connections.broadcast(authToken, gameID, loadGameMessage);

            var message = String.format("Move made:  " + move.toString());
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(authToken, gameID, notification);


        } catch (IOException e){
            System.out.println("Something went wrong in websocket handler observe: " + e);
        }
    }
    private void leave(Connection conn, String msg){
        LeaveCommand leaveCommand = new Gson().fromJson(msg, LeaveCommand.class);
        String authToken = leaveCommand.getAuthString();
        Integer gameID = leaveCommand.getGameID();
        String leaveCommandUsername = myAuthDAO.getAuth(authToken).username();
        GameData gameToLeave = myGameDAO.getGame(gameID);

        if(Objects.equals(leaveCommandUsername, gameToLeave.whiteUsername())){
            GameData gameWhiteRemoved = new GameData(gameToLeave.gameID(), null, gameToLeave.blackUsername(), gameToLeave.gameName(), gameToLeave.game());
            myGameDAO.updateGame(gameWhiteRemoved);
        }else if(Objects.equals(leaveCommandUsername, gameToLeave.blackUsername())){
            GameData gameBlackRemoved = new GameData(gameToLeave.gameID(), gameToLeave.whiteUsername(), null, gameToLeave.gameName(), gameToLeave.game());
            myGameDAO.updateGame(gameBlackRemoved);
        }
        connections.remove(authToken);
        try {
            var message = String.format("Has left:  " + leaveCommandUsername);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(authToken, gameID, notification);
        } catch (IOException e) {
            System.out.println("Something went wrong in websocket handler : " + e);
        }
    }

    private void resign(Connection conn, String msg){
        ResignCommand resignCommand = new Gson().fromJson(msg, ResignCommand.class);
        String authToken = resignCommand.getAuthString();
        Integer gameID = resignCommand.getGameID();
        String resignCommandUsername = myAuthDAO.getAuth(authToken).username();
        GameData gameToResign = myGameDAO.getGame(gameID);

        //gameToResign.isOver(true);

        if(Objects.equals(resignCommandUsername, gameToResign.whiteUsername())){
            GameData gameWhiteRemoved = new GameData(gameToResign.gameID(), null, gameToResign.blackUsername(), gameToResign.gameName(), gameToResign.game());
            myGameDAO.updateGame(gameWhiteRemoved);
        }else if(Objects.equals(resignCommandUsername, gameToResign.blackUsername())){
            GameData gameBlackRemoved = new GameData(gameToResign.gameID(), gameToResign.whiteUsername(), null, gameToResign.gameName(), gameToResign.game());
            myGameDAO.updateGame(gameBlackRemoved);
        }else{ //if username isn't white or black
            try {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: you are not a player in this game");
                connections.rootusersend(authToken, gameID, errorMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            var message = String.format("Game over. Has resigned:  " + resignCommandUsername);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(authToken, gameID, notification);
            connections.rootusersend(authToken, gameID, notification);
        } catch (IOException e) {
            System.out.println("Something went wrong in websocket handler : " + e);
        }
        connections.remove(authToken);
    }

}