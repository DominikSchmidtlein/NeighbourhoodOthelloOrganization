package com.domkick1.othello;

import java.util.ArrayList;
import java.lang.Boolean;
import java.lang.Integer;
import java.util.Scanner;
import java.lang.NumberFormatException;

public class Othello{
    
    public static final int SIZE = 8;
    public static final char EMPTY = '_';
    public static final char X = 'x';
    public static final char O = 'o';
    public static final int MIDDLE_INSIDE = 3;
    public static final int MIDDLE_OUTSIDE = 4;
    public static final int OPTIONS = 9;
    private static final String SPACEBAR = " ";
    private static final int INDEX_ZERO = 0;
    
    public static final int X_STUCK = -2;
    public static final int O_STUCK = -1;
    public static final int ILLEGAL = 0;
    public static final int ONGOING = 1;
    public static final int TIE = 2;
    public static final int X_WON = 3;
    public static final int O_WON = 4;
    public static final int NO_MOVE = 5;
    
    public static final int UP_LEFT = 1;
    public static final int UP = 2;
    public static final int UP_RIGHT = 3;
    public static final int LEFT = 4;
    public static final int RIGHT = 6;
    public static final int DOWN_LEFT = 7;
    public static final int DOWN = 8;
    public static final int DOWN_RIGHT = 9;
    
    private boolean ongoing;
    private char turn;
    private char[][] grid;
    private Scanner scanner;
    private MoveStrategy moveStrategy;
    
    public Othello(){
        grid = new char[SIZE][SIZE];
        turn = X;
        scanner = new Scanner(System.in);
        ongoing = true;
        moveStrategy = new GreedyMove();
        
        for(int i = 0;i < SIZE;i++){
            for(int j = 0;j < SIZE;j++){
                if(i == MIDDLE_INSIDE && j == MIDDLE_INSIDE ||
                    i == MIDDLE_OUTSIDE && j == MIDDLE_OUTSIDE){
                        grid[i][j] = X;
                }
                else if(i == MIDDLE_INSIDE && j == MIDDLE_OUTSIDE ||
                    i == MIDDLE_OUTSIDE && j == MIDDLE_INSIDE){
                        grid[i][j] = O;
                }
                else{
                    grid[i][j] = EMPTY;
                }
            }
        }
    }
    
    public Othello(Othello othello){
        this.ongoing = othello.ongoing;
        this.turn = othello.turn;
        grid = new char[Othello.SIZE][Othello.SIZE];
        for(int i = 0;i < SIZE;i++){
            for(int j = 0;j < SIZE;j++){
                this.grid[i][j] = othello.grid[i][j];
            }
        }
    }
    
    public void loop(){
        while(ongoing){
            this.print();
            ArrayList<Integer> chipCount = countChips();
            //System.out.println("X chips: " + chipCount.get(0) + "\nO chips: " + chipCount.get(1));
            if(turn == X || turn == O){
                boolean legal = false;
                ArrayList<Move> endPoints = new ArrayList<Move>();
                Move move = new Move();
                while(!legal){
                    move = getMove();
                    if(move == null){
                        legal = true;
                        ongoing = false;
                    }
                    else{
                        endPoints = this.isLegal(turn,move);
                        if(endPoints != null){
                            legal = true;
                        }
                    }
                }
                if(move != null){
                    this.setMove(turn,move,endPoints);
                }
            }
            else{
                //System.out.println("Computer turn:");
                machinePlay();
            }
            toggleTurn();
            if(generateMoves(this.turn).size() == 0){
                //System.out.println("Player " + turn + " could not play, thus the turn was forfeited.");
                toggleTurn();
            }
            if(isWinner() != ONGOING){
                ongoing = false;
                this.print();
                if(isWinner() == X_WON){
                    //System.out.println("X won the game. Goodbye!");
                }
                else if(isWinner() == O_WON){
                    //System.out.println("O won the game. Goodbye!");
                }
                else{
                    //System.out.println("The game ended in a tie. Goodbye!");
                }
            }
        }
    }
    
    public int evaluateMove(Move move){
    	int ring = getRing(move);
        if(ring == 1){
        	if(isCornerInRing(move)){
        		return 10;
        	}
        	return 9;
        }
        else if(ring == 2){
        	if(isCornerInRing(move)){
        		return 0;
        	}
        	return 1;
        }
        else{
        	return 5;
        }
    }
    
    private boolean isCornerInRing(Move move){
    	int ring = getRing(move);
    	return((move.getRow() == ring-1 || move.getRow() == SIZE-ring) && (move.getCol() == ring-1 || move.getCol() == SIZE-ring));
    }
    
