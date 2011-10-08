package UltimateFrisbee.Stats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GetTotals extends Activity {
	private frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	Cursor statCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();
//		db.execSQL("CREATE TABLE " + POINT_PLAYER_TN + "(point_player_id TIMESTAMP PRIMARY KEY, point_id INTEGER, player_name TEXT)");
//		db.execSQL("CREATE TABLE " + STATS_TN + "(stat_id TIMESTAMP PRIMARY KEY, player_point_id TIMESTAMP, stat TEXT)");

		//statCursor = frisbeeData.query(UltimateFrisbee.Stats.frisbeeOpenHelper.STATS_TN, new String[] {"stat_id" , "player_point_id", "stat"}, null, null, null, null, null);
		String query = "SELECT " + UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_TN +"." 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NAME+ ", " 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_TN +"." 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.OPPONENT_NAME+ ", "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_TN +"." 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_FOR+ ", "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_TN +"." 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_NAME+ ", "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.STATS_TN + "."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.STAT_TYPE+" FROM " 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.STATS_TN +" JOIN " 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_TN + " ON " 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_TN +"."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_ID+"=" 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.STATS_TN+"." 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_ID + " JOIN " 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_TN + " ON "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_TN + "."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_ID + "="
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_TN +"."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_ID+ " JOIN " 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_TN + " ON "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_TN + "."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_ID + "="
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_TN  +"."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_ID + " JOIN " 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_TN + " ON "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.GAME_TN + "."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_ID + "="
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_TN  +"."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_ID+ " ORDER BY "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_PLAYER_TN +"." 
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NAME + ", "
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.STATS_TN + "."
				+ UltimateFrisbee.Stats.frisbeeOpenHelper.STAT_TYPE;
		Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG, query);
		statCursor = frisbeeData.rawQuery(query, null);
		ScrollView totalsScroller = new ScrollView(this);
		TableLayout totalsListing = new TableLayout (this);
		FileWriter rosterFile = null;
		try {
			rosterFile = new FileWriter("/mnt/sdcard/Notes/stats.csv",true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter writer = null;
		writer = new BufferedWriter(rosterFile);
		try {
			writer.append(UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NAME+ ", " + UltimateFrisbee.Stats.frisbeeOpenHelper.OPPONENT_NAME + ", " + UltimateFrisbee.Stats.frisbeeOpenHelper.POINT_FOR + ", " + UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_NAME+ ", " + UltimateFrisbee.Stats.frisbeeOpenHelper.STAT_TYPE + "\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//TODO better error checking
		while(!statCursor.isLast()){
			//XXX this should be done programaticaly not using the constant "0"
			//the 0 should be rosterCursor.getColumnIndex("roster")
			statCursor.moveToNext();
			TableRow tr = new TableRow(this);
			TextView line = new TextView(this);
			line.setText(statCursor.getString(0)+ ", " + statCursor.getString(1) + ", " + statCursor.getString(2) + ", " + statCursor.getString(3)+ ", " + statCursor.getString(4));
			try {
				writer.append(statCursor.getString(0)+ ", " + statCursor.getString(1) + ", " + statCursor.getString(2) + ", " + statCursor.getString(3)+ ", " + statCursor.getString(4) + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tr.addView(line);
			totalsListing.addView(tr);
			//rosterSPAdapter.add(rosterCursor.getString(0));
		}
		statCursor.close();
		try {
			writer.close();
			rosterFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		totalsListing.setLayoutParams( new TableLayout.LayoutParams(2,2) );
		totalsListing.setPadding(1,1,1,1);
		totalsScroller.addView(totalsListing);
		super.setContentView(totalsScroller);
	}
}
