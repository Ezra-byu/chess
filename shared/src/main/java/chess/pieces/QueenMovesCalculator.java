package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class QueenMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    private int row;
    private int col;
    private int rowincr;
    private int colincr;
    private ChessPosition thetempposition;
    Set<ChessMove> movesSet = new HashSet<ChessMove>();
    public QueenMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        diagonalChecker(1, 1, board, position);
        diagonalChecker(1, -1, board, position);
        diagonalChecker(-1, 1, board, position);
        diagonalChecker(-1, -1, board, position);

        diagonalChecker(0, 1, board, position);
        diagonalChecker(0, -1, board, position);
        diagonalChecker(1, 0, board, position);
        diagonalChecker(-1, 0, board, position);

        return movesSet;
    }
    private void diagonalChecker(int rowincr, int colincr, ChessBoard board, ChessPosition position) {
//        System.out.println("in loop");
        row = position.getRow() ;
        col = position.getColumn();
        row += rowincr;
        col += colincr;
        while((row >= 1) && (row <= 8) && (col >= 1) && (col <= 8)){
            thetempposition = new ChessPosition(row , col); //create the position being investigated
            if (board.getPiece(thetempposition) != null) { //if there is a piece in the position being investigated
                if (board.getPiece(thetempposition).getTeamColor() != piece.getTeamColor()){ //if piece not same team
                    //add tempposition to the moves_set
                    movesSet.add(new ChessMove(position, thetempposition, null));
                    break;
                }
                else if (board.getPiece(thetempposition).getTeamColor() == piece.getTeamColor()){
                    break;
                }
                else {throw new RuntimeException("Team Color equal method error");}
            }
            else{ // if no piece in the position of interest
                //add tempposition to the move array
                movesSet.add(new ChessMove(position, thetempposition, null));
            }
            row += rowincr;
            col += colincr;
        }
    }
}
