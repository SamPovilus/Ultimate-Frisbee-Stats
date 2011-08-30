package UltimateFrisbee.Stats;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;

public class StatClickListener implements OnClickListener {
	SQLiteDatabase frisbeeData;
	long point_player_id;
	String currentStat;
	public StatClickListener(SQLiteDatabase frisbeeData, long point_player_id,
			String currentStat) {
		this.frisbeeData = frisbeeData;
		this.point_player_id = point_player_id;
		this.currentStat = currentStat;
		// TODO Auto-generated constructor stub
	} 
	
	@Override
	public void onClick(View arg0) {
		java.util.Date today = new java.util.Date();
		java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
		frisbeeData.execSQL("INSERT INTO "+ frisbeeOpenHelper.STATS_TN +" VALUES ( " + ts.getTime() + "," + point_player_id +",\""+ currentStat + "\")");	
	}
}
