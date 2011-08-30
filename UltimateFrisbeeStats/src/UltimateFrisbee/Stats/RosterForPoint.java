package UltimateFrisbee.Stats;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

public class RosterForPoint extends Activity {
	//TODO all screens from this point onward should have a bar at the top showing timer and score
	//TODO add stats to database: walk through roster incriment games played, add game to game table
	public static final String ON_FIELD_KEY = "on field";
	private ArrayList<Player> Roster,onField;
	private Spinner rosterSP;
	private ArrayAdapter<Player> rosterArrayAdapter;
	private Button addPlayerB, startOffensivePoint;
	private TableLayout playersOnField;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rosterforpoint);
		Bundle rosterForGameExtras = getIntent().getExtras();
		Roster = rosterForGameExtras.getParcelableArrayList(RosterForGame.ROSTER_FOR_GAME_KEY);
		rosterArrayAdapter = new ArrayAdapter<Player>(this,android.R.layout.simple_spinner_dropdown_item, Roster);
		rosterSP = (Spinner) findViewById(R.id.rosterSP);
		rosterSP.setAdapter(rosterArrayAdapter);
		
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
					arg0.invalidate();
					Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG,"Added " + playerToAdd.toString() + " to roster" );
				}
			}
		});
		startOffensivePoint =(Button) findViewById(R.id.startOffensePoint);
		startOffensivePoint.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//TODO add game to SQL database if this is first point
				
				//add data to intent and start offensive screen
				//TODO send score to this screen
				Intent intent = new Intent(RosterForPoint.this, StatPoint.class);
				Log.d(UltimateFrisbeeStatsActivity.DEBUG_TAG, onField.toString());
				intent.putParcelableArrayListExtra(ON_FIELD_KEY,onField);
				startActivity(intent);
			}

		});
		
	}	
}
