package UltimateFrisbee.Stats;

import java.util.ArrayList;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class playerCheckedListenerWithRedraw extends playerCheckedListener
		implements OnCheckedChangeListener {
	RosterForPoint activityToRedraw;

	playerCheckedListenerWithRedraw(ArrayList<Player> roster2, Player player,
			CheckBox checkBox, RosterForPoint rosterForPoint) {
		super(roster2, player, checkBox);
		this.activityToRedraw = rosterForPoint;
	}
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		super.onCheckedChanged(arg0,arg1);
		activityToRedraw.redrawPlayersOnField();
	}
}
