package main.java;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the state of the board of a game of Entropy.
 *
 * "Entropy is a board game by Augustine Carreno published in 1994.
 * It is played on a square board divided into 5×5 cells, with seven
 * black and seven white pieces set up as in the Korean board game
 * Five Field Kono. The object is to be first to go from the initial
 * position, in which all the player's pieces can move, to a position
 * in which none can. A piece is able to move only when it is in contact,
 * orthogonally or diagonally, with at least one other piece of the same type."
 *
 * https://en.wikipedia.org/wiki/Entropy_(1994_board_game)
 */

public class Board {

    private final boolean CHECK_REP_ON = true;

    private final int BOARD_SIZE = 5;

    // Abstraction Function:
    // The 2-D array, entBoard, is a NxN array of Pieces that represents where
    // the pieces are arranged on the board.

    // Representation Invariant:
    // entBoard contains 7 BLACK pieces &&
    // entBoard contains 7 WHITE pieces &&
    // entBoard contains 11 null (or empty spaces)

    private Piece[][] entBoard;

    /**
     * Creates a new Board object with all of the Pieces in the initial position.
     *
     * @spec.modifies this
     * @spec.effects constructs a new board object
     */
    public Board() {
        entBoard = new Piece[BOARD_SIZE][BOARD_SIZE];

        // Setup
        for (int i = 0; i < BOARD_SIZE; i++) { // Top row
            entBoard[0][i] = new Piece(0, i, Color.BLACK);
        }

        entBoard[1][0] = new Piece(1, 0, Color.BLACK); // Second row from top
        entBoard[1][4] = new Piece(1, 4, Color.BLACK);

        entBoard[3][0] = new Piece(3, 0, Color.WHITE); // Second row from bottom
        entBoard[3][4] = new Piece(3, 4, Color.WHITE);

        for (int i = 0; i < BOARD_SIZE; i++) { // Bottom row
            entBoard[4][i] = new Piece(4, i, Color.WHITE);
        }

        checkRep();
    }

