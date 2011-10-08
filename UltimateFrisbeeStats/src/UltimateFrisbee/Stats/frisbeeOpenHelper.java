package UltimateFrisbee.Stats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


class frisbeeOpenHelper extends SQLiteOpenHelper {
	private static final String DB_DEBUG_TAG = "Debug Ultimate Frizbee Stats DB";
	private static final String DATABASE_NAME = "frisbee.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TOURNAMENT_TN = "tournament";
	public static final String ROSTER_TN = "roster";
	public static final String GAME_ROSTER_TN = "game_roster";
	public static final String POINT_TN = "point";
	public static final String GAME_TN = "game";
	public static final String OPPONENTS_TN = "opponents";
	public static final String POINT_PLAYER_TN = "pointplayer";
	public static final String STATS_TN = "stats";
	
	public static final String TOURNAMENT_ID = "Tournament_Date";
	public static final String TOURNAMENT_NAME = "Tournament_Name";
	public static final String POINT_ID = "Point_Time";
	public static final String POINT_FOR = "Point_For";
	public static final String GAME_ID = "Game_Time";
	public static final String PLAYER_NAME = "Player_Name";
	public static final String PLAYER_NUMBER = "Player_Number";
	public static final String PLAYER_TIME_ADDED = "Player_Time_Added";
	public static final String PLAYER_POINTS_PLAYED = "Player_Points_Played";
	public static final String PLAYER_GAMES_PLAYED = "Player_Games_Played";
	public static final String PLAYER_TOURNAMENTS_PLAYED = "Player_Tournaments_Played";
	//public static final String GAME_TIME_STARTED = "Game_Time_Started";
	public static final String GAME_TIME_ENDED = "Game_Time_Ended";
	public static final String OPPONENT_GAMES_WON_AGAINST = "Games_Won_Against";
	public static final String OPPONENT_NAME = "Opponent_Name";
	public static final String POINT_PLAYER_ID = "Player_Point_ID";
	public static final String STAT_ID = "Stat_ID";
	public static final String GAME_ROSTER_ID = "Game_Roster_ID";
	public static final String GAME_OUR_SCORE = "Our_Score";
	public static final String OPPONENT_GAMES_LOST_AGAINST = "Games_Lost_Against";
	public static final String STAT_TYPE = "Stat_Type";
	public static final String GAME_THEIR_SCORE = "Thier_Score";
	
	
	private Context context;
	frisbeeOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TOURNAMENT_TN + "(" + TOURNAMENT_ID + " TIMESTAMP PRIMARY KEY, " + TOURNAMENT_NAME + " TEXT)");
		db.execSQL("CREATE TABLE " + ROSTER_TN + "(" + PLAYER_NAME + " TEXT PRIMARY KEY, " + PLAYER_NUMBER +" INTEGER, " + PLAYER_TIME_ADDED + " TIMESTAMP, " + PLAYER_POINTS_PLAYED + " INTEGER, " +PLAYER_GAMES_PLAYED+ " INTEGER, " + PLAYER_TOURNAMENTS_PLAYED + " INTEGER)");
		db.execSQL("CREATE TABLE " + POINT_TN + "(" + POINT_ID + " TIMESTAMP PRIMARY KEY, " +POINT_FOR + " TEXT(1), "+ GAME_ID + " TIMESTAMP)");
		db.execSQL("CREATE TABLE " + GAME_TN + "("+GAME_ID+" TIMESTAMP PRIMARY KEY, "+ GAME_TIME_ENDED +" TIMESTAMP, "+ OPPONENT_NAME +" TEXT, "+ GAME_OUR_SCORE +" INTEGER, " +GAME_THEIR_SCORE+" INTEGER, "+TOURNAMENT_ID+" TIMESTAMP)");
		db.execSQL("CREATE TABLE " + OPPONENTS_TN + "(" + OPPONENT_NAME +" TEXT PRIMARY KEY, "+ OPPONENT_GAMES_WON_AGAINST +" INTEGER, "+ OPPONENT_GAMES_LOST_AGAINST + " INTEGER)");
		db.execSQL("CREATE TABLE " + POINT_PLAYER_TN + "("+POINT_PLAYER_ID+" TIMESTAMP PRIMARY KEY, "+POINT_ID+" INTEGER, "+PLAYER_NAME+" TEXT)");
		db.execSQL("CREATE TABLE " + STATS_TN + "(" + STAT_ID +" TIMESTAMP PRIMARY KEY, "+ POINT_PLAYER_ID +" TIMESTAMP, "+STAT_TYPE+" TEXT)");
		db.execSQL("CREATE TABLE " + GAME_ROSTER_TN + "(" + GAME_ROSTER_ID +" TIMESTAMP PRIMARY KEY, "+ PLAYER_NAME +" TEXT, "+GAME_ID+" TIMESTAMP)");
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.wtf(DB_DEBUG_TAG, "THIS SHOULD NOT HAPPEN WE NEED TO FIGURE OUT UPGRADEING BEFORE DOING IT");
		Toast.makeText(context, "OH GOD SOMETHING BAD HAPPENED", Toast.LENGTH_LONG);
	}
}