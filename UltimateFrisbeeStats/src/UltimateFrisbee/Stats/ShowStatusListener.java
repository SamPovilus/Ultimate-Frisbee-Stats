package UltimateFrisbee.Stats;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ShowStatusListener implements OnClickListener {
	UltimateFrisbeeStatsActivity parent;
	public ShowStatusListener(
			UltimateFrisbeeStatsActivity ultimateFrisbeeStatsActivity) {
		// TODO Auto-generated constructor stub
	}

	public void onClick(View v) {
		if(parent.mExternalStorageAvailable & parent.mExternalStorageWriteable){
			Toast.makeText(parent,R.string.rw, Toast.LENGTH_LONG).show(); 
		}
		if(parent.mExternalStorageAvailable){
			Toast.makeText(parent,R.string.read, Toast.LENGTH_LONG).show();
		}
		if(!parent.mExternalStorageAvailable & !parent.mExternalStorageWriteable){
			Toast.makeText(parent,R.string.none, Toast.LENGTH_LONG).show();
		}				
	}
}


