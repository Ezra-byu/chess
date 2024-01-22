package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {//piecemovescalculatorable
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

}
