package org.example.brickbreaker.classes;

public class Brick {
    private final int row;
    private final int column;
    private final int width;
    private final int height;
    private boolean isDestroyed;

    public Brick(int row, int column, int width, int height) {
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
        this.isDestroyed = false;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

}
