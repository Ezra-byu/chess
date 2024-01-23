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
    Set<ChessMove> moves_Set = new HashSet<ChessMove>();

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
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.QUEEN));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.BISHOP));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.ROOK));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.KNIGHT));
                }
                else{ //single move no promotion white
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }

                // check for the double move white
                proposedposition = new ChessPosition(position.getRow()+2 , position.getColumn());
                if (isInBounds(proposedposition) && !isBlocked(proposedposition) && whiteDoubleStart(position)){
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }
            }
            //try right diagonal
            proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn()+1);
            if (isInBounds(proposedposition) && !diagIsBlocked(proposedposition)) {
                if (whitePromotion(proposedposition)) { //single move promotion
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.QUEEN));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.BISHOP));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.ROOK));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.KNIGHT));
                } else { //right diag move no promotion white
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }
            }

            //try right diagonal
            proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn()-1);
            if (isInBounds(proposedposition) && !diagIsBlocked(proposedposition)) {
                if (whitePromotion(proposedposition)) { //single move promotion
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.QUEEN));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.BISHOP));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.ROOK));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.KNIGHT));
                } else { //left diag move no promotion white
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }
            }
        }
        else if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK){ // if piece Black
            //try move -1
            proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn());
            if (isInBounds(proposedposition) && !isBlocked(proposedposition)){//if it can move forward
                if (blackPromotion(proposedposition)) { //single move promotion
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.QUEEN));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.BISHOP));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.ROOK));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.KNIGHT));
                }
                else{ //single move no promotion
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }
                // check for the double move
                proposedposition = new ChessPosition(position.getRow()-2 , position.getColumn());
                if (isInBounds(proposedposition) && !isBlocked(proposedposition) && blackDoubleStart(position)){
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }
            }

            //try right diagonal black
            proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn()+1);
            if (isInBounds(proposedposition) && !diagIsBlocked(proposedposition)) {
                if (whitePromotion(proposedposition)) { //single move promotion
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.QUEEN));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.BISHOP));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.ROOK));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.KNIGHT));
                } else { //right diag move no promotion black
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }
            }

            //try left diagonal black
            proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn()-1);
            System.out.print(diagIsBlocked(proposedposition));
            if (isInBounds(proposedposition) && !diagIsBlocked(proposedposition)) {
                if (blackPromotion(proposedposition)) {
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.QUEEN));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.BISHOP));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.ROOK));
                    moves_Set.add(new ChessMove(position, proposedposition, ChessPiece.PieceType.KNIGHT));
                } else { //left diag move no promotion black
                    moves_Set.add(new ChessMove(position, proposedposition, null));
                }
            }

        }
        return moves_Set;
    }

    private Boolean isInBounds(ChessPosition position){
        if ((position.getRow() >= 1) && (position.getRow() <= 8) && (position.getColumn() >= 1) && (position.getColumn() <= 8)){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }

    private Boolean isBlocked(ChessPosition position){
        if (board.getPiece(position) != null){
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
