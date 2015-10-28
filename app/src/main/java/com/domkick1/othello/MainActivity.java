package com.domkick1.othello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

	public static final String NUMBER_OF_PLAYERS = "com.domkick1.tictactoe.NumberOfPlayers";
	public static final String NAME1 = "com.domkick1.othello.name1";
	public static final String NAME2 = "com.domkick1.othello.name2";
	
	EditText editTextName1;
	EditText editTextName2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editTextName1 = (EditText) findViewById(R.id.edit_text_name1);
    	editTextName2 = (EditText) findViewById(R.id.edit_text_name2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void startGame(View view){
    	Intent intent = new Intent(this,OthelloActivity.class);
    	
    	String name1 = editTextName1.getText().toString();
    	String name2 = editTextName2.getText().toString();
    	
    	int numberOfPlayers;
    	
    	if(name1.length() == 0 && name2.length() == 0){
    		return;
    	}
    	else if(name1.length() != 0 && name2.length() != 0){
    		numberOfPlayers = 2;
    		intent.putExtra(NAME1, name1);
    		intent.putExtra(NAME2, name2);
    	}
    	else if(name1.length() != 0){
    		numberOfPlayers = 1;
    		intent.putExtra(NAME1, name1);
    	}
    	else{
    		numberOfPlayers = 1;
    		intent.putExtra(NAME2, name2);
    	}
    	
    	intent.putExtra(NUMBER_OF_PLAYERS, numberOfPlayers);
    	
    	startActivity(intent);
    }
    
    public void defaultName(View view){
    	if(view.getId() == R.id.description1){
    		editTextName1.setText(R.string.name_player1);
    	}
    	else if(view.getId() == R.id.description2){
    		editTextName2.setText(R.string.name_player2);
    	}
    }
}
