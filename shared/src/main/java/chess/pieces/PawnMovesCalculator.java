package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    private int row;
    private int col;
    private ChessPosition proposedposition;
    Set<ChessMove> movesSet = new HashSet<ChessMove>();

    public PawnMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //move forward 1
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE){//if piece white
            //try move +1
            proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn());
            if (isInBounds(proposedposition) && !isBlocked(proposedposition)){ //if it can move forward
                if (whitePromotion(proposedposition)) { //single move promotion
                    movesSet.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.QUEEN));
                    movesSet.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.BISHOP));
                    movesSet.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.ROOK));
                    movesSet.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.KNIGHT));
                }
                else{ //single move no promotion white
                    movesSet.add(new ChessMove(position, proposedposition, null));
                }

                // check for the double move white
                proposedposition = new ChessPosition(position.getRow()+2 , position.getColumn());
                if (isInBounds(proposedposition) && !isBlocked(proposedposition) && whiteDoubleStart(position)){
                    movesSet.add(new ChessMove(position, proposedposition, null));
                }
            }
            //try right diagonal
            ChessPosition proposedposition1 = new ChessPosition(position.getRow()+1 , position.getColumn()+1);
            if (isInBounds(proposedposition1) && !diagIsBlocked(proposedposition1)) {
                if (whitePromotion(proposedposition1)) { //single move promotion
                    movesSet.add(new ChessMove(position, proposedposition1, ChessPiece.PieceType.QUEEN));
                    movesSet.add(new ChessMove(position, proposedposition1, ChessPiece.PieceType.BISHOP));
                    movesSet.add(new ChessMove(position, proposedposition1, ChessPiece.PieceType.ROOK));
                    movesSet.add(new ChessMove(position, proposedposition1, ChessPiece.PieceType.KNIGHT));
                } else { //right diag move no promotion white
                    movesSet.add(new ChessMove(position, proposedposition1, null));
                }
            }

            //try right diagonal
            ChessPosition proposedposition2 = new ChessPosition(position.getRow()+1 , position.getColumn()-1);
            if (isInBounds(proposedposition2) && !diagIsBlocked(proposedposition2)) {
                if (whitePromotion(proposedposition2)) { //single move promotion
                    movesSet.add(new ChessMove(position, proposedposition2, ChessPiece.PieceType.QUEEN));
                    movesSet.add(new ChessMove(position, proposedposition2, ChessPiece.PieceType.BISHOP));
                    movesSet.add(new ChessMove(position, proposedposition2, ChessPiece.PieceType.ROOK));
                    movesSet.add(new ChessMove(position, proposedposition2, ChessPiece.PieceType.KNIGHT));
                } else { //left diag move no promotion white
                    movesSet.add(new ChessMove(position, proposedposition2, null));
                }
            }
        }
        else if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK){ // if piece Black
            //try move -1
            ChessPosition proposedposition3 = new ChessPosition(position.getRow()-1 , position.getColumn());
            if (isInBounds(proposedposition3) && !isBlocked(proposedposition3)){//if it can move forward
                if (blackPromotion(proposedposition3)) { //single move promotion
                    movesSet.add(new ChessMove(position, proposedposition3, ChessPiece.PieceType.QUEEN));
                    movesSet.add(new ChessMove(position, proposedposition3, ChessPiece.PieceType.BISHOP));
                    movesSet.add(new ChessMove(position, proposedposition3, ChessPiece.PieceType.ROOK));
                    movesSet.add(new ChessMove(position, proposedposition3, ChessPiece.PieceType.KNIGHT));
                }
                else{ //single move no promotion
                    movesSet.add(new ChessMove(position, proposedposition3, null));
                }
                // check for the double move
                proposedposition = new ChessPosition(position.getRow()-2 , position.getColumn());
                if (isInBounds(proposedposition) && !isBlocked(proposedposition) && blackDoubleStart(position)){
                    movesSet.add(new ChessMove(position, proposedposition, null));
                }
            }

            //try right diagonal black
            ChessPosition proposedposition4 = new ChessPosition(position.getRow()-1 , position.getColumn()+1);
            if (isInBounds(proposedposition4) && !diagIsBlocked(proposedposition4)) {
                if (blackPromotion(proposedposition4)) { //single move promotion
                    movesSet.add(new ChessMove(position, proposedposition4, ChessPiece.PieceType.QUEEN));
                    movesSet.add(new ChessMove(position, proposedposition4, ChessPiece.PieceType.BISHOP));
                    movesSet.add(new ChessMove(position, proposedposition4, ChessPiece.PieceType.ROOK));
                    movesSet.add(new ChessMove(position, proposedposition4, ChessPiece.PieceType.KNIGHT));
                }
                else { //right diag move no promotion black
                    movesSet.add(new ChessMove(position, proposedposition4, null));
                }
            }

            //try left diagonal black
            ChessPosition proposedposition5 = new ChessPosition(position.getRow()-1 , position.getColumn()-1);
            //System.out.print(diagIsBlocked(proposedposition));
            if (isInBounds(proposedposition5) && !diagIsBlocked(proposedposition5)) {
                if (blackPromotion(proposedposition5)) {
                    movesSet.add(new ChessMove(position, proposedposition5, ChessPiece.PieceType.QUEEN));
                    movesSet.add(new ChessMove(position, proposedposition5, ChessPiece.PieceType.BISHOP));
                    movesSet.add(new ChessMove(position, proposedposition5, ChessPiece.PieceType.ROOK));
                    movesSet.add(new ChessMove(position, proposedposition5, ChessPiece.PieceType.KNIGHT));
                }
                else { //left diag move no promotion black
                    movesSet.add(new ChessMove(position, proposedposition5, null));
                }
            }

        }
        return movesSet;
    }

    private Boolean isInBounds(ChessPosition positionToCheck){
        if ((positionToCheck.getRow() >= 1) && (positionToCheck.getRow() <= 8) && (positionToCheck.getColumn() >= 1) && (positionToCheck.getColumn() <= 8)){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }

    private Boolean isBlocked(ChessPosition pawnPosition){
        if (board.getPiece(pawnPosition) != null){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }

    private Boolean diagIsBlocked(ChessPosition position){
        if ((board.getPiece(position) != null) && (board.getPiece(position).getTeamColor() != piece.getTeamColor())){
            return Boolean.FALSE;
        }
        else {return Boolean.TRUE; }
    }

    private Boolean whitePromotion(ChessPosition position){
        if (position.getRow() == 8){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }

    private Boolean blackPromotion(ChessPosition position){
        if (position.getRow() == 1){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }

    private Boolean whiteDoubleStart(ChessPosition position){
        if (position.getRow() == 2){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }

    private Boolean blackDoubleStart(ChessPosition position){
        if (position.getRow() == 7){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }
}
