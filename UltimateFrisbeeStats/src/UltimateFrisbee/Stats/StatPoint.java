package UltimateFrisbee.Stats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatPoint extends Activity {
	public static final int NAME_WIDTH = 100;
	public static final int BUTTON_WIDTH = 125;
	//folowing is 0 indexed
	public static final int BUTTON_TEXT_LENGTH = 8;
	private ArrayList<Player> onField;
	private ArrayList<PlayerWithPlayerPointID> onFieldWithID;
	int playersOnFieldThisPoint = 0;
	//list of stats to keep
	private ArrayList<String> statsToKeepOffense, statsToKeepDefense;
	public ArrayList<String> getStatsToKeepDefense() {
		return statsToKeepDefense;
	}

	public void setStatsToKeepDefense(ArrayList<String> statsToKeepDefense) {
		this.statsToKeepDefense = statsToKeepDefense;
	}

	public ArrayList<String> getStatsToKeepOffense() {
		return statsToKeepOffense;
	}

	public void setStatsToKeepOffense(ArrayList<String> statsToKeepOffense) {
		this.statsToKeepOffense = statsToKeepOffense;
	}
	private boolean onDefense;

	
	public boolean isOnDefense() {
		return onDefense;
	}
	//database
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	
	//visual elements 
	private Button turnoverB,pointScoredB;
	private TableLayout onFieldPlayersL;
	
	//keys for database
	private long point_id;
	private long game_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		
		setContentView(R.layout.stat_point);
		//setup the point id for this point
		//TODO save this point to the database
		//TODO add notes about the point
		java.util.Date today = new java.util.Date();
		java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
		point_id = ts.getTime();
		
		//open db for this proceess
		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();
		
		//TODO get the stats from the incoming bundle OR database
		statsToKeepOffense = new ArrayList<String>();
		statsToKeepOffense.add("Drops");
		statsToKeepOffense.add("Hucks");
		statsToKeepOffense.add("Bad Decisions");
		
		//TODO get the stats from the incoming bundle OR database
		statsToKeepDefense = new ArrayList<String>();
		statsToKeepDefense.add("Handblock");
		statsToKeepDefense.add("Footblock");
		statsToKeepDefense.add("Bad Decisions");
		
		//setup turnover button
		turnoverB = (Button) findViewById(R.id.turnoverB);
		turnoverB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onDefense = !onDefense;
				StatPoint.this.redrawStatsBox();
			}
			
		});
		
		//setup point scored button
		pointScoredB = (Button) findViewById(R.id.pointScoredB);
		pointScoredB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent();
				mIntent.putExtra(RosterForPoint.POINT_FOR_KEY, !StatPoint.this.isOnDefense());
				StatPoint.this.setResult(RESULT_OK,mIntent);
				savePointAndPlayerPointToDB();
				StatPoint.this.finish();
			}
			
		});
		//setup the screen
		onFieldPlayersL = (TableLayout) findViewById(R.id.playersOnField);
		//onFieldPlayersL.setLayoutParams( new TableLayout.LayoutParams(2,2) );
		onFieldPlayersL.setPadding(1,1,1,1);
		Bundle rosterForGameExtras = getIntent().getExtras();
		onField = rosterForGameExtras.getParcelableArrayList(RosterForPoint.ON_FIELD_KEY);
		onDefense =  rosterForGameExtras.getBoolean(RosterForPoint.DEFENSE_KEY);
		game_id = rosterForGameExtras.getLong(RosterForPoint.GAME_ID_KEY);
		//go through players and add them and their stat buttons to the screen
		onFieldWithID = new ArrayList<PlayerWithPlayerPointID>();
		for(Iterator<Player> it = onField.iterator();it.hasNext();){
			playersOnFieldThisPoint++;
			Player tempPlayer = it.next();
			PlayerWithPlayerPointID tempPlayerWithPlayerPointID = new PlayerWithPlayerPointID(tempPlayer.getName(),tempPlayer.getNumber());
			tempPlayerWithPlayerPointID.setPlayerPointID(point_id+playersOnFieldThisPoint);
			onFieldWithID.add(tempPlayerWithPlayerPointID);
		}
		redrawStatsBox();
	}
	
	public void redrawStatsBox(){
		onFieldPlayersL.removeAllViews();
		TextView tv = new TextView(this);
		ArrayList<String> statsToKeep;
		if(onDefense){
			tv.setText(R.string.defense);
			statsToKeep = this.statsToKeepDefense;
		}else{
			tv.setText(R.string.offense);
			statsToKeep = this.statsToKeepOffense;
		}
		
		onFieldPlayersL.addView(tv);

		for(Iterator<PlayerWithPlayerPointID> it = onFieldWithID.iterator();it.hasNext();){
			PlayerWithPlayerPointID tempPlayer = it.next();
			TableRow tr = new TableRow(this);
			TextView playerName = new TextView(this);
			playerName.setText(tempPlayer.toString());
			//TODO this could be dynamic
			playerName.setWidth(NAME_WIDTH);
			tr.addView(playerName);
			for(Iterator<String> stat_it = statsToKeep.iterator();stat_it.hasNext();){
				String currentStat = stat_it.next();
				Button stat = new Button(this);
				if(currentStat.length()>BUTTON_TEXT_LENGTH){
					stat.setText(currentStat.substring(0, BUTTON_TEXT_LENGTH));
				}else{
					stat.setText(currentStat);
				}
				//TODO this could be dynamic
				stat.setWidth(BUTTON_WIDTH);
				StatClickListener statListen = new StatClickListener(frisbeeData,tempPlayer.getPlayerPointID(),currentStat);
				stat.setOnClickListener(statListen);
				tr.addView(stat);
			}

			onFieldPlayersL.addView(tr);
		}
	}

	private void savePointAndPlayerPointToDB(){
		ContentValues pointValues = new ContentValues();
		pointValues.put(UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_FOR, (onDefense)?"f":"t");
		pointValues.put(UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_ID, point_id);
		pointValues.put(UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_ID,this.game_id);
		frisbeeData.insertOrThrow(UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_TN, null, pointValues);
		for(Iterator<PlayerWithPlayerPointID> it = onFieldWithID.iterator();it.hasNext();){
			ContentValues playerPointValues = new ContentValues();
			PlayerWithPlayerPointID tempPlayer = it.next();
			playerPointValues.put(UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_ID, tempPlayer.getPlayerPointID());
			playerPointValues.put(UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NAME,tempPlayer.getSQLPrimaryKey());
			playerPointValues.put(UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_ID,point_id);
			frisbeeData.insertOrThrow(UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_TN, null, playerPointValues);
			
		}
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		frisbeeData.close();
	}
}
