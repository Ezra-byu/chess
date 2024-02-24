import chess.*;
import server.Server;

//Phase 3
public class Main {
    public static void main(String[] args) {
        Server my_server = new Server();
        my_server.run(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}