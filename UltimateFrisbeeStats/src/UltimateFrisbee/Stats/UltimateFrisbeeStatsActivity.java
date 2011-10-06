/*
    File: UltimateFrisbeeStatsActivity.java
 	Date     	 Author      Changes
   	Aug 10  2011 Sam Povilus    Created  
 */
package UltimateFrisbee.Stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract.Contacts;  
import android.provider.ContactsContract.PhoneLookup;

// TODO: Auto-generated Javadoc
/**
 * The Class UltimateFrisbeeStatsActivity.
 */
public class UltimateFrisbeeStatsActivity extends Activity {
	//TODO menu button to view current roster
		//TODO remove people from roster
	//TODO make the database not kill app on duplicate contacts
	//TODO figure out why and how continuation of process works
		//XXX use onCreate and onStart properly
		//TODO setup menu
		//TODO menu should be able to collapse windows to "new game" or to "roster setup" or app launch
	//TODO create dedicated roster setup screen
	// Tag for log messages for this app
	/** The Constant DEBUG_TAG. */
	public static final String DEBUG_TAG = "Debug Ultimate Frizbee Stats";
	/** The Constant CONTACT_PICKER_RESULT. */
	private static final int CONTACT_PICKER_RESULT = 1001;
	//public static final String GAME_LABEL = "game_label_key_for_intent"; 
	public static final String NEW_TOURNAMENT_OR_GAME_BOOL = "KEY: is this not a continuation of a tournament?";
	public static final String SELECTED_TOURNAMENT = "KEY: what tournament did the user select?";
	private static final int ADD_PLAYER_RESULT = 1002;
	private static final String NAME_TAG = "player name";
	private static final String NUMBER_TAG = "player number";
	protected static final int ADD_MANUALLY_DIALOG = 2001;
	
	/** Called when the activity is first created. */
	private Button readStatsB,addContactB,readRosterB,startGameB,continueTournamentB,addPlayerManuallyB;

	/** The edittext. */
	private EditText rosterPath, rosterFile;
	private TextView pathToCard;

	private Spinner recentTournementsSP;
	//private String gameName = "you should never see this";

	/** The m external storage available. */
	boolean mExternalStorageAvailable = false;

	/** The m external storage writable. */
	boolean mExternalStorageWriteable = false;
	
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Setup the content view to follow our main.xml file
		setContentView(R.layout.main);

		frisbeeOpenHelper = new frisbeeOpenHelper(this);
		frisbeeData = frisbeeOpenHelper.getWritableDatabase();

		//Create the buttons and fields on the main screen
		addContactB = (Button) findViewById(R.id.AddContactB);
		pathToCard = (TextView) findViewById(R.id.pathToExternalStorage);
		rosterPath = (EditText) findViewById(R.id.RosterReadPathET);
		rosterFile = (EditText)	findViewById(R.id.RosterReadFileET);
		readRosterB = (Button) findViewById(R.id.ReadRosterFromFileB);
		startGameB = (Button) findViewById(R.id.StartGameB);
		recentTournementsSP = (Spinner) findViewById(R.id.RecentTournamentsSP);
		continueTournamentB = (Button) findViewById(R.id.ContinueTournamentB);
		addPlayerManuallyB = (Button) findViewById(R.id.addPlayerManually);
		pathToCard.setText(Environment.getExternalStorageDirectory()+ "/");
		rosterPath.setText("Notes");
		rosterFile.setText("roster.csv");

		readStatsB = (Button) findViewById(R.id.Read_Stats);
		
		//populate recent tournaments spinner
		ArrayAdapter<tournamentsWithYear> recentTournamentsAdapter = new ArrayAdapter<tournamentsWithYear>(this,android.R.layout.simple_spinner_item, getRecentTournaments() );
		recentTournementsSP.setAdapter(recentTournamentsAdapter);
		
