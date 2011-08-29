package UltimateFrisbee.Stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class RosterForGame extends Activity {
	public static final String ROSTER_FOR_GAME_KEY = "roster for game";
	public static final String IS_FIRST_POINT_KEY = "is first point?";
	//TODO add menu button to set all checked, set all unchecked or invers checking
	//TODO add tournament to SQL table
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	private ArrayList<Player> Roster;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();
		Cursor rosterCursor = frisbeeData.query("roster", new String[] {"name", "number"}, null, null, null, null, null);
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
		TableRow tr = new TableRow(this);
		Button startGameB = new Button(this);
		startGameB.setText(R.string.newPoint);
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
				//TODO add game to SQL games table
				//TODO for each person in RosterForGame incriment games played
				Intent intent = new Intent(RosterForGame.this, RosterForPoint.class);
				intent.putExtras(newGameExtras);
				intent.putParcelableArrayListExtra(ROSTER_FOR_GAME_KEY, Roster);
				startActivity(intent);
				Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG, Roster.toString());
			}
			
		});
	}
}
