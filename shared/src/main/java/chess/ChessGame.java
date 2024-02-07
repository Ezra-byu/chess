package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor game_team;
    private ChessBoard game_board;
    private ChessBoard temp_stored_board;
    private Collection<ChessMove> potential_moves = new HashSet<ChessMove>();
    private Collection<ChessPosition> king_danger_zone = new HashSet<ChessPosition>();
    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return game_team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        game_team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (game_board.getPiece(startPosition) == null){
            return null;
        }
        temp_stored_board = game_board;
        potential_moves = game_board.getPiece(startPosition).pieceMoves(game_board, startPosition);
        for (ChessMove move_element : potential_moves){
            tryMove(move_element);

        }

    }
    public void tryMove(ChessMove move){
        //replace the piece at the end position
        game_board.addPiece(move.getEndPosition(), game_board.getPiece(move.getStartPosition()));
        //set the start position to null (remove piece)
        game_board.removePiece(move.getStartPosition());
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
        //check if correct color
        //check if there is a piece in start position
        //check if move is ValidMove() put start postion
        //check if theres a piece there of opp. color and remove it
        //pawn -> check if promotion
        //update piece position in ChessBoard
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> kingDangerZone = king_danger_zone;
        kingDangerZone.clear();
        if (teamColor == TeamColor.WHITE) {
            for (ChessPosition pos_element : game_board.getBlackPosition()) {
                //for position in set of positions of white pieces on the board
                for (ChessMove move_element: game_board.getPiece(pos_element).pieceMoves(game_board, pos_element)){
                    //for all the moves of that white piece, add the end position to black king danger zone
                    kingDangerZone.add(move_element.getEndPosition());
                }
            }
            ChessPosition king_pos = game_board.getKingPosition(ChessPiece.PieceType.KING, TeamColor.WHITE);
            System.out.print("king position:" + king_pos);
            if (kingDangerZone.contains(king_pos)){
                System.out.print("white in check");
                return true;
            }
        }
        if (teamColor == TeamColor.BLACK) {
            for (ChessPosition pos_element : game_board.getWhitePosition()) {
                //for position in set of positions of white pieces on the board
                for (ChessMove move_element: game_board.getPiece(pos_element).pieceMoves(game_board, pos_element)){
                    //for all the moves of that white piece, add the end position to black king danger zone
                    kingDangerZone.add(move_element.getEndPosition());
                }
            }
            ChessPosition king_pos = game_board.getKingPosition(ChessPiece.PieceType.KING, TeamColor.BLACK);
            if (kingDangerZone.contains(king_pos)){
               System.out.print("black in check");
               return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //repitition of position same position 3 times
        //no valid moves
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        game_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
//        throw new RuntimeException("Not implemented");
        return game_board;
    }
}
