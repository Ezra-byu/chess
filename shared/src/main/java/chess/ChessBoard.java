package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * Basically, an array that holds a little array with a chess piece and a position
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    private String outputStr;
    private Set<ChessPiece> pieceByTeamTypeSet = new HashSet<ChessPiece>();
//    Set<ChessPiece> white_pieces_set = new HashSet<ChessPiece>();
//    Set<ChessPiece> black_pieces_set = new HashSet<ChessPiece>();
    Set<ChessMove> whiteMoveSet = new HashSet<ChessMove>();
    private Set<ChessPosition> blackPositionSet = new HashSet<ChessPosition>();
    private Set<ChessPosition> whitePositionSet = new HashSet<ChessPosition>();
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
    public void removePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
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

    public ChessPosition getKingPosition(ChessPiece.PieceType type, ChessGame.TeamColor team) {
        // make piece_by_team_type_set empty
        for(int i=0; i<squares.length; i++) {
            for(int j=0; j<squares[i].length; j++) {
                //the piece at squares i j type == type and team == team, add to set
                if((squares[i][j] != null) && (squares[i][j].getPieceType() == type) && (squares[i][j].getTeamColor() == team)){
                    //System.out.println("Get kingPosition piece: "+squares[i][j]);
                    //System.out.println("Get kingPosition piece: "+(i+1) +" "+(j+1));
                    return new ChessPosition((i+1) , (j+1)); //remember, positions are 1-8
                }
            }
        }
        return null;
    }


    public Collection<ChessPosition> getWhitePosition() {
        whitePositionSet.clear();
        for(int i=0; i<squares.length; i++) {
            for(int j=0; j<squares[i].length; j++) {
                //the piece at squares i j type == type and team == team, add to set
                if((squares[i][j] != null) && (squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE)){
                    //System.out.println("GetwhitePosition piece: "+squares[i][j]);
                    //System.out.println("GetwhitePosition: "+(i+1) +" "+(j+1));
                    whitePositionSet.add(new ChessPosition((i+1),(j+1))); //remember, positions are 1-8
                }
            }
        }
        return whitePositionSet;
    }

    public Collection<ChessPosition> getBlackPosition() {
        blackPositionSet.clear();
        for(int i=0; i<squares.length; i++) {
            for(int j=0; j<squares[i].length; j++) {
                //the piece at squares i j type == type and team == team, add to set
                if((squares[i][j] != null) && (squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK)){
//                    System.out.println("GetblackPosition piece: "+squares[i][j]);
//                    System.out.println("GetblackPosition: "+(i+1) +" "+(j+1));
                    blackPositionSet.add(new ChessPosition((i+1),(j+1))); //remember, positions are 1-8
                }
            }
        }
        return blackPositionSet;
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

    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                ", outputStr='" + outputStr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares) && Objects.equals(outputStr, that.outputStr); //Arrays.equals
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(outputStr);
        result = 31 * result + Arrays.deepHashCode(squares); // changed from Arrays.hashCode
        return result;
    }

}