    private boolean isInRing(Move move, int ring){
        if(move.getRow() >= SIZE/2-1 && move.getRow() <= SIZE/2 && move.getCol() >= SIZE/2-1 && move.getCol() <= SIZE/2){
            return true;
        }
        return(move.getRow() >= ring-1 && move.getRow() <= SIZE-ring &&
            move.getCol() >= ring-1 && move.getCol() <= SIZE-ring && !isInRing(move,ring+1));
    }
    
    public int getRing(Move move){
        for(int i =0;i < SIZE/2;i++){
            if(isInRing(move,SIZE/2-i)){
                return SIZE/2-i;
            }
        }
        return -1;
    }
    
    public char getTurn(){
    	return turn;
    }
    
    public int play(Move move){
    	ArrayList<Move> endPoints = this.isLegal(turn, move);
    	if(endPoints == null){
    		return ILLEGAL;    		
    	}
    	else{
    		this.setMove(turn,move,endPoints);
    		toggleTurn();
    		return isWinner();
    	}
    }
    
    private boolean isStuck(char turn){
    	if(generateMoves(turn).size() == 0 && generateMoves(opposingTurn(turn)).size() != 0){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    public int isWinner(){
        if(generateMoves(this.turn).size() == 0 && 
            generateMoves(opposingTurn(this.turn)).size() == 0){
                ArrayList<Integer> chipCount = countChips();
                int xChips = chipCount.get(0);
                int oChips = chipCount.get(1);
                if(xChips > oChips){
                    return X_WON;
                }
                else if(xChips < oChips){
                    return O_WON;
                }
                else{
                    return TIE;
                }    
        }
        else if(isStuck(turn)){
        	toggleTurn();
        	if(turn == X){
        		return O_STUCK;
        	}
        	else {
        		return X_STUCK;
        	}
        }
        else{
            return ONGOING;
        }        
    }
    
    public ArrayList<Integer> countChips(){
        int xChips = 0;
        int oChips = 0;
        for(char[] row:grid){
            for(char col:row){
                if(col == X){
                    xChips++;
                }
                else if(col == O){
                    oChips++;
                }
            }
        }
        
        ArrayList<Integer> chipCount = new ArrayList<Integer>();
        chipCount.add(xChips);
        chipCount.add(oChips);
        return chipCount;
    }
    
    private Move getMove(){
        //System.out.println("Please enter your next move, it must be legal. (row space col)" + turn);
        boolean result = true;
        int row = 0;
        int col = 0;
        while(result){
            String plyMove = scanner.nextLine();
            if(plyMove.equals("x")){
                return null;
            }
            else{
                result = false;
                if(plyMove.length() == 3){
                    try{
                        row = Integer.parseInt(plyMove.charAt(INDEX_ZERO)+"");
                        char mid = plyMove.charAt(1);
                        col = Integer.parseInt(plyMove.charAt(2)+"");
                    }
                    catch(NumberFormatException nfe){
                        result = true;
                    }
                }
                else{
                    result = true;
                }
            }
        }
        return new Move(row,col);
    }
    
    private void print() {
        System.out.println(toString());
    }
    
    @Override
    public String toString() {
        String myString = new String();
        for(char[] row:grid) {
            for(char c:row){
                myString += c + " ";
            }
            myString += "\n";
        }
        return myString;
    }
    
    private ArrayList<Move> isLegal(char turn, Move move){
        //if move is out of bounds return null
        if(!isInbounds(move)){
            return null;
        }
        //if desired move is already occupied, return empty array
        if(grid[move.getRow()][move.getCol()] != EMPTY){
            return null;
        }
        //if there are no occupied neighbouring fields, return empty array
        if(!this.hasAround(opposingTurn(turn),move).contains(true)){
           return null;
        }
        ArrayList<Boolean> directions = this.hasAround(opposingTurn(turn),move);
        ArrayList<Move> endPoints = new ArrayList<Move>();
        
        //iterate trough all 8 potential directions around
        for(int i = 0;i < OPTIONS;i++){
            //if there is an opposing team's chip in the specified direction
            if(directions.get(i)){
                //increment to skip the neighbouring spot
                Move checkMove = incrementInLine(i + 1, move);
                boolean checking = true;
                while(checking){
                    checkMove = incrementInLine(i + 1, checkMove);
                    if(!isInbounds(checkMove)){
                        checking = false;
                    }
                    else if(grid[checkMove.getRow()][checkMove.getCol()] == turn){
                        endPoints.add(new Move(checkMove.getRow(),checkMove.getCol()));
                        checking = false;
                    }
                    else if(grid[checkMove.getRow()][checkMove.getCol()] == EMPTY){
                        checking = false;
                    }
                    
                }
            }
        }
        if(endPoints.size() == 0){
            return null;
        }
        return endPoints;
    }
    
    private Move incrementInLine(int dir, Move move){
        if(dir == UP_LEFT){
            return new Move(move.getRow() - 1, move.getCol() - 1);
        }
        else if(dir == UP){
            return new Move(move.getRow() - 1, move.getCol());
        }
        else if(dir == UP_RIGHT){
            return new Move(move.getRow() - 1, move.getCol() + 1);
        }
        else if(dir == LEFT){
            return new Move(move.getRow(), move.getCol() - 1);
        }
        else if(dir == RIGHT){
            return new Move(move.getRow(), move.getCol() + 1);
        }
        else if(dir == DOWN_LEFT){
            return new Move(move.getRow() + 1, move.getCol() - 1);
        }
        else if(dir == DOWN){
            return new Move(move.getRow() + 1, move.getCol());
        }
        else if(dir == DOWN_RIGHT){
            return new Move(move.getRow() + 1, move.getCol() + 1);
        }
        else {
            return null;
        }
    }
    
    private ArrayList<Boolean> hasAround(char c, Move move){
        ArrayList<Boolean> directions = new ArrayList<Boolean>(OPTIONS);
        int index = 0;
        for(int i = 0;i < 3;i++){
            for(int j = 0;j < 3;j++){
                //check if middle
                if(!(i == 1 && j == 1)){
                    //check if index inbounds
                    if(isInbounds(new Move(move.getRow() + i - 1,move.getCol() + j - 1))){
                            if(grid[move.getRow() + i - 1][move.getCol() + j - 1] == c){
                                directions.add(true);
                            }
                            else{
                                directions.add(false);
                            }
                    }
                    else{
                        directions.add(false);
                    }
                }
                else{
                    directions.add(false);
                }   
                index++;
            }
        }
        return directions;
    }
    
    private boolean isInbounds(Move move){
        return move.getRow() >= 0 &&
                move.getRow() < SIZE &&
                move.getCol() >= 0 &&
                move.getCol() < SIZE;
    }
    
    private char opposingTurn(char turn){
        if(turn == X){
            return O;
        }
        else{
            return X;
        }
    }
    
    private int getDirection(Move m1,Move m2){
        if(m1.getRow() > m2.getRow()){
            if(m1.getCol() > m2.getCol()){
                return UP_LEFT;
            }
            else if(m1.getCol() == m2.getCol()){
                return UP;
            }
            else{
                return UP_RIGHT;
            }
        }
        else if (m1.getRow() == m2.getRow()){
            if(m1.getCol() > m2.getCol()){
                return LEFT;
            }
            else{
                return RIGHT;
            }
        }
        else{
            if(m1.getCol() > m2.getCol()){
                return DOWN_LEFT;
            }
            else if(m1.getCol() == m2.getCol()){
                return DOWN;
            }
            else{
                return DOWN_RIGHT;
            }            
        }
    }
    
    private void setChar(char turn, Move move){
        grid[move.getRow()][move.getCol()] = turn;
    }
    
    private void setMove(char turn, Move move, ArrayList<Move> eP){
        for(Move m:eP){
            Move nextMove = move;
            int direction = getDirection(move,m);
            while(!nextMove.equals(m)){
                setChar(turn,nextMove);;
                nextMove = incrementInLine(direction,nextMove);
            }
        }
    }
    
    public void toggleTurn() {
        turn = opposingTurn(turn);
    }
    
    public ArrayList<Move> generateMoves(char turn){
        Move move;
        ArrayList<Move> generatedMoves = new ArrayList<Move>();
        for(int i = 0;i < SIZE;i++){
            for(int j = 0;j < SIZE;j++){
                move = new Move(i,j);
                if(isLegal(turn,move) != null){
                    generatedMoves.add(move);
                }
            }
        }
        return generatedMoves;
    }
    
    public int machinePlay(){
        Move move = moveStrategy.selectMove(this);
        if(move == null){
        	return NO_MOVE;
        }
        return play(move);
    }
    
    private Move firstAvailableMove() {
        return this.generateMoves(turn).get(0);
    }
    
    private Move randomMove() {
        ArrayList<Move> moves = generateMoves(turn);
        return moves.get((int)(Math.random()*(moves.size())));
    }
    
    public char[][] getGrid(){
    	return grid;
    }
    
}