package com.domkick1.othello;

public class Move
{

    private int row;
    private int col;
    private int rating;


    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public Move() {
        this(0,0);
    }

    public Move(int rating){
        this();
        this.rating = rating;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public void setRow(int row) {
        this.row = row;
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Move))
            return false;
        Move move = (Move) o;
        return row == move.row && col == move.col;
    }
}
