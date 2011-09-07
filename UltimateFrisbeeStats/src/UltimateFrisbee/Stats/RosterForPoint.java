package UltimateFrisbee.Stats;

import java.util.ArrayList;
import java.util.Iterator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	private static final int MIN_TO_SEC = 60;
	private static final int SEC_TO_MILLS = 1000;
	private static final int MIN_TO_MILLS = MIN_TO_SEC * SEC_TO_MILLS;
	private ArrayList<Player> Roster,onField;
	private Spinner rosterSP;
	private ArrayAdapter<Player> rosterArrayAdapter;
	private Button addPlayerB, startOffensivePoint,startDefensivePoint, clearListB;
	private TableLayout playersOnField;
	private boolean firstPoint = true;
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	private int theirScore = 0;
	private int ourScore= 0;
	private TextView score, timer;
	private CountDownTimer timeLeftInGame;
	private long gameStartTime;
	
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

		//set up visual elements on top tab 
		//TODO hoping to make this a floating tab at somepoint
		score = (TextView) findViewById(R.id.Score);	
		score.setText(ourScore + "-" + theirScore);
		timer = (TextView) findViewById(R.id.Time);
		
	
		playersOnField = (TableLayout) findViewById(R.id.playersOnField);
		
		onField = new ArrayList<Player>();
		
		addPlayerB = (Button) findViewById(R.id.addPlayerB);
		addPlayerB.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//add to array adapter and check that is not already added
				Player playerToAdd = (Player) rosterSP.getSelectedItem();
				if(!onField.contains(playerToAdd)){
					addPlayerToOnField(playerToAdd);
				}
				//playersOnField.invalidate();
			}
		});
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
		
		clearListB = (Button) findViewById(R.id.clearOnFieldB);
		clearListB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				RosterForPoint.this.clearOnField();
				redrawPlayersOnField();
			}

		});
		
		//setup timer
		int minInGame = 90;
		int timeToCountByInSec= 1;
		timeLeftInGame = new CountDownTimer(minInGame*MIN_TO_MILLS, timeToCountByInSec * SEC_TO_MILLS) {

		     public void onTick(long millisUntilFinished) {
		    	 int hours   = (int) ((millisUntilFinished / 1000) / 3600);
		    	 int seconds = (int) ((millisUntilFinished / 1000) % 60);
		    	 int minutes = (int) (((millisUntilFinished-1000*3600) / 1000) / 60);
		    	 timer.setText(String.format("%d:%02d:%02d", hours, minutes,seconds));
		    	 //LONGTERMTODO this can be done more efficently with someithng like the following i think
		    	 //timer.setText(String.format("%T",millisUntilFinished));
		    	 
		     }

		     public void onFinish() {
		    	 timer.setText("game over");
		     }
		  };
		
	}

	protected synchronized  void redrawPlayersOnField() {
		// TODO this can be done with listeners
		playersOnField.removeAllViews();
		//using an iterator as follows can cause a ConcurrentModificationException.
		// one could say not to thread this but instead i will just get a local copy and asume the user isn't removing 
		//XXX The convert to array idea causes very wierd issues, i think locks/syncronisation are the answer
		for(Iterator<Player> it = onField.iterator();it.hasNext();){
			Player tempPlayer = it.next();
			addPlayerToOnField(tempPlayer);
		}
		//folowing dosnt work  because of threads, im just gonna lock the resource cause its small
		//Player[] onFieldNow = new Player[onField.size()];
		//onFieldNow = onField.toArray(onFieldNow);
		//for(int i = 0; i<onFieldNow.length;i++){
		//	addPlayerToOnField(onFieldNow[i]);
		//}
		}
	private void addPlayerToOnField(Player playerToAdd){
		onField.add(playerToAdd);
		//TableRow tr = new TableRow(RosterForPoint.this);
		CheckBox playerCB = new CheckBox(RosterForPoint.this);
		playerCB.setChecked(true);
		playerCB.setOnCheckedChangeListener(new playerCheckedListenerWithRedraw(onField, playerToAdd, playerCB,RosterForPoint.this));
		playerCB.setText(playerToAdd.toString());
		playersOnField.addView(playerCB);
		Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG,"Added " + playerToAdd.toString() + " to on field" );
	}

	protected void checkAndInsertFirstGame() {
		// if this is the first point, put the game into the database
		if(RosterForPoint.this.isFirstPoint()){
			timeLeftInGame.start();
			RosterForPoint.this.setFirstPoint(false);
			Bundle rosterForGameExtras = getIntent().getExtras();
			ContentValues gameValues = new ContentValues();
			gameValues.put("tournament" ,"\"" + (String) rosterForGameExtras.get(NewGame.TOURNY_OR_GAME_NAME_KEY) + "\"");
			gameValues.put("opponent" ,"\"" +(String) rosterForGameExtras.get(NewGame.OPPONENT_NAME_KEY) + "\"");
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
			gameStartTime = ts.getTime();
			gameValues.put("time_started", gameStartTime);
			frisbeeData.insertOrThrow(UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_TN, null, gameValues);
			checkAndInsertTournament();
		}
	}

	private void checkAndInsertTournament() {
		// insert tournament into db if its not there
		Bundle rosterForGameExtras = getIntent().getExtras();
		if((Boolean) rosterForGameExtras.get(UltimateFrisbeeStatsActivity.NEW_TOURNAMENT_OR_GAME_BOOL)){
			ContentValues tournamentValues = new ContentValues();
			tournamentValues.put("name" ,"\"" + (String) rosterForGameExtras.get(NewGame.TOURNY_OR_GAME_NAME_KEY) + "\"");
			tournamentValues.put("date", gameStartTime-1);
			frisbeeData.insertOrThrow(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_TN,null,tournamentValues);
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
	public void clearOnField(){
		onField.clear();
	}
}
