package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RookMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    private int row;
    private int col;
    private ChessPosition tempposition;
    Set<ChessMove> movesSet = new HashSet<ChessMove>();
    public RookMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        orthogonalChecker(0, 1, board, position);
        orthogonalChecker(0, -1, board, position);
        orthogonalChecker(1, 0, board, position);
        orthogonalChecker(-1, 0, board, position);
        return movesSet;
    }
    private void orthogonalChecker(int rowincr, int colincr, ChessBoard board, ChessPosition position) {
//        System.out.println("in loop");
        row = position.getRow() ;
        col = position.getColumn();
        row += rowincr;
        col += colincr;

        while((row >= 1) && (row <= 8) && (col >= 1) && (col <= 8)){
            tempposition = new ChessPosition(row , col); //create the position being investigated
            if (board.getPiece(tempposition) != null) { //if there is a piece in the position being investigated
                if (board.getPiece(tempposition).getTeamColor() != piece.getTeamColor()){ //if piece not same team
                    //add tempposition to the movesSet
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
            row += rowincr; //increment row
            col += colincr; //incrament col
        }
    }
}
