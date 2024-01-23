package chess;

import chess.pieces.BishopMovesCalculator;
import chess.pieces.KnightMovesCalculator;
import chess.pieces.PawnMovesCalculator;
import chess.pieces.RookMovesCalculator;

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
    private ChessPiece piece;
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
        piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            BishopMovesCalculator bishopmoves = new BishopMovesCalculator(piece, board);
            return bishopmoves.pieceMoves(board, myPosition);
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            RookMovesCalculator rookmoves = new RookMovesCalculator(piece, board);
            return rookmoves.pieceMoves(board, myPosition);
        }
        else if (piece.getPieceType() == PieceType.PAWN) {
            PawnMovesCalculator pawnmoves = new PawnMovesCalculator(piece, board);
            return pawnmoves.pieceMoves(board, myPosition);
        }
        else if (piece.getPieceType() == PieceType.KNIGHT) {
            KnightMovesCalculator knightmoves = new KnightMovesCalculator(piece, board);
            return knightmoves.pieceMoves(board, myPosition);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "piececolor=" + piececolor +
                ", type=" + type +
                ", piece=" + piece +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return piececolor == that.piececolor && type == that.type && Objects.equals(piece, that.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piececolor, type, piece);
    }
}