		addContactB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
						Contacts.CONTENT_URI);  
				startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
			}
		});
		
		readRosterB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				readRosterFromFile(pathToCard.getText().toString()+rosterPath.getText().toString()+"/",rosterFile.getText().toString());
			}
		});
		//XXX assure roster is not empty
		startGameB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(UltimateFrisbeeStatsActivity.this, NewGame.class);
				intent.putExtra(NEW_TOURNAMENT_OR_GAME_BOOL,true);
				startActivity(intent);

			}
		});

		continueTournamentB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				
				Intent intent = new Intent(UltimateFrisbeeStatsActivity.this, NewGame.class);
				intent.putExtra(NEW_TOURNAMENT_OR_GAME_BOOL, false);
				intent.putExtra(SELECTED_TOURNAMENT, ((tournamentsWithYear) recentTournementsSP.getSelectedItem()).getName());
				startActivity(intent);

			}
		});
		
		addPlayerManuallyB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				UltimateFrisbeeStatsActivity.this.showDialog(ADD_MANUALLY_DIALOG);
			}
		});
		
		readStatsB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){				
				Intent intent = new Intent(UltimateFrisbeeStatsActivity.this, GetTotals.class);
				startActivity(intent);
			}
		});
	}
	//LONGTERMTODO Export database to CSV in some way, look at Useful_example_code/Save_to_disk.java

	//LONGTERMTODO Make own contact chooser that allows multiple selections at once for faster roster selection


	//get the results of the contact picker and add to team list.
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */

	//TODO error checking
	//TODO get notes and parse out number
	//TODO get picture
	//TODO export names and numbers to (database)
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (resultCode == RESULT_OK) {  
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:  
				Cursor cursor = null; 
				 String contactId = "What a terrible failure";
				try {  
					Uri result = data.getData();  
					Log.v(DEBUG_TAG, "Got a contact result: "  
							+ result.toString());  
					cursor = getContentResolver().query(result,null/*new String[] {PhoneLookup.DISPLAY_NAME}*/, null,  null, null);
					cursor.moveToFirst();
					contactId = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
					Log.v(DEBUG_TAG, "Added " + contactId + "to roster");
				} catch (Exception e) {  
					Log.e(DEBUG_TAG, "Failed to get contact ID", e);  
				} finally {  
					if (cursor != null) {  
						cursor.close();  
					}
					//Roster.add(new Player(contactId));
					addNewPlayerSQL(frisbeeData,contactId,-1);
					//TODO this is probably useless
					if (contactId.length() == 0) {  
						Toast.makeText(this, "No id found for this contact",
								Toast.LENGTH_LONG).show();  
					}  

				}  
				break;
			case ADD_PLAYER_RESULT:
				addNewPlayerSQL(frisbeeData, data.getStringExtra(NAME_TAG),data.getIntExtra(NUMBER_TAG, -1));
				break;
				
			}  

		} else {  
			Log.w(DEBUG_TAG, "Warning: activity result not ok");  
		}  
	}  

	/**
	 * Read roster from file.
	 *
	 * @param path the path the file is in
	 * @param filename the filename
	 * @return the number of names read from the file
	 */
	//TODO figure out if return is nessicary
	private int readRosterFromFile(String path, String filename){
		Log.d(DEBUG_TAG, "path:" + path);
		Log.d(DEBUG_TAG, "filename:" + filename);
		File rosterFile = new File(path+filename);
		BufferedReader reader = null;
		StringBuffer contents = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(rosterFile));
			String text = null;
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				contents.append(text)
				.append(System.getProperty(
						"line.separator"));
				processLine(text);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// show file contents here
		//Log.d(DEBUG_TAG, contents.toString());
		Toast.makeText(this, "Added new contacts from csv to roster", Toast.LENGTH_SHORT).show();
		return 0;
	}
	
	 protected void processLine(String aLine){
		    //use a second Scanner to parse the content of each line 
		    Scanner scanner = new Scanner(aLine);
		    scanner.useDelimiter(",");
		    if ( scanner.hasNext() ){
		      String name = scanner.next();
		      name = stripLeadingAndTrailingQuotes(name);
		      if ( scanner.hasNext() ){
		    	  String number = scanner.next();
		    	  Log.d(DEBUG_TAG,"Name is : " + name + " and Value is : " + number );
		    	  //Roster.add(new Player(name,Integer.parseInt( number )));
		    	  addNewPlayerSQL(frisbeeData,name,Integer.parseInt(number));
		    	   }
		      else {
		    	  //Roster.add(new Player(name));
		      }
		    }
		    else {
		    	Log.d(DEBUG_TAG,"Empty or invalid line. Unable to process.");
		    }
		    //no need to call scanner.close(), since the source is a String
		  }
	 
	  private void addNewPlayerSQL(SQLiteDatabase frisbeeData2, String name,int number) {
		  //TODO confirm that the catch only catches the correct error, aka it only catches when the user attempts to instert a player that already exsists.
		  //LONGTERMTODO It seems like the below lines would be better but they do not allow one to do the timestamp well, one should come back to this and think about is though
		  ContentValues values = new ContentValues();
		  values.put(UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NAME, name);
		  values.put(UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_NUMBER, number);
		  java.util.Date today = new java.util.Date();
		  java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
		  values.put(UltimateFrisbee.Stats.frisbeeOpenHelper.PLAYER_TIME_ADDED, ts.getTime());
		  try{
			  frisbeeData.insertOrThrow(UltimateFrisbee.Stats.frisbeeOpenHelper.ROSTER_TN, null, values);
		  }catch(SQLiteConstraintException e){
			  Toast.makeText(this, "Player " + name + " already on roster.", Toast.LENGTH_SHORT).show();
		  }
	  }
	static String stripLeadingAndTrailingQuotes(String str)
	  {
	      if (str.startsWith("\""))
	      {
	          str = str.substring(1, str.length());
	      }
	      if (str.endsWith("\""))
	      {
	          str = str.substring(0, str.length() - 1);
	      }
	      return str;
	  }
	  //TODO get recent tournaments from database and send them back
	  private ArrayList<tournamentsWithYear> getRecentTournaments(){
		  ArrayList<tournamentsWithYear> noTournaments;
		  ArrayList<tournamentsWithYear> tournamentList;
		  Cursor tournamentsCursor = frisbeeData.query(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_TN, new String[] {UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_ID , UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_NAME}, null, null, null, null, UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_ID);
		  if(!tournamentsCursor.moveToFirst()){
			  noTournaments = new ArrayList<tournamentsWithYear>();
			  noTournaments.add(new tournamentsWithYear("no tournaments in database", 0));
			  return noTournaments;
		  }
		  tournamentList = new ArrayList<tournamentsWithYear>();
		  while(!tournamentsCursor.isLast()){
			  //TODOLONGTERM Date.getYear is depricated
			  //get only most recent 10 tournaments
			  Date getYear = new Date(tournamentsCursor.getLong(tournamentsCursor.getColumnIndex(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_ID)));
			  if(tournamentsCursor.getString(tournamentsCursor.getColumnIndex(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_NAME)).length()>2){
				  tournamentList.add(new tournamentsWithYear(stripLeadingAndTrailingQuotes(tournamentsCursor.getString(tournamentsCursor.getColumnIndex(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_NAME))), (getYear.getYear()+1900)));
			  }
			  tournamentsCursor.moveToNext();
		  }
		  Date getYear = new Date(tournamentsCursor.getLong(tournamentsCursor.getColumnIndex(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_ID)));
		  if(tournamentsCursor.getString(tournamentsCursor.getColumnIndex(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_NAME)).length()>2){
			  tournamentList.add(new tournamentsWithYear(stripLeadingAndTrailingQuotes(tournamentsCursor.getString(tournamentsCursor.getColumnIndex(UltimateFrisbee.Stats.frisbeeOpenHelper.TOURNAMENT_NAME))), (getYear.getYear()+1900)));
		  }
		  return tournamentList;
		  
	  }
	  
	  protected Dialog onCreateDialog(int id) {
		  final Dialog dialog = new Dialog(this);
		  switch(id) {
		  case ADD_MANUALLY_DIALOG:
			  
			  dialog.setContentView(R.layout.add_player_manually);
			  dialog.setTitle(R.string.addPlayerManually);
			  dialog.setCanceledOnTouchOutside(true);
			  dialog.show();
			  Button cancelAddPlayerDialog = (Button) dialog.findViewById(R.id.cancelAddPlayerManuallyDialog);
			  cancelAddPlayerDialog.setOnClickListener(new OnClickListener(){
				  public void onClick(View v){
					  UltimateFrisbeeStatsActivity.this.removeDialog(ADD_MANUALLY_DIALOG);				  }
			  });
			  Button addPlayerManuallyDialogB = (Button) dialog.findViewById(R.id.addPlayerManuallyDialog);
			  addPlayerManuallyDialogB.setOnClickListener(new OnClickListener(){
				  public void onClick(View v){
					  EditText playerNameET = (EditText) dialog.findViewById(R.id.playerNameAddPlayerManually);
					  EditText playerNumberET = (EditText) dialog.findViewById(R.id.playerNumberAddPlayerManually);
					  addNewPlayerSQL(frisbeeData,playerNameET.getText().toString(), Integer.parseInt(playerNumberET.getText().toString()));
					  UltimateFrisbeeStatsActivity.this.removeDialog(ADD_MANUALLY_DIALOG);
				  }
			  });

		  default:
			  //dialog = null;
		  }
		  return dialog;
	  }
}
