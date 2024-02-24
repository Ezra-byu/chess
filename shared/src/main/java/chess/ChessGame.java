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
    private ChessPiece removed_piece;
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
        ChessPiece original_piece = game_board.getPiece(startPosition);
        if (game_board.getPiece(startPosition) == null){
            return null;
        }
        potential_moves = original_piece.pieceMoves(game_board, startPosition);
        for (ChessMove move_element : original_piece.pieceMoves(game_board, startPosition)){
            tryMove(move_element);
            if (isInCheck(original_piece.getTeamColor())){ // game_team can be used because makeMoves ensures the right color is making move
                potential_moves.remove(move_element);
            }
            undoMove(move_element);
            //set game_board back to before next tryMove
        }
        return potential_moves;
    }


    public void tryMove(ChessMove move){
        //replace the piece at the end position
        removed_piece = game_board.getPiece(move.getEndPosition());
        game_board.addPiece(move.getEndPosition(), game_board.getPiece(move.getStartPosition()));
        //set the start position to null (remove piece)
        game_board.removePiece(move.getStartPosition());
    }

    public void undoMove(ChessMove move){
        //move the piece at the move's end position to the move's start position
        game_board.addPiece(move.getStartPosition(), game_board.getPiece(move.getEndPosition()));
        game_board.addPiece(move.getEndPosition(), removed_piece);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //check if correct color
        if(game_board.getPiece(move.getStartPosition()).getTeamColor() != game_team){
            System.out.print("make move piece team color" + game_board.getPiece(move.getStartPosition()).getTeamColor());
            throw new InvalidMoveException();
        }
        //check if there is a piece in start position
        //check if move is ValidMove() put start postion
        Collection<ChessMove> local_valid_moves= validMoves(move.getStartPosition());
        if (!local_valid_moves.contains(move)){
            throw new InvalidMoveException();
        }

        //check if there is a piece there of opp. color and remove it
        //pawn -> check if promotion
        //update piece position in ChessBoard

        if(game_board.getPiece(move.getStartPosition()).getTeamColor() != game_team){
            throw new InvalidMoveException();
        }
        //replace the piece at the end position
        if (move.getPromotionPiece() != null){
            game_board.addPiece(move.getEndPosition(), new ChessPiece(game_board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
        }
        else {game_board.addPiece(move.getEndPosition(), game_board.getPiece(move.getStartPosition()));}
        //set the start position to null (remove piece)
        game_board.removePiece(move.getStartPosition());

        game_team = (game_team == TeamColor.BLACK)? TeamColor.WHITE: TeamColor.BLACK;
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
//            System.out.print("king position:" + king_pos);
            if (kingDangerZone.contains(king_pos)){
//                System.out.print("white in check");
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
//               System.out.print("black in check");
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
        ///if valid moves is empty for all teamColor Postitions (stalemate) and (is in check)
        if (isInCheck(teamColor) && isInStalemate(teamColor)){return true;}
        else {return false;}
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //repitition of position same position 3 times
        //no valid moves
        if (teamColor != game_team){ return false;}
        if (teamColor == TeamColor.WHITE ) {
            for (ChessPosition pos_element : game_board.getWhitePosition()) {
                if (!validMoves(pos_element).isEmpty()){
                    return false;
                }
            }
        }
        if (teamColor == TeamColor.BLACK) {
            for (ChessPosition pos_element : game_board.getBlackPosition()) {
                if (!validMoves(pos_element).isEmpty()){
                    return false;
                }
            }
        }
        return true;
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
