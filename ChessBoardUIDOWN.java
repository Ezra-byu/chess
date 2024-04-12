

package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoardUIDOWN {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 0;
    private static final String EMPTY = "   ";


    public static void main() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawTicTacToeBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setDarkGray(out);

        String[] headers = { " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a " };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void drawSider(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setDarkGray(out);
    }

    private static void printSiderText(PrintStream out, String siderText, int squareRow) {
        out.print(SET_TEXT_COLOR_GREEN);
        if(squareRow == 1){out.print(siderText);}
        else{out.print(EMPTY);}
    }

    private static void drawTicTacToeBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                drawVerticalLine(out);
                setDarkGray(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                // String[] siders = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
                String[] siders = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
                if (boardCol == 0){printSiderText( out, siders[boardRow],  squareRow);}

                String background = "black";
                setDarkGray(out);
                if (( (boardRow & 1) == 0 ) && ( (boardCol & 1) == 0 )) {
                    setLightGrey(out);
                    background = "white";
                }
                if (( (boardRow & 1) == 1 ) && ( (boardCol & 1) == 1 )) {
                    setLightGrey(out);
                    background = "white";
                }

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));

                    printStandardPlayer(out, boardRow, boardCol, background);
                    //printPlayer(out, rand.nextBoolean() ? WHITE_BISHOP : O);
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw right line
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }

                setDarkGray(out);
            }

            out.println();


        }
    }

    private static void drawVerticalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setDarkGray(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setDarkGray(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);

        setLightGrey(out);
    }
    private static void printStandardPlayer(PrintStream out, int boardRow, int boardCol, String background) {
        String piece = "";
        String pieceColor = "black";
//        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; ++i){
//            for (int j = 0; j < BOARD_SIZE_IN_SQUARES; ++j)
//                if(boardRow == i && boardCol == j){
//                    piece = WHITE_ROOK;
//                    pieceColor =
//                }
        if(boardRow == 7 && boardCol == 0) {
            piece = BLACK_ROOK;
            pieceColor = "black";
        }
        else if(boardRow == 7 && boardCol == 1) {
            piece = BLACK_KNIGHT;
            pieceColor = "black";
        }
        else if(boardRow == 7 && boardCol == 2) {
            piece = BLACK_BISHOP;
            pieceColor = "black";
        }
        else if(boardRow == 7 && boardCol == 4) {
            piece = BLACK_QUEEN;
            pieceColor = "black";
        }
        else if(boardRow == 7 && boardCol == 3) {
            piece = BLACK_KING;
            pieceColor = "black";
        }
        else if(boardRow == 7 && boardCol == 5) {
            piece = BLACK_BISHOP;
            pieceColor = "black";
        }
        else if(boardRow == 7 && boardCol == 6) {
            piece = BLACK_KNIGHT;
            pieceColor = "black";
        }
        else if(boardRow == 7 && boardCol == 7) {
            piece = BLACK_ROOK;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 0) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 1) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 2) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 3) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 4) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 5) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 6) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 6 && boardCol == 7) {
            piece = BLACK_PAWN;
            pieceColor = "black";
        }
        else if(boardRow == 1 && boardCol == 0) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 1 && boardCol == 1) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 1 && boardCol == 2) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 1 && boardCol == 3) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 1 && boardCol == 4) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 1 && boardCol == 5) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 1 && boardCol == 6) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 1 && boardCol == 7) {
            piece = WHITE_PAWN;
            pieceColor = "white";
        }
        else if(boardRow == 0 && boardCol == 0) {
            piece = WHITE_ROOK;
            pieceColor = "black";
        }
        else if(boardRow == 0 && boardCol == 1) {
            piece = WHITE_KNIGHT;
            pieceColor = "black";
        }
        else if(boardRow == 0 && boardCol == 2) {
            piece = WHITE_BISHOP;
            pieceColor = "black";
        }
        else if(boardRow == 0 && boardCol == 4) {
            piece = WHITE_QUEEN;
            pieceColor = "black";
        }
        else if(boardRow == 0 && boardCol == 3) {
            piece = WHITE_KING;
            pieceColor = "black";
        }
        else if(boardRow == 0 && boardCol == 5) {
            piece = WHITE_BISHOP;
            pieceColor = "black";
        }
        else if(boardRow == 0 && boardCol == 6) {
            piece = WHITE_KNIGHT;
            pieceColor = "black";
        }
        else if(boardRow == 0 && boardCol == 7) {
            piece = WHITE_ROOK;
            pieceColor = "black";
        }
        else{piece = EMPTY;}

        if (Objects.equals(background, "white")){
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(piece);
            setLightGrey(out);
        }else{
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(piece);
            setDarkGray(out);
        }

    }
}

