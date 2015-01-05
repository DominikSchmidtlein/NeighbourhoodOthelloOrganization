package com.domkick1.othello;

import android.content.Context;
import android.view.View;
import android.widget.Button;



public class ButtonHandler {
	
	Button[][] buttons;
	Othello othello;
	char[][] grid;
	OthelloActivity activity;
	boolean status;
	private int numberOfPlayers;
	
	public ButtonHandler(Othello othello, Button[][] buttons, OthelloActivity activity, int numberOfPlayers){
		this.buttons = buttons;
		this.othello = othello;
		this.grid = othello.getGrid();
		this.activity = activity;
		this.activity.updateGrid(grid);
		status = true;
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public void click(View view){
		if(!status){
			return;
		}
		Move move = null;
		for(int i = 0;i < Othello.SIZE;i++){
			for(int j = 0;j < Othello.SIZE;j++){
				if(buttons[i][j].getId() == view.getId()){
					move = new Move(i,j);
				}
			}
		}
		int result = othello.play(move);
		if(result == Othello.ILLEGAL){
			activity.updateInstruction(result, othello.getTurn());
			return;
		}
		if(result == Othello.X_WON || result == Othello.O_WON || result == Othello.TIE){
			status = false;
		}
		if(numberOfPlayers == 1 && status){
			result = othello.machinePlay();
			if(result == Othello.NO_MOVE){
				if(othello.generateMoves(othello.getTurn()).isEmpty()){
	                result = othello.isWinner();
	                status = false;
	            }
	            else{
	                return;
	            }
	        }
			else{
	            while(othello.generateMoves(othello.getTurn()).isEmpty()){
	                othello.toggleTurn();
	                result = othello.machinePlay();
	                if(result == Othello.NO_MOVE){
	                    result = othello.isWinner();
	                    break;
	                }
	            }
	        }
			
		}
		activity.updateGrid(grid);
		activity.updateInstruction(result, othello.getTurn());
		activity.updateScores(othello.countChips());
		
		/*
		 * 
		 
        
        
        else{
            while(generateMoves().isEmpty()){
                toggleTurn();
                result = machinePlay();
                toggleTurn();
                if(result == NO_MOVE){
                    print();
                    determineWinner();
                    break;
                }
            }
        }
        print();
		 * 
		 */
		
	}
	
	
}
