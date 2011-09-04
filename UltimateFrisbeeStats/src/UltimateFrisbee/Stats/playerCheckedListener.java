package UltimateFrisbee.Stats;

import java.util.ArrayList;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class playerCheckedListener implements OnCheckedChangeListener {
	ArrayList<Player> roster;
	Player player;
	CheckBox checkBox;
	playerCheckedListener(ArrayList<Player> roster2,Player player, CheckBox checkBox){
		this.roster = (ArrayList<Player>) roster2;
		this.player = player;
		this.checkBox = checkBox;
	}
	@Override
	public synchronized  void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if(this.checkBox.isChecked()){
			this.roster.add(this.player);
		}else{
			this.roster.remove(this.player);
		}
	}

}
