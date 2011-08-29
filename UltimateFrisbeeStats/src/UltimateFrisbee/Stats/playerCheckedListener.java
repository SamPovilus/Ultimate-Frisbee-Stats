package UltimateFrisbee.Stats;

import java.util.Collection;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class playerCheckedListener implements OnCheckedChangeListener {
	Collection<Player> roster;
	Player player;
	CheckBox checkBox;
	playerCheckedListener(Collection<Player> roster,Player player, CheckBox checkBox){
		this.roster = roster;
		this.player = player;
		this.checkBox = checkBox;
	}
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if(this.checkBox.isChecked()){
			this.roster.add(this.player);
		}else{
			this.roster.remove(this.player);
		}
	}

}
