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
import java.util.Collection;
import java.util.Scanner;

import android.app.Activity;
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
	private static final String DEBUG_TAG = "Debug Ultimate Frizbee Stats";
	/** Called when the activity is first created. */
	private Button ShowStausB,addContactB,goToOffenseB,goToDynamicB,readRosterB;

	/** The edittext. */
	private EditText rosterPath, rosterFile;
	private TextView pathToCard;


	/** The Constant CONTACT_PICKER_RESULT. */
	private static final int CONTACT_PICKER_RESULT = 1001;  

	/** The m external storage available. */
	boolean mExternalStorageAvailable = false;

	/** The m external storage writable. */
	boolean mExternalStorageWriteable = false;
	
	private Collection<Player> Roster;
	private SQLiteDatabase frisbeeData;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Setup the content view to follow our main.xml file
		setContentView(R.layout.main);


		//Create the buttons and fields on the main screen
		ShowStausB = (Button) findViewById(R.id.ShowStatusButton);
		addContactB = (Button) findViewById(R.id.AddContactButton);
		goToOffenseB = (Button) findViewById(R.id.GoToOffenseB);
		goToDynamicB = (Button) findViewById(R.id.goToDynamicButtons);
		pathToCard = (TextView) findViewById(R.id.pathToExternalStorage);
		rosterPath = (EditText) findViewById(R.id.rosterReadPath);
		rosterFile = (EditText)	findViewById(R.id.rosterReadFile);
		readRosterB = (Button) findViewById(R.id.readRosterFromFileButton);
		pathToCard.setText(Environment.getExternalStorageDirectory()+ "/");
		rosterPath.setText("Notes");
		rosterFile.setText("roster.csv");


		//Check and see what access we have to the external memory when the button is pressed
		ShowStausB.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String state = Environment.getExternalStorageState();
				//This is code to check the status of the "external memory" on the device this will usually be a SD card
				if (Environment.MEDIA_MOUNTED.equals(state)) {
					// We can read and write the media
					mExternalStorageAvailable = mExternalStorageWriteable = true;
				} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
					// We can only read the media
					mExternalStorageAvailable = true;
					mExternalStorageWriteable = false;
				} else {
					// Something else is wrong. It may be one of many other states, but all we need
					//  to know is we can neither read nor write
					mExternalStorageAvailable = mExternalStorageWriteable = false;
				}
				if(mExternalStorageAvailable & mExternalStorageWriteable){
					Toast.makeText(UltimateFrisbeeStatsActivity.this,R.string.rw, Toast.LENGTH_LONG).show(); 
				}
				if(mExternalStorageAvailable & !mExternalStorageWriteable){
					Toast.makeText(UltimateFrisbeeStatsActivity.this,R.string.read, Toast.LENGTH_LONG).show();
				}
				if(!mExternalStorageAvailable & !mExternalStorageWriteable){
					Toast.makeText(UltimateFrisbeeStatsActivity.this,R.string.none, Toast.LENGTH_LONG).show();
				}				
			}
		});


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
		//		SQLiteDatabase myDataBase;

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
					Roster.add(new Player(contactId));
					Toast.makeText(this, contactId + " added to roster",  
							Toast.LENGTH_SHORT).show();
					
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
		    	  Roster.add(new Player(name,Integer.parseInt( number )));
		      }
		      else {
		    	  Roster.add(new Player(name));
		      }
		    }
		    else {
		    	Log.d(DEBUG_TAG,"Empty or invalid line. Unable to process.");
		    }
		    //no need to call scanner.close(), since the source is a String
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
}
