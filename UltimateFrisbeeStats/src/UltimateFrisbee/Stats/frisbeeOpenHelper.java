package UltimateFrisbee.Stats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


class frisbeeOpenHelper extends SQLiteOpenHelper {
	private static final String DB_DEBUG_TAG = "Debug Ultimate Frizbee Stats DB";
	private static final String DATABASE_NAME = "frisbee.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "table1";
	private Context context;
	frisbeeOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//example db exec 	
		//db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, name TEXT)");
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.wtf(DB_DEBUG_TAG, "THIS SHOULD NOT HAPPEN WE NEED TO FIGURE OUT UPGRADEING BEFORE DOING IT");
		Toast.makeText(context, "OH GOD SOMETHING BAD HAPPENED", Toast.LENGTH_LONG);
	}
}