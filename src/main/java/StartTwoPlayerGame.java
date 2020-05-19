package main.java;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StartTwoPlayerGame {

    private static Board newBoard = new Board();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        newBoard.print2D();
        while (true) {
            if (!newBoard.mustPass(Color.WHITE)) {
                turn(Color.WHITE);
            } else {
                System.out.println("There are no valid moves for " + Color.WHITE + ". You must pass your turn.");
            }
            if (newBoard.checkWin() == Color.WHITE) {
                break;
            }
            newBoard.print2D();
            if (!newBoard.mustPass(Color.BLACK)) {
                turn(Color.BLACK);
            } else {
                System.out.println("There are no valid moves for " + Color.BLACK + ". You must pass your turn.");
            }
            if (newBoard.checkWin() == Color.BLACK) {
                break;
            }
            newBoard.print2D();
        }
        System.out.println(newBoard.checkWin() + " wins!");
        newBoard.print2D();
    }

    /**
     * A player's turn.
     *
     * @param player the color of the player whose turn it is
     * @spec.requires board != null
     * @spec.modifies board
     * @spec.effects board is now a turn ahead of the original board, ie one piece has moved
     */
    private static void turn(Color player) {
        // Getting all the pieces in check, if there are pieces in check
        List<Point> pointsOfCheck = newBoard.inCheck(player);

        // If there are pieces in check, get the surrounding squares as valid end positions
        List<Point> validEndPositions = new ArrayList<>();
        if (!pointsOfCheck.isEmpty()) {
            for (Point point : pointsOfCheck) {
                validEndPositions.addAll(newBoard.getSurroundingPoints(point.x, point.y));
            }
        }

        int startX = -1;
        int startY = -1;

        // Ask for a piece to move until user gives coordinates of a valid piece to move
        while (true) {
            System.out.println(player + "'s turn");
            System.out.print("Enter a piece row position: ");
            startX = scanner.nextInt();
            System.out.print("Enter a piece column position: ");
            startY =  scanner.nextInt();
            try {
                Piece piece = newBoard.getPieceAtSquare(startX, startY);
                if (newBoard.isMovable(startX, startY)) {
                    if (piece.getColor() == player) {
                        break;
                    } else {
                        System.out.println("You don't have a " + player + " piece there");
                    }
                } else {
                    System.out.println("Piece is not movable");
                }
            } catch (IllegalArgumentException illegalMove) {
                System.out.println("Coordinate out of bounds");
            }
        }

        // Asks for a destination for the piece, if the destination is invalid, turn starts over
        while (true) {
            System.out.print("Enter a destination row position: ");
            int endX = scanner.nextInt();
            System.out.print("Enter a destination column position: ");
            int endY = scanner.nextInt();
            Point endPoint = new Point(endX, endY);
            if (!validEndPositions.isEmpty() && !validEndPositions.contains(endPoint)) {
                System.out.println("Need to get a piece out of check. Pieces in check: ");
                System.out.println(pointsOfCheck.toString());
                turn(player);
                break;
            } else {
                try {
                    newBoard.movePiece(startX, startY, endX, endY);
                    break;
                } catch (IllegalArgumentException illegalMove) {
                    System.out.println("Cannot move " + player + " piece there.");
                    turn(player);
                    break;
                }
            }
        }
    }
}
