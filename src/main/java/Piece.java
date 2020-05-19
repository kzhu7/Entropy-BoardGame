package main.java;

/**
 * Represents a piece in a game of entropy.
 */
public class Piece {

    private int x;

    private int y;

    private Color color;

    public Piece(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    //region Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
    //endregion
}
