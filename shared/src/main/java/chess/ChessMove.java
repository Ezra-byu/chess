package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startposition;
    private ChessPosition endposition;
    private ChessPiece.PieceType promotionpiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        startposition = startPosition;
        endposition = endPosition;
        promotionpiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startposition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endposition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionpiece;
    }

    @Override
    public String toString() {
        return "(" +
                "start" + startposition +
                ", end" + endposition +
                " " + promotionpiece +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startposition, chessMove.startposition) && Objects.equals(endposition, chessMove.endposition) && promotionpiece == chessMove.promotionpiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startposition, endposition, promotionpiece);
    }
}
