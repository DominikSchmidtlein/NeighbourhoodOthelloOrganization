package com.domkick1.othello;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OthelloActivity extends Activity {
	
	private int numberOfPlayers;
	private Button[][] buttons;
	private ButtonHandler handler;
	private TextView instruction;
	private TextView blackScore;
	private TextView whiteScore;
	private TextView blackName;
	private TextView whiteName;
	private String namePlayerBlack;
	private String namePlayerWhite;
	private boolean playerIsBlack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_othello);
		
		Intent intent = getIntent();
		numberOfPlayers = intent.getIntExtra(MainActivity.NUMBER_OF_PLAYERS,1);
		namePlayerBlack = intent.getStringExtra(MainActivity.NAME1);
		namePlayerWhite = intent.getStringExtra(MainActivity.NAME2);
		
		instruction = (TextView) findViewById(R.id.instruction);
		blackScore = (TextView) findViewById(R.id.black_player_score);
		whiteScore = (TextView) findViewById(R.id.white_player_score);
		blackName = (TextView) findViewById(R.id.black_player_name);
		whiteName = (TextView) findViewById(R.id.white_player_name);
		
		if(numberOfPlayers == 1){
			if(namePlayerWhite == null){
				playerIsBlack = true;
				blackName.setText(namePlayerBlack);
				namePlayerWhite = getString(R.string.name_player2);
			}
			else{
				playerIsBlack = false;
				whiteName.setText(namePlayerWhite);
				namePlayerBlack = getString(R.string.name_player1);
			}
		}
		else{
			blackName.setText(namePlayerBlack);
			whiteName.setText(namePlayerWhite);
		}
		
		LinearLayout row0 = (LinearLayout) findViewById(R.id.row0);
		LinearLayout row1 = (LinearLayout) findViewById(R.id.row1);
		LinearLayout row2 = (LinearLayout) findViewById(R.id.row2);
		LinearLayout row3 = (LinearLayout) findViewById(R.id.row3);
		LinearLayout row4 = (LinearLayout) findViewById(R.id.row4);
		LinearLayout row5 = (LinearLayout) findViewById(R.id.row5);
		LinearLayout row6 = (LinearLayout) findViewById(R.id.row6);
		LinearLayout row7 = (LinearLayout) findViewById(R.id.row7);
		
		LinearLayout[] rows = {row0,row1,row2,row3,row4,row5,row6,row7};
		buttons = new Button[8][8];
		
		for(int i = 0;i < 8;i++){
			for(int j = 0;j < rows[i].getChildCount();j++){
				buttons[i][j] = (Button) rows[i].getChildAt(j);
			}
		}
		
		handler = new ButtonHandler(new Othello(), buttons, this, numberOfPlayers);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.othello, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void click(View view){
		handler.click(view);
	}
	
	public void updateGrid(char[][] grid){
		for(int i = 0;i < 8;i++){
			for(int j = 0;j < 8;j++){
				if(grid[i][j] == Othello.X){
					buttons[i][j].setBackgroundColor(getResources().getColor(R.color.black));
				}
				else if(grid[i][j] == Othello.O){
					buttons[i][j].setBackgroundColor(getResources().getColor(R.color.white));
				}
				
			}
		}
	}
	
	public void updateScores(ArrayList<Integer> scores){
		blackScore.setText(scores.get(0).toString());
		whiteScore.setText(scores.get(1).toString());
	}
	
	public void updateInstruction(int i, char turn){
		if(i == Othello.ILLEGAL){
			instruction.setText(R.string.illegal_move);
		}
		else if(i == Othello.O_WON){
			instruction.setText(getString(R.string.winner) + namePlayerWhite);
		}
		else if(i == Othello.X_WON){
			instruction.setText(getString(R.string.winner) + namePlayerBlack);
		}
		else if(i == Othello.TIE){
			instruction.setText(R.string.tie);
		}
		else if(i == Othello.O_STUCK){
			instruction.setText(namePlayerWhite + getString(R.string.stuck));
		}
		else if(i == Othello.X_STUCK){
			instruction.setText(namePlayerBlack + getString(R.string.stuck));
		}
		else if(i == Othello.ONGOING){
			if(turn == Othello.X){
				instruction.setText(getString(R.string.instruction_message) + namePlayerBlack);
			}
			else{
				instruction.setText(getString(R.string.instruction_message) + namePlayerWhite);
			}
		}
	}
}
