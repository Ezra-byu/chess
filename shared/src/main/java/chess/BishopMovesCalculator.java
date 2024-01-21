package chess;

import java.util.ArrayList;
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

    Set<ChessMove> moves_Set = new HashSet<ChessMove>();
    public BishopMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }
    @Override
    public void pieceMoves(ChessBoard board, ChessPosition position) {
        //left (row -1), up (col +1)
        diagonalChecker(1, 1, board, position);
        diagonalChecker(1, -1, board, position);
        diagonalChecker(-1, 1, board, position);
        diagonalChecker(-1, -1, board, position);
    }
    private void diagonalChecker(int rowincr, int colincr, ChessBoard board, ChessPosition position) {
        while(0 <= row && row <= 8 && 0 <= col && col <= 8){
            row = position.getRow() + rowincr;
            col = position.getColumn() + colincr;
            tempposition = new ChessPosition(row , col); //create the position being investigated
            if (board.getPiece(tempposition) != null) { //if there is a piece in the position being investigated
                if (board.getPiece(tempposition).getTeamColor() != piece.getTeamColor()){ //if piece not same team
                    //add tempposition to the moves_set
                    moves_Set.add(new ChessMove(position, tempposition, null));
                    break;
                }
                else if (board.getPiece(tempposition).getTeamColor() == piece.getTeamColor()){
                    break;
                }
                else {throw new RuntimeException("Team Color equal method error");}
            }
            else{ // if no piece in the position of interest
                //add tempposition to the move array
                moves_Set.add(new ChessMove(position, tempposition, null));
            }

            //break
            //elif piece does = curr color)
            //break
            //else(is empty)
            //add the move, continue

        }
    }
    //row right, row up
    //row right, row down
    //row left, row down
    //create a method that returns a collection of <ChessMove>. Chessmove is start<position>,end<positions>,promotion?
}
