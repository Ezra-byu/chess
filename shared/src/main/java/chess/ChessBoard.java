package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * Basically, an array that holds a little array with a chess piece and a position
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    private String output_str;
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //WHITE
        squares[0][0] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK );
        squares[0][1] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[0][5] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][6] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][7] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        squares[1][0] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
        squares[1][1] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
        squares[1][2] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
        squares[1][3] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
        squares[1][4] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
        squares[1][5] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
        squares[1][6] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
        squares[1][7] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );

//        BLACk
        squares[6][0] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
        squares[6][1] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
        squares[6][2] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
        squares[6][3] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
        squares[6][4] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
        squares[6][5] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
        squares[6][6] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
        squares[6][7] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );

        squares[7][0] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK );
        squares[7][1] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[7][5] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][6] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][7] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK );

//        squares[1][1] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK );
//        squares[1][2] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
//        squares[1][3] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
//        squares[1][4] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
//        squares[1][5] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
//        squares[1][6] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
//        squares[1][7] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
//        squares[1][8] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
//
//        squares[2][1] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//        squares[2][2] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//        squares[2][3] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//        squares[2][4] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//        squares[2][5] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//        squares[2][6] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//        squares[2][7] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//        squares[2][8] = new ChessPiece( ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN );
//
//        //BLACk
//        squares[7][0] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//        squares[7][1] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//        squares[7][2] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//        squares[7][3] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//        squares[6][4] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//        squares[6][5] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//        squares[6][6] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//        squares[6][7] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN );
//
//        squares[7][0] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK );
//        squares[7][1] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
//        squares[7][2] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
//        squares[7][3] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
//        squares[7][4] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
//        squares[7][5] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
//        squares[7][6] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
//        squares[7][7] = new ChessPiece( ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK );

    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                ", output_str='" + output_str + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares) && Objects.equals(output_str, that.output_str); //Arrays.equals
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(output_str);
        result = 31 * result + Arrays.deepHashCode(squares); // changed from Arrays.hashCode
        return result;
    }

}