    /**
     * Returns the piece at a given square.
     *
     * @param x x-coordinate of square
     * @param y y-coordinate of square
     * @throws IllegalArgumentException if the coordinate point is not on the board
     * @return the color of the piece*/
    public Piece getPieceAtSquare(int x, int y) throws IllegalArgumentException {
        checkRep();
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            throw new IllegalArgumentException("Coordinate out of bounds");
        }
        checkRep();
        return entBoard[x][y];
    }

    /**
     * Moves a piece to a valid location.
     *
     * @param startX the starting row of the piece
     * @param startY the starting column of the piece
     * @param endX the ending row of the piece
     * @param endY the ending column of the piece
     * @throws IllegalArgumentException if there is no piece at that the starting position
     * @throws IllegalArgumentException if the piece is not movable
     * @throws IllegalArgumentException if the ending position is illegal
     * @spec.modifies this
     * @spec.effects changes the position of one piece on the board
     */
    public void movePiece(int startX, int startY, int endX, int endY) throws IllegalArgumentException {
        checkRep();
        if (entBoard[startX][startY] == null) { // No piece at starting position
            throw new IllegalArgumentException("No piece in selected position");
        } else if (!isMovable(startX, startY)) {
            // Piece is not adjacent to any other piece of its color (not allowed to move)
            throw new IllegalArgumentException("Piece is not movable");
        }
        // Illegal end positions
        if (endX < 0 || endX >= BOARD_SIZE || endY < 0 || endY >= BOARD_SIZE) { // Off the board
            throw new IllegalArgumentException("Not a square on the board");
        }
        Point move = new Point(endX, endY);
        if (!legalMoves(startX, startY).contains(move)) { // Not a legal move for the piece
            throw new IllegalArgumentException("Not a legal move for the piece");
        }

        // Move piece
        entBoard[endX][endY] = entBoard[startX][startY];
        entBoard[startX][startY] = null;
        checkRep();
    }

    /**
     * Determines whether a player is in check or not.
     *
     * @param player the player we are checking
     * @return a list of pieces that are in check
     */
    public List<Point> inCheck(Color player) {
        checkRep();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (entBoard[i][j] != null
                        && entBoard[i][j].getColor() == player
                        && !getSurroundingColors(i, j).contains(player)) {
                    points.add(new Point(i, j));
                }
            }
        }
        checkRep();
        return points;
    }

    /**
     * Calculates all of the legal moves for a given piece not considering checks.
     *
     * @param pieceX the x coordinate of the piece
     * @param pieceY the y coordinate of the piece
     * @throws IllegalArgumentException if there is no piece at the position
     * @return a list of coordinates as Point objects
     */
    public List<Point> legalMoves(int pieceX, int pieceY) throws IllegalArgumentException {
        checkRep();
        if (entBoard[pieceX][pieceY] == null) { // No piece at starting position
            throw new IllegalArgumentException("No piece in selected position");
        }
        List<Point> listCoordinates = new ArrayList<>();
        int steps = 1;
        while (pieceX - steps >= 0 && entBoard[pieceX - steps][pieceY] == null) {
            // Legal move above the piece (haven't hit top or another piece)
            Point point = new Point(pieceX - steps, pieceY);
            listCoordinates.add(point);
            steps++;
        }
        steps = 1;
        while (pieceX + steps <= BOARD_SIZE - 1 && entBoard[pieceX + steps][pieceY] == null) {
            // Legal move below the piece (haven't hit bottom or another piece)
            Point point = new Point(pieceX + steps, pieceY);
            listCoordinates.add(point);
            steps++;
        }
        steps = 1;
        while (pieceY - steps >= 0 && entBoard[pieceX][pieceY - steps] == null) {
            // Legal move left of the piece (haven't hit leftmost or another piece)
            Point point = new Point(pieceX, pieceY - steps);
            listCoordinates.add(point);
            steps++;
        }
        steps = 1;
        while (pieceY + steps <= BOARD_SIZE - 1 && entBoard[pieceX][pieceY + steps] == null) {
            // Legal move right of the piece (haven't hit rightmost or another piece)
            Point point = new Point(pieceX, pieceY + steps);
            listCoordinates.add(point);
            steps++;
        }
        checkRep();
        return listCoordinates;
    }

    /**
     * Returns whether the piece at the position is movable or not.
     *
     * @param pieceX piece x position
     * @param pieceY piece y position
     * @return true if movable, false if not movable
     */
    public boolean isMovable(int pieceX, int pieceY) {
        checkRep();
        Piece piece = entBoard[pieceX][pieceY];
        if (piece != null && getSurroundingColors(pieceX, pieceY).contains(piece.getColor())) {
            return true;
        }
        checkRep();

        return false;
    }

    /**
     * Determines if a player must pass their turn.
     */
    public boolean mustPass(Color player) {
        // Getting all the pieces in check, if there are pieces in check
        List<Point> pointsOfCheck = inCheck(player);

        if (pointsOfCheck.isEmpty()) {
            return false;
        }

        // If there are pieces in check, get the surrounding squares as valid end positions
        List<Point> validEndPositions = new ArrayList<>();
        if (!pointsOfCheck.isEmpty()) {
            for (Point point : pointsOfCheck) {
                validEndPositions.addAll(getSurroundingPoints(point.x, point.y));
            }
        }

        // List of all legal moves disregarding checks
        List<Point> allLegalMoves = new ArrayList<>();
        for (Piece[] row : entBoard) {
            for (Piece piece : row) {
                if (piece != null
                        && piece.getColor() == player
                        && isMovable(piece.getX(), piece.getY())) {
                    allLegalMoves.addAll(legalMoves(piece.getX(), piece.getY()));
                }
            }
        }

        // Check if there are any valid moves
        for (Point point : validEndPositions) {
            if (allLegalMoves.contains(point)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to if the game is in a win state for either player.
     *
     * @return the color of the winning player, null if no one has won
     */
    public Color checkWin() {
        checkRep();
        boolean blackWin = true;
        boolean whiteWin = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (Color surrounding : getSurroundingColors(i, j)) {
                    if (entBoard[i][j] != null) {
                        if (entBoard[i][j].getColor() == Color.BLACK
                                && surrounding == Color.BLACK) {
                            blackWin = false;
                        } else if (entBoard[i][j].getColor() == Color.WHITE
                                && surrounding == Color.WHITE) {
                            whiteWin = false;
                        }
                    }
                }
            }
        }
        checkRep();
        if (blackWin) {
            return Color.BLACK;
        } else if (whiteWin) {
            return Color.WHITE;
        } else {
            return null;
        }
    }

    /**
     * Gets the colors of the pieces surrounding an square on the board.
     *
     * @param row the row of the element we are finding the surroundings of
     * @param col the column of the element we are finding the surroundings of
     * @return a list of all of the values surrounding the element, ordered clockwise from the upper left
     * @spec.requires BOARD_SIZE >= 2
     * */
    public List<Color> getSurroundingColors(int row, int col) {
        checkRep();
        List<Color> colors = new ArrayList<>();
        List<Point> surroundingPoints = getSurroundingPoints(row, col);
        for (Point point : surroundingPoints) {
            if (entBoard[(int) point.getX()][(int) point.getY()] != null) {
                colors.add(entBoard[(int) point.getX()][(int) point.getY()].getColor());
            }
        }
        checkRep();

        return colors;
    }

    /**
     * Gets the points of the squares surrounding an square on the board.
     *
     * @param row the row of the element we are finding the surroundings of
     * @param col the column of the element we are finding the surroundings of
     * @return a list of all of the values surrounding the element, ordered clockwise from the upper left
     * @spec.requires BOARD_SIZE >= 2
     * */
    public List<Point> getSurroundingPoints(int row, int col) {
        checkRep();
        List<Point> result = new ArrayList<>();
        if (row == 0) {
            if (col == 0) {
                result.add(new Point(0, 1));
                result.add(new Point(1, 1));
                result.add(new Point(1, 0));
            } else if (col == BOARD_SIZE - 1) {
                result.add(new Point(1, col));
                result.add(new Point(1, col - 1));
                result.add(new Point(0, col - 1));
            } else {
                result.add(new Point(row, col + 1));
                result.add(new Point(row + 1, col  + 1));
                result.add(new Point(row + 1, col));
                result.add(new Point(row + 1, col - 1));
                result.add(new Point(row, col - 1));
            }
        } else if (row == BOARD_SIZE - 1) {
            if (col == 0) {
                result.add(new Point(row - 1, 0));
                result.add(new Point(row - 1, 1));
                result.add(new Point(row, 1));
            } else if (col == BOARD_SIZE - 1) {
                result.add(new Point(row - 1, col - 1));
                result.add(new Point(row - 1, col));
                result.add(new Point(row, col - 1));
            } else {
                result.add(new Point(row - 1, col - 1));
                result.add(new Point(row - 1, col));
                result.add(new Point(row - 1, col + 1));
                result.add(new Point(row, col + 1));
                result.add(new Point(row, col - 1));
            }
        }
        else {
            if (col == 0) {
                result.add(new Point(row - 1, col));
                result.add(new Point(row - 1, col + 1));
                result.add(new Point(row, col + 1));
                result.add(new Point(row + 1, col + 1));
                result.add(new Point(row + 1, col));
            } else if (col == BOARD_SIZE - 1) {
                result.add(new Point(row - 1, col - 1));
                result.add(new Point(row - 1, col));
                result.add(new Point(row + 1, col));
                result.add(new Point(row + 1, col - 1));
                result.add(new Point(row, col - 1));
            } else {
                result.add(new Point(row - 1, col - 1));
                result.add(new Point(row - 1, col));
                result.add(new Point(row - 1, col + 1));
                result.add(new Point(row, col + 1));
                result.add(new Point(row + 1, col + 1));
                result.add(new Point(row + 1, col));
                result.add(new Point(row + 1, col - 1));
                result.add(new Point(row, col - 1));
            }
        }
        checkRep();

        return result;
    }

    /**
     * This is a testing method to print the board to the console.
     *
     * in a 5x5 matrix.
     */
    public void print2D() {
        checkRep();
        System.out.print("\t\t");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + "\t\t");
        }
        System.out.println();
        System.out.println("    +-------+-------+-------+-------+-------+");
        int i = 0;
        for (Piece[] row : entBoard) {
            System.out.print(i + "\t|");
            for (Piece square : row) {
                if (square != null) {
                    System.out.print(" " + square.getColor() + "\t|");
                } else {
                    System.out.print("\t\t|");
                }
            }
            System.out.println();
            System.out.println("    +-------+-------+-------+-------+-------+");
            i++;
        }
        checkRep();
    }

    /**
     * This method checks that the representation invariant holds.
     */
    private void checkRep() {
        if (CHECK_REP_ON) {
            int black = 0;
            int white = 0;
            int empty = 0;
            for (Piece[] row : entBoard) {
                for (Piece piece : row) {
                    if (piece == null) {
                        empty++;
                    } else if (piece.getColor() == Color.BLACK) {
                        black++;
                    } else if (piece.getColor() == Color.WHITE) {
                        white++;
                    }
                }
            }

            assert (black == 7) : "there should be 7 black pieces";
            assert (white == 7) : "there should be 7 white pieces";
            assert (empty == 11) : "there should be 11 empty spaces";
        }
    }
}
