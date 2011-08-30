package UltimateFrisbee.Stats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewGame extends Activity {
	public static final String TOURNY_OR_GAME_NAME_KEY = "tournament or game name";
	public static final String OPPONENT_NAME_KEY = "opponent name";
	public static final String GAME_TIME_IN_MIN_KEY = "game time in min";
	protected static final String POINTS_IN_GAME_KEY = "points in game";
	private Button StartGameB;
	private EditText  TournamentOrLabelET,OpponentET, GameLengthET,PointsInGameET;
	private Bundle extras;
	//public frisbeeOpenHelper frisbeeOpenHelper;
	//private SQLiteDatabase frisbeeData;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//TODO add checkbox for bracket or pool play
		//TODO setup something for gender and size of team on field
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        
		//frisbeeOpenHelper = new frisbeeOpenHelper(this);
		//frisbeeData = frisbeeOpenHelper.getWritableDatabase();

        //setup graphical elements
        TournamentOrLabelET = (EditText) findViewById(R.id.TournamentorLabelET);
        OpponentET = (EditText) findViewById(R.id.OpponentET);
        GameLengthET = (EditText) findViewById(R.id.GameLengthET);
        StartGameB = (Button) findViewById(R.id.StartGameB);
        PointsInGameET = (EditText) findViewById(R.id.pointsInGameET);
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
				//I'm choosing to not put the game in the database yet as the risk of cancelation is high at this point and i want game time to be ase close to actual game start as possible)
				Intent intent = new Intent(NewGame.this, RosterForGame.class);
				intent.putExtra(TOURNY_OR_GAME_NAME_KEY, TournamentOrLabelET.getText().toString());
				intent.putExtra(OPPONENT_NAME_KEY, OpponentET.getText().toString());
				intent.putExtra(POINTS_IN_GAME_KEY, Integer.parseInt(PointsInGameET.getText().toString()));
				//TODO error checking on game length
				intent.putExtra(GAME_TIME_IN_MIN_KEY, Integer.parseInt(GameLengthET.getText().toString()));
				startActivity(intent);
			}
        	
        });
   
	}


}
