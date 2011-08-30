package UltimateFrisbee.Stats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatPoint extends Activity {
	private ArrayList<Player> onField;
	private ArrayList<String> statsToKeepOffense, statsToKeepDefense;
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;
	private long point_id;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setup the point id for this point
		//TODO save this point to the database
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
		
		//setup the screen
		TableLayout layout = new TableLayout (this);
		layout.setLayoutParams( new TableLayout.LayoutParams(2,2) );
		layout.setPadding(1,1,1,1);
		Bundle rosterForGameExtras = getIntent().getExtras();
		onField = rosterForGameExtras.getParcelableArrayList(RosterForPoint.ON_FIELD_KEY);
		//go through players and add them and their stat buttons to the screen
		int i = 0;
		for(Iterator<Player> it = onField.iterator();it.hasNext();){
			i++;
			//save this point_player_id to the database
			long point_player_id = point_id + i;
			Player tempPlayer = it.next();
			TableRow tr = new TableRow(this);
			TextView playerName = new TextView(this);
			playerName.setText(tempPlayer.toString());
			//TODO this is a hack to see how it works
			playerName.setWidth(150);
			tr.addView(playerName);
			for(Iterator<String> stat_it = statsToKeepOffense.iterator();stat_it.hasNext();){
				String currentStat = stat_it.next();
				Button stat = new Button(this);
				stat.setText(currentStat);
				StatClickListener statListen = new StatClickListener(frisbeeData,point_player_id,currentStat);
				stat.setOnClickListener(statListen);
				tr.addView(stat);
			}

			layout.addView(tr);
		}

		super.setContentView(layout);
	}

}
