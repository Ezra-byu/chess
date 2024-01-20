package chess;

import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    ChessMove moveArray[] = {};
    public BishopMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }
    @Override
    public void pieceMoves(ChessBoard board, ChessPosition position) {
        //row left, row up
        //row right, row up
        //row right, row down
        //row left, row down
    }

    //create a method that returns a collection of <ChessMove>. Chessmove is start<position>,end<positions>,promotion?
}
