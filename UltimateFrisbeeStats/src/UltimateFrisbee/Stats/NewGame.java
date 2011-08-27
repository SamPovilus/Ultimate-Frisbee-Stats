package UltimateFrisbee.Stats;

import android.app.Activity;
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

public class NewGame extends Activity {
	private Button addPlayerB, startGameB;
	private EditText  TournamentOrLabelET,OpponentET, GameLengthET;
	private Spinner RosterSP;
	private TextView OnFieldTV;
	private Bundle extras;
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        
		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();

        //setup graphical elements
        TournamentOrLabelET = (EditText) findViewById(R.id.TournamentorLabelET);
        OpponentET = (EditText) findViewById(R.id.OpponentET);
        GameLengthET = (EditText) findViewById(R.id.GameLengthET);
        RosterSP = (Spinner) findViewById(R.id.RosterSP);
        addPlayerB = (Button) findViewById(R.id.AddPlayerB);
        OnFieldTV = (TextView) findViewById(R.id.playerList);
        startGameB = (Button) findViewById(R.id.StartGameB);
        //get bundle
        extras = getIntent().getExtras();
        if(extras != null){
        	if(!extras.getBoolean(UltimateFrisbeeStatsActivity.NEW_TOURNAMENT_OR_GAME_BOOL)){
        		TournamentOrLabelET.setKeyListener(null);
        		TournamentOrLabelET.setText(extras.getString(UltimateFrisbeeStatsActivity.SELECTED_TOURNAMENT));
        	}
        }
        //setup spinner of roster
		ArrayAdapter rosterSPAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
		Cursor rosterCursor = frisbeeData.query("roster", new String[] {"name"}, null, null, null, null, null);
		//rosterCursor.moveToFirst();
		  while(!rosterCursor.isLast()){
			  //the 0 should be rosterCursor.getColumnIndex("roster")
			  rosterCursor.moveToNext();
			  rosterSPAdapter.add(rosterCursor.getString(0));

		  }
		RosterSP.setAdapter(rosterSPAdapter);
		addPlayerB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				OnFieldTV.setText((String)RosterSP.getSelectedItem());
			}
		});
		
        
	}

}
