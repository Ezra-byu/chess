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
    private TeamColor gameTeam;
    private ChessBoard gameBoard;
    private ChessBoard tempStoredBoard;
    private ChessPiece removedPiece;
    private Collection<ChessMove> potentialMoves = new HashSet<ChessMove>();
    private Collection<ChessPosition> kingDangerZone = new HashSet<ChessPosition>();
    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return gameTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        gameTeam = team;
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
        ChessPiece originalPiece = gameBoard.getPiece(startPosition);
        if (gameBoard.getPiece(startPosition) == null){
            return null;
        }
        potentialMoves = originalPiece.pieceMoves(gameBoard, startPosition);
        for (ChessMove moveElement : originalPiece.pieceMoves(gameBoard, startPosition)){
            tryMove(moveElement);
            if (isInCheck(originalPiece.getTeamColor())){ // game_team can be used because makeMoves ensures the right color is making move
                potentialMoves.remove(moveElement);
            }
            undoMove(moveElement);
            //set game_board back to before next tryMove
        }
        return potentialMoves;
    }


    public void tryMove(ChessMove move){
        //replace the piece at the end position
        removedPiece = gameBoard.getPiece(move.getEndPosition());
        gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
        //set the start position to null (remove piece)
        gameBoard.removePiece(move.getStartPosition());
    }

    public void undoMove(ChessMove move){
        //move the piece at the move's end position to the move's start position
        gameBoard.addPiece(move.getStartPosition(), gameBoard.getPiece(move.getEndPosition()));
        gameBoard.addPiece(move.getEndPosition(), removedPiece);
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var teamcoolor = gameBoard.getPiece(move.getStartPosition()).getTeamColor();
        //check if correct color
        if(gameBoard.getPiece(move.getStartPosition()).getTeamColor() != gameTeam){
            System.out.print("make move piece team color" + gameBoard.getPiece(move.getStartPosition()).getTeamColor());
            throw new InvalidMoveException();
        }
        //check if there is a piece in start position
        //check if move is ValidMove() put start postion
        Collection<ChessMove> localValidMoves= validMoves(move.getStartPosition());
        if (!localValidMoves.contains(move)){
            throw new InvalidMoveException();
        }

        //check if there is a piece there of opp. color and remove it
        //pawn -> check if promotion
        //update piece position in ChessBoard

        if(gameBoard.getPiece(move.getStartPosition()).getTeamColor() != gameTeam){
            throw new InvalidMoveException();
        }
        //replace the piece at the end position
        if (move.getPromotionPiece() != null){
            gameBoard.addPiece(move.getEndPosition(), new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
        }
        else {
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));}
        //set the start position to null (remove piece)
        gameBoard.removePiece(move.getStartPosition());

        gameTeam = (gameTeam == TeamColor.BLACK)? TeamColor.WHITE: TeamColor.BLACK;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> kingDangerZone = this.kingDangerZone;
        kingDangerZone.clear();
        if (teamColor == TeamColor.WHITE) {
            for (ChessPosition posElement : gameBoard.getBlackPosition()) {
                //for position in set of positions of white pieces on the board
                for (ChessMove moveElement: gameBoard.getPiece(posElement).pieceMoves(gameBoard, posElement)){
                    //for all the moves of that white piece, add the end position to black king danger zone
                    kingDangerZone.add(moveElement.getEndPosition());
                }
            }
            ChessPosition kingPos = gameBoard.getKingPosition(ChessPiece.PieceType.KING, TeamColor.WHITE);
//            System.out.print("king position:" + kingPos);
            if (kingDangerZone.contains(kingPos)){
//                System.out.print("white in check");
                return true;
            }
        }
        if (teamColor == TeamColor.BLACK) {
            for (ChessPosition posElement : gameBoard.getWhitePosition()) {
                //for position in set of positions of white pieces on the board
                for (ChessMove moveElement: gameBoard.getPiece(posElement).pieceMoves(gameBoard, posElement)){
                    //for all the moves of that white piece, add the end position to black king danger zone
                    kingDangerZone.add(moveElement.getEndPosition());
                }
            }
            ChessPosition kingPos = gameBoard.getKingPosition(ChessPiece.PieceType.KING, TeamColor.BLACK);
            if (kingDangerZone.contains(kingPos)){
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
        if (teamColor != gameTeam){ return false;}
        if (teamColor == TeamColor.WHITE ) {
            for (ChessPosition posElement : gameBoard.getWhitePosition()) {
                if (!validMoves(posElement).isEmpty()){
                    return false;
                }
            }
        }
        if (teamColor == TeamColor.BLACK) {
            for (ChessPosition posElement : gameBoard.getBlackPosition()) {
                if (!validMoves(posElement).isEmpty()){
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
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
