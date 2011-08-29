package UltimateFrisbee.Stats;

import java.util.Collection;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewGame extends Activity {
	private Button StartGameB;
	private EditText  TournamentOrLabelET,OpponentET, GameLengthET;
	private Bundle extras;
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//TODO add checkbox for bracket or pool play
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        
		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();

        //setup graphical elements
        TournamentOrLabelET = (EditText) findViewById(R.id.TournamentorLabelET);
        OpponentET = (EditText) findViewById(R.id.OpponentET);
        GameLengthET = (EditText) findViewById(R.id.GameLengthET);
        StartGameB = (Button) findViewById(R.id.StartGameB);
        //get bundle
        extras = getIntent().getExtras();
        if(extras != null){
        	if(!extras.getBoolean(UltimateFrisbeeStatsActivity.NEW_TOURNAMENT_OR_GAME_BOOL)){
        		TournamentOrLabelET.setKeyListener(null);
        		TournamentOrLabelET.setText(extras.getString(UltimateFrisbeeStatsActivity.SELECTED_TOURNAMENT));
        	}
        }
        StartGameB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewGame.this, RosterForGame.class);
				startActivity(intent);
			}
        	
        });
   
	}


}
