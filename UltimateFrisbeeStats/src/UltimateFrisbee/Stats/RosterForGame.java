package UltimateFrisbee.Stats;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class RosterForGame extends Activity {
	public static final String ROSTER_FOR_GAME_KEY = "roster for game";
	public static final String IS_FIRST_POINT_KEY = "is first point?";
	//TODO add menu button to set all checked, set all unchecked or invers checking
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	private ArrayList<Player> Roster;
	protected Intent gameIntent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();
		Cursor rosterCursor = frisbeeData.query(UltimateFrisbee.Stats.frisbeeOpenHelper.ROSTER_TN, new String[] {UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NAME , UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NUMBER}, null, null, null, null, null);
		//LONGTERMTODO should this be a linked list?
		Roster = new ArrayList<Player>();
		ScrollView rosterScroller = new ScrollView(this);
		TableLayout rosterListing = new TableLayout (this);
        rosterListing.setLayoutParams( new TableLayout.LayoutParams(2,2) );
        rosterListing.setPadding(1,1,1,1);
		//rosterCursor.moveToFirst();
		while(!rosterCursor.isLast()){
			//XXX this should be done programaticaly not using the constant "0"
			//the 0 should be rosterCursor.getColumnIndex("roster")
			rosterCursor.moveToNext();
			Player playerToAdd = new Player(rosterCursor.getString(0), rosterCursor.getInt(1));
			Roster.add(playerToAdd);
			TableRow tr = new TableRow(this);
			CheckBox playerCB = new CheckBox(this);
			playerCB.setChecked(true);
			playerCB.setText(Roster.toArray()[Roster.toArray().length-1].toString());
			playerCheckedListener playerCheckL = new playerCheckedListener(Roster,playerToAdd,playerCB);
			playerCB.setOnCheckedChangeListener(playerCheckL);
			tr.addView(playerCB);
			rosterListing.addView(tr);
			//rosterSPAdapter.add(rosterCursor.getString(0));
		}
		rosterCursor.close();
		TableRow tr = new TableRow(this);
		Button startGameB = new Button(this);
		startGameB.setText(R.string.newPoint);
//		Button restoreGameB = new Button(this);
//		restoreGameB.setText(R.string.restoreGame);
//		tr.addView(restoreGameB);
		tr.addView(startGameB);
		rosterListing.addView(tr);
		rosterScroller.addView(rosterListing);
		super.setContentView(rosterScroller);
        final Bundle newGameExtras = getIntent().getExtras();
		startGameB.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View v) {

				//Bundle rosterBundle = new Bundle();
				//for(Iterator<Player> it = Roster.iterator(); it.hasNext();){
				//	if(it.next().)
				//}
				//TODO for each person in RosterForGame incriment games played
				gameIntent = new Intent(RosterForGame.this, RosterForPoint.class);
				gameIntent.putExtras(newGameExtras);
				gameIntent.putParcelableArrayListExtra(ROSTER_FOR_GAME_KEY, Roster);
				startActivity(gameIntent);
				Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG, Roster.toString());
			}
			
		});
//		restoreGameB.setOnClickListener(new OnClickListener(){
//			
//			@Override
//			public void onClick(View v) {
//				if(gameIntent != null){
//					gameIntent.FLAG_ACTIVITY_REORDER_TO_FRONT = true;
//				}
//			}
//			
//		});
	}
}
