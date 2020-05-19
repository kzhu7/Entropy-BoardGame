package test;

import main.java.Board;
import main.java.Color;
import main.java.Color;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;

/**
 * This class tests the Board class.
 */

public class BoardTest {

    @Test
    public void testGetSurroundingPoints() {
        Board board = new Board();
        System.out.println(board.getSurroundingPoints(0, 0));
        System.out.println(board.getSurroundingPoints(0, 4));
        System.out.println(board.getSurroundingPoints(4, 0));
        System.out.println(board.getSurroundingPoints(4, 4));
        System.out.println(board.getSurroundingPoints(0, 2));
        System.out.println(board.getSurroundingPoints(4, 2));
        System.out.println(board.getSurroundingPoints(2, 0));
        System.out.println(board.getSurroundingPoints(2, 4));
        System.out.println(board.getSurroundingPoints(2, 2));
    }

    @Test
    public void testGetSurroundingColors() {
        Board newBoard = new Board();
        newBoard.print2D();
        List<Color> topLeft = newBoard.getSurroundingColors(0, 0);
        List<Color> topRight = newBoard.getSurroundingColors(0, 4);
        List<Color> bottomLeft = newBoard.getSurroundingColors(4, 0);
        List<Color> bottomRight = newBoard.getSurroundingColors(4, 4);
        List<Color> topRow = newBoard.getSurroundingColors(0, 2);
        List<Color> bottomRow = newBoard.getSurroundingColors(4, 1);
        List<Color> leftColumn = newBoard.getSurroundingColors(2, 0);
        List<Color> rightColumn = newBoard.getSurroundingColors(3, 4);
        List<Color> middle = newBoard.getSurroundingColors(2, 2);

        assertEquals(topLeft.toString(), "[BLACK, null, BLACK]");
        assertEquals(topRight.toString(), "[BLACK, null, BLACK]");
        assertEquals(bottomLeft.toString(), "[WHITE, null, WHITE]");
        assertEquals(bottomRight.toString(), "[null, WHITE, WHITE]");
        assertEquals(topRow.toString(), "[BLACK, null, null, null, BLACK]");
        assertEquals(bottomRow.toString(), "[WHITE, null, null, WHITE, WHITE]");
        assertEquals(leftColumn.toString(), "[BLACK, null, null, null, WHITE]");
        assertEquals(rightColumn.toString(), "[null, null, WHITE, WHITE, null]");
        assertEquals(middle.toString(), "[null, null, null, null, null, null, null, null]");
    }

    @Test
    public void testLegalMoves() {
        Board board = new Board();
        board.print2D();
        System.out.println(board.legalMoves(1, 0));
    }

    @Test
    public void testPrint2D() {
        Board board = new Board();
        board.print2D();
    }
}
