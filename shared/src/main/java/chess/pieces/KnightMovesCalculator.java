package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KnightMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    private ChessPosition proposedposition;
    Set<ChessMove> moves_Set = new HashSet<ChessMove>();
    public KnightMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //upper left
        proposedposition = new ChessPosition(position.getRow()+2 , position.getColumn()-1);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
        }
        proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn()-2);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
        }
        //upper right
        proposedposition = new ChessPosition(position.getRow()+2 , position.getColumn()+1);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
        }
        proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn()+2);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
        }
        //lower right
        proposedposition = new ChessPosition(position.getRow()-2 , position.getColumn()+1);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
        }
        proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn()+2);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
        }
        //lower left
        proposedposition = new ChessPosition(position.getRow()-2 , position.getColumn()-1);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
        }
        proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn()-2);
        if (isInBounds(proposedposition) && !isBlocked(proposedposition)) {
            moves_Set.add(new ChessMove(position, proposedposition, null));
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
        if ((board.getPiece(position) == null)){
            return Boolean.FALSE;
        }
        else if (board.getPiece(position).getTeamColor() != piece.getTeamColor()) {
            return Boolean.FALSE;
        }
        else {return Boolean.TRUE; }
    }

}
