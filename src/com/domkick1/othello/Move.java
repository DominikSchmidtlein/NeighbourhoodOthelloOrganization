package com.domkick1.othello;

public class Move
{

    private int row;
    private int col;


    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public Move() {
        this(0,0);
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
    public boolean equals(Object o){
        Move move = (Move) o;
        if(row == move.row && col == move.col){
            return true;
        }
        else{
            return false;
        }
    }
}
