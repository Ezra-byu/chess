package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KingMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    private ChessPosition proposedposition;
    Set<ChessMove> movesSet = new HashSet<ChessMove>();
    public KingMovesCalculator(ChessPiece piece, ChessBoard board) {
        this.piece = piece;
        this.board = board;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        proposedposition = new ChessPosition(position.getRow(), position.getColumn()+1);
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }

        proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn());
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }

        proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn()+1);
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }

        proposedposition = new ChessPosition(position.getRow() , position.getColumn()-1);
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }

        proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn());
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }

        proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn()-1);
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }
        proposedposition = new ChessPosition(position.getRow()-1 , position.getColumn()+1);
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }
        proposedposition = new ChessPosition(position.getRow()+1 , position.getColumn()-1);
        if (isInBoundsKing(proposedposition) && !isBlocked(proposedposition)) {
            movesSet.add(new ChessMove(position, proposedposition, null));
        }
        return movesSet;
    }
    private Boolean isInBoundsKing(ChessPosition kingPosition){
        if ((kingPosition.getRow() >= 1) && (kingPosition.getRow() <= 8) && (kingPosition.getColumn() >= 1) && (kingPosition.getColumn() <= 8)){
            return Boolean.TRUE;
        }
        else {return Boolean.FALSE; }
    }
    private Boolean isBlocked(ChessPosition kingPosition){
        if ((board.getPiece(kingPosition) == null)){
            return Boolean.FALSE;
        }
        else if (board.getPiece(kingPosition).getTeamColor() != piece.getTeamColor()) {
            return Boolean.FALSE;
        }
        else {return Boolean.TRUE; }
    }
}
