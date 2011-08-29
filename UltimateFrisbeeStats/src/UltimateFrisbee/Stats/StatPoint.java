package UltimateFrisbee.Stats;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatPoint extends Activity {
	private ArrayList<Player> onField;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TableLayout layout = new TableLayout (this);
		layout.setLayoutParams( new TableLayout.LayoutParams(2,2) );
		layout.setPadding(1,1,1,1);
		Bundle rosterForGameExtras = getIntent().getExtras();
		onField = rosterForGameExtras.getParcelableArrayList(RosterForGame.ROSTER_FOR_GAME_KEY);
		
		
		for(Iterator<Player> it = onField.iterator();it.hasNext();){
			Player tempPlayer = it.next();
			TableRow tr = new TableRow(this);
			TextView playerName = new TextView(this);
			playerName.setText(tempPlayer.toString());
			//TODO this is a hack to see how it works
			playerName.setWidth(150);
			Button drops = new Button(this);
			drops.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO add sql statment here
					
				}
				
			});
			drops.setText("drops");
			tr.addView(playerName);
			tr.addView(drops);
			layout.addView(tr);
		}
		
		super.setContentView(layout);
	}

}
