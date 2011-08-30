package UltimateFrisbee.Stats;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RosterForPoint extends Activity {
	//TODO all screens from this point onward should have a bar at the top showing timer and score
	//TODO add stats to database: walk through roster incriment games played, add game to game table
	public static final String ON_FIELD_KEY = "on field";
	public static final String DEFENSE_KEY = "defense?";
	public static final String POINT_FOR_KEY = "point for?";
	//TODO assure uniquness of folowing number
	private static final int POINT_FOR_REQEST_CODE = 1000;
	private ArrayList<Player> Roster,onField;
	private Spinner rosterSP;
	private ArrayAdapter<Player> rosterArrayAdapter;
	private Button addPlayerB, startOffensivePoint,startDefensivePoint;
	private TableLayout playersOnField;
	private boolean firstPoint = true;
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	private int theirScore = 0;
	private int ourScore= 0;
	private TextView score;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		theirScore = ourScore = 0;
		
		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();
	
		setContentView(R.layout.rosterforpoint);
		Bundle rosterForGameExtras = getIntent().getExtras();
		Roster = rosterForGameExtras.getParcelableArrayList(RosterForGame.ROSTER_FOR_GAME_KEY);
		rosterArrayAdapter = new ArrayAdapter<Player>(this,android.R.layout.simple_spinner_dropdown_item, Roster);
		rosterSP = (Spinner) findViewById(R.id.rosterSP);
		rosterSP.setAdapter(rosterArrayAdapter);

		score = (TextView) findViewById(R.id.Score);	
		score.setText(ourScore + "-" + theirScore);
	
		playersOnField = (TableLayout) findViewById(R.id.playersOnField);
		
		onField = new ArrayList<Player>();
		
		addPlayerB = (Button) findViewById(R.id.addPlayerB);
		addPlayerB.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//add to array adapter and check that is not already added
				
				Player playerToAdd = (Player) rosterSP.getSelectedItem();
				if(!onField.contains(playerToAdd)){
					onField.add(playerToAdd);
					//TableRow tr = new TableRow(RosterForPoint.this);
					CheckBox playerCB = new CheckBox(RosterForPoint.this);
					playerCB.setChecked(true);
					playerCB.setText(playerToAdd.toString());
					playersOnField.addView(playerCB);
					Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG,"Added " + playerToAdd.toString() + " to on field" );
				}
				//playersOnField.invalidate();
			}
		});
		//TODO add game to SQL database if this is first point
		//setup buttons
		startOffensivePoint =(Button) findViewById(R.id.startOffensePoint);
		startOffensivePoint.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				RosterForPoint.this.checkAndInsertFirstGame();
				RosterForPoint.this.startPoint(false);
			}

		});
		startDefensivePoint =(Button) findViewById(R.id.startDefensivePoint);
		startDefensivePoint.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				RosterForPoint.this.startPoint(true);
			}

		});
		
	}

	protected void checkAndInsertFirstGame() {
		// TODO Auto-generated method stub
		if(RosterForPoint.this.isFirstPoint()){
			RosterForPoint.this.setFirstPoint(false);
			Bundle rosterForGameExtras = getIntent().getExtras();
			ContentValues values = new ContentValues();
			values.put("tournament" ,"\"" + (String) rosterForGameExtras.get(NewGame.TOURNY_OR_GAME_NAME_KEY) + "\"");
			values.put("opponent" ,"\"" +(String) rosterForGameExtras.get(NewGame.OPPONENT_NAME_KEY) + "\"");
			
			//values.put("player_name", name);
			//values.put("number", number);
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
			values.put("time_started", ts.getTime());
			//try{
			frisbeeData.insertOrThrow(UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_TN, null, values);
			//}catch(SQLiteConstraintException e){
			//Toast.makeText(this, "Player " + name + " already on roster.", Toast.LENGTH_SHORT).show();
			//}
		}
	}

	public void startPoint(boolean defense){		
		//add data to intent and start offensive screen
		//TODO send score to this screen
		Intent intent = new Intent(RosterForPoint.this, StatPoint.class);
		Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG, "on field this point:" + onField.toString());
		intent.putParcelableArrayListExtra(ON_FIELD_KEY,onField);
		intent.putExtra(DEFENSE_KEY, defense);
		startActivityForResult(intent,POINT_FOR_REQEST_CODE);
	}

	public boolean isFirstPoint() {
		return firstPoint;
	}

	public void setFirstPoint(boolean firstPoint) {
		this.firstPoint = firstPoint;
	}
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		if(requestCode == POINT_FOR_REQEST_CODE){
			if(resultCode == RESULT_OK){
				if(data.getBooleanExtra(POINT_FOR_KEY, true)){
					ourScore++;
					Toast.makeText(this, "we scored", Toast.LENGTH_SHORT).show();
				}else{
					theirScore++;
					Toast.makeText(this, "they scored", Toast.LENGTH_SHORT).show();
				}
				score.setText(ourScore + "-" + theirScore);
			}
		}
	}
}
