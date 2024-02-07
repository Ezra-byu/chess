package chess;

import chess.pieces.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor piececolor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.piececolor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.piececolor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == ChessPiece.PieceType.BISHOP) {
            BishopMovesCalculator bishopmoves = new BishopMovesCalculator(this, board);
            return bishopmoves.pieceMoves(board, myPosition);
        }
        else if (type == ChessPiece.PieceType.ROOK) {
            RookMovesCalculator rookmoves = new RookMovesCalculator(this, board);
            return rookmoves.pieceMoves(board, myPosition);
        }
        else if (type == PieceType.PAWN) {
            PawnMovesCalculator pawnmoves = new PawnMovesCalculator(this, board);
            return pawnmoves.pieceMoves(board, myPosition);
        }
        else if (type == PieceType.KNIGHT) {
            KnightMovesCalculator knightmoves = new KnightMovesCalculator(this, board);
            return knightmoves.pieceMoves(board, myPosition);
        }
        else if (type == PieceType.QUEEN) {
            QueenMovesCalculator queenmoves = new QueenMovesCalculator(this, board);
            return queenmoves.pieceMoves(board, myPosition);
        }
        else if (type == PieceType.KING) {
            KingMovesCalculator kingmoves = new KingMovesCalculator(this, board);
            return kingmoves.pieceMoves(board, myPosition);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "piececolor=" + piececolor +
                ", type=" + type +
                ", piece=" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return piececolor == that.piececolor && type == that.type ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piececolor, type);
    }
}
