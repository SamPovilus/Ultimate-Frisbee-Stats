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
import java.util.Calendar;
import java.util.Collection;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;  
import android.provider.ContactsContract.CommonDataKinds.Email;  
import android.provider.ContactsContract.PhoneLookup;

// TODO: Auto-generated Javadoc
/**
 * The Class UltimateFrisbeeStatsActivity.
 */
public class UltimateFrisbeeStatsActivity extends Activity {
	//TODO Make the database
	//TODO make the database not duplicate contacts
	// Tag for log messages for this app
	/** The Constant DEBUG_TAG. */
	public static final String DEBUG_TAG = "Debug Ultimate Frizbee Stats";
	/** The Constant CONTACT_PICKER_RESULT. */
	private static final int CONTACT_PICKER_RESULT = 1001;
	//public static final String GAME_LABEL = "game_label_key_for_intent"; 
	public static final String NEW_TOURNAMENT_OR_GAME_BOOL = "KEY: is this not a continuation of a tournament?";
	public static final String SELECTED_TOURNAMENT = "KEY: what tournament did the user select?";

	/** Called when the activity is first created. */
	private Button addContactB,goToOffenseB,goToDynamicB,readRosterB,startGameB,startTournamentB,continueTournamentB;

	/** The edittext. */
	private EditText rosterPath, rosterFile;
	private TextView pathToCard;

	private Spinner recentTournementsSP;
	private String gameName = "you should never see this";

	/** The m external storage available. */
	boolean mExternalStorageAvailable = false;

	/** The m external storage writable. */
	boolean mExternalStorageWriteable = false;
	
	private Collection<Player> Roster;
	public frisbeeOpenHelper frisbeeOpenHelper;
	private SQLiteDatabase frisbeeData;

	private Calendar calendar;
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
		goToOffenseB = (Button) findViewById(R.id.GoToOffenseB);
		goToDynamicB = (Button) findViewById(R.id.GoToDynamicButtonsB);
		pathToCard = (TextView) findViewById(R.id.pathToExternalStorage);
		rosterPath = (EditText) findViewById(R.id.RosterReadPathET);
		rosterFile = (EditText)	findViewById(R.id.RosterReadFileET);
		readRosterB = (Button) findViewById(R.id.ReadRosterFromFileB);
		startGameB = (Button) findViewById(R.id.StartGameB);
		recentTournementsSP = (Spinner) findViewById(R.id.RecentTournamentsSP);
		continueTournamentB = (Button) findViewById(R.id.ContinueTournamentB);
		pathToCard.setText(Environment.getExternalStorageDirectory()+ "/");
		rosterPath.setText("Notes");
		rosterFile.setText("roster.csv");
		
		//create calendar for timestamps
		calendar = Calendar.getInstance(); 
		
		//populate recent tournaments spinner
		ArrayAdapter recentTournamentsAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, getRecentTournaments() );
		recentTournementsSP.setAdapter(recentTournamentsAdapter);
		
		addContactB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				doLaunchContactPicker();
			}
		});
		goToOffenseB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){               
				Intent intent = new Intent(UltimateFrisbeeStatsActivity.this, OffenseHandler.class);
				//Probably put a link to the database in here and mabey the active players?
				//intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		goToDynamicB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){               
				Intent intent = new Intent(UltimateFrisbeeStatsActivity.this, DynamicButtons.class);
				startActivity(intent);
			}
		});
		readRosterB.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				readRosterFromFile(pathToCard.getText().toString()+rosterPath.getText().toString()+"/",rosterFile.getText().toString());
			}
		});
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
				intent.putExtra(SELECTED_TOURNAMENT, "return from other activity");
				startActivity(intent);

			}
		});
	}
	//LONGTERMTODO Export database to CSV in some way, look at Useful_example_code/Save_to_disk.java

	//Launch the contact chooser
	//LONGTERMTODO Make own contact chooser that allows multiple selections at once for faster roster selection
	/**
	 * Do launch contact picker.
	 */
	public void doLaunchContactPicker() {  
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
				Contacts.CONTENT_URI);  
		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
	}  

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
					Toast.makeText(this, contactId + " added to roster",  
							Toast.LENGTH_SHORT).show();
					addNewPlayerSQL(frisbeeData,contactId,-1);
					//TODO this is probably useless
					if (contactId.length() == 0) {  
						Toast.makeText(this, "No id found for this contact",
								Toast.LENGTH_LONG).show();  
					}  

				}  

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
	//TODO get file open
	//TODO Parse names and numbers
	//TODO export names and numbers to (database)
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
		  //LONGTERMTODO It seems like the below lines would be better but they do not allow one to do the timestamp well, one should come back to this and think about is though
//		  ContentValues values = new ContentValues();
//		  values.put("name", name);
//		  values.put("number", number);
//		  values.put("id", new java.sql.Timestamp(new java.util.Date().getTime()));
//		  frisbeeData2.insertOrThrow(frisbeeOpenHelper.ROSTER_TN, null, values);
		  java.util.Date today = new java.util.Date();
		  java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
		  frisbeeData.execSQL("INSERT INTO roster VALUES ( \"" + name + "\"," + number +","+ ts.getTime() + ",0, 0, 0 )");
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
	  private String [] getRecentTournaments(){
		  String[] oneDimArray = { "abc","def","xyz" };
		  return oneDimArray;
		  
	  }
	  
}
