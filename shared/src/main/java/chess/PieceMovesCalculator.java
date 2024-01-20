package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    public void pieceMoves(ChessBoard board, ChessPosition position);
}
