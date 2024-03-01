package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    private int row;
    private int col;
    private int rowincr;
    private int colincr;
    private ChessPosition tempposition;

    Set<ChessMove> movesSet = new HashSet<ChessMove>();
    public BishopMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //left (row -1), up (col +1)
        diagonalChecker(1, 1, board, position);
        diagonalChecker(1, -1, board, position);
        diagonalChecker(-1, 1, board, position);
        diagonalChecker(-1, -1, board, position);
        //System.out.println("out of loop");
        //moves_Set.add(new ChessMove(new ChessPosition(1 , 1), new ChessPosition(1 , 1), null));
        return movesSet;
    }
    private void diagonalChecker(int rowincr, int colincr, ChessBoard board, ChessPosition position) {
//        System.out.println("in loop");
        row = position.getRow() ;
        col = position.getColumn();
        row += rowincr;
        col += colincr;
        while((row >= 1) && (row <= 8) && (col >= 1) && (col <= 8)){
            tempposition = new ChessPosition(row , col); //create the position being investigated
            if (board.getPiece(tempposition) != null) { //if there is a piece in the position being investigated
                if (board.getPiece(tempposition).getTeamColor() != piece.getTeamColor()){ //if piece not same team
                    //add tempposition to the moves_set
                    movesSet.add(new ChessMove(position, tempposition, null));
                    break;
                }
                else if (board.getPiece(tempposition).getTeamColor() == piece.getTeamColor()){
                    break;
                }
                else {throw new RuntimeException("Team Color equal method error");}
            }
            else{ // if no piece in the position of interest
                //add tempposition to the move array
                movesSet.add(new ChessMove(position, tempposition, null));
            }
            row += rowincr;
            col += colincr;
        }

    }
    //row right, row up
    //row right, row down
    //row left, row down
    //create a method that returns a collection of <ChessMove>. Chessmove is start<position>,end<positions>,promotion?
}
