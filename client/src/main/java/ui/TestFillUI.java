package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class TestFillUI {
        private static final int BOARD_SIZE_IN_SQUARES = 8;
        private static final int SQUARE_SIZE_IN_CHARS = 3;
        private static final int LINE_WIDTH_IN_CHARS = 0;
        private static final String EMPTY = "   ";
        private static ChessBoard testboard;
        private static String orientation;

        public static void fillUI(ChessBoard myBoard) {
            testboard = myBoard;
            
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            TestFillUI testFillUI = new TestFillUI();

            out.print(ERASE_SCREEN);

            testFillUI.drawHeaders(out);

            testFillUI.drawTicTacToeBoard(out);

            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }

        private static void drawHeaders(PrintStream out) {

            setDarkGray(out);
            //if(orientation == down) { " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a " }
            String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
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

            for (int boardRow = 7; boardRow >= 0; boardRow--) {//reverse this

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
                    //if(orientation == down){
                    String[] siders = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
                    //else{ String[] siders = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " }; }
                    if (boardCol == 0){
                        printSiderText( out, siders[boardRow],  squareRow);
                    }

                    String background = "white";
                    setLightGrey(out);
                    if (( (boardRow & 1) == 0 ) && ( (boardCol & 1) == 0 )) {
                        setDarkGray(out);
                        background = "black";
                    }
                    if (( (boardRow & 1) == 1 ) && ( (boardCol & 1) == 1 )) {
                        setDarkGray(out);
                        background = "black";
                    }

                    if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                        out.print(EMPTY.repeat(prefixLength));

                        printStandardPlayer(out, boardRow, boardCol, background);
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
//        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; ++i){ for (int j = 0; j < BOARD_SIZE_IN_SQUARES; ++j) if(boardRow == i && boardCol == j){ piece = WHITE_ROOK; pieceColor =}
            //This file runs on 0-7 index. ChessBoard expects 1-8
            ChessPosition piecePosition = new ChessPosition(boardRow+1, boardCol+1);
            ChessPiece pieceToPlace = testboard.getPiece(piecePosition);
            //piece = " " + boardRow + boardCol;
            if (pieceToPlace == null) {
                piece = EMPTY;
            } else {
                if (pieceToPlace.getTeamColor() == ChessGame.TeamColor.WHITE){
                    if (pieceToPlace.getPieceType() == KING) {
                        piece = WHITE_KING;
                    } else if (pieceToPlace.getPieceType() == QUEEN) {
                        piece = WHITE_QUEEN;
                    } else if (pieceToPlace.getPieceType() == BISHOP) {
                        piece = WHITE_BISHOP;
                    } else if (pieceToPlace.getPieceType() == KNIGHT) {
                        piece = WHITE_KNIGHT;
                    } else if (pieceToPlace.getPieceType() == ROOK) {
                        piece = WHITE_ROOK;
                    } else if (pieceToPlace.getPieceType() == PAWN) {
                        piece = WHITE_PAWN;
                    } else { //should never happen
                        piece = EMPTY;
                    }
                } else if (pieceToPlace.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (pieceToPlace.getPieceType() == KING) {
                        piece = BLACK_KING;
                    } else if (pieceToPlace.getPieceType() == QUEEN) {
                        piece = BLACK_QUEEN;
                    } else if (pieceToPlace.getPieceType() == BISHOP) {
                        piece = BLACK_BISHOP;
                    } else if (pieceToPlace.getPieceType() == KNIGHT) {
                        piece = BLACK_KNIGHT;
                    } else if (pieceToPlace.getPieceType() == ROOK) {
                        piece = BLACK_ROOK;
                    } else if (pieceToPlace.getPieceType() == PAWN) {
                        piece = BLACK_PAWN;
                    } else { //should never happen
                        piece = EMPTY;
                    }
                }
            }

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


