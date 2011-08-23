/*
    File: UltimateFrisbeeStatsActivity.java
 	Date     	 Author      Changes
   	Aug 10  2011 Sam Povilus    Created  
 */
package UltimateFrisbee.Stats;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.provider.ContactsContract.CommonDataKinds.Email;  

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
	 
 	/** The contacts spinner. */
 	private Spinner contactsSpinner;
	 
 	/** The adapter. */
 	private ArrayAdapter<CharSequence> adapter;

	  
	/** The Constant CONTACT_PICKER_RESULT. */
	private static final int CONTACT_PICKER_RESULT = 1001;  
	
	/** The m external storage available. */
	boolean mExternalStorageAvailable = false;
	
	/** The m external storage writable. */
	boolean mExternalStorageWriteable = false;
	
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
		contactsSpinner = (Spinner) findViewById(R.id.ContactsSpinner);
		pathToCard = (TextView) findViewById(R.id.pathToExternalStorage);
		rosterPath = (EditText) findViewById(R.id.rosterReadPath);
		rosterFile = (EditText)	findViewById(R.id.rosterReadFile);
		readRosterB = (Button) findViewById(R.id.readRosterFromFileButton);
		pathToCard.setText(Environment.getExternalStorageDirectory()+ "/");
		rosterPath.setText("Notes");
		rosterFile.setText("roster.csv");
		
		//setup the adapter for the contacts spinner, this is just to test getting contact info from the contacts on the phone. one of potentaly many ways to get the team input into the program
		adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactsSpinner.setAdapter(adapter);
		
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
		final int point = 0;
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
				readRosterFromFile(pathToCard.getText().toString()+rosterPath.getText().toString(),rosterFile.getText().toString());
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
	//TODO change to name from email
	//TODO get notes and parse out number
	//TODO get picture
	//TODO export names and numbers to (database, collection)
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    if (resultCode == RESULT_OK) {  
	        switch (requestCode) {  
	        case CONTACT_PICKER_RESULT:  
	            Cursor cursor = null;  
	            String email = "";  
	            try {  
	                Uri result = data.getData();  
	                Log.v(DEBUG_TAG, "Got a contact result: "  
	                        + result.toString());  
	  
	                // get the contact id from the Uri  
	                String id = result.getLastPathSegment();  
	  
	                // query for everything email  
	                cursor = getContentResolver().query(Email.CONTENT_URI,  
	                        null, Email.CONTACT_ID + "=?", new String[] { id },  
	                        null);  
	  
	                int emailIdx = cursor.getColumnIndex(Email.DATA);  
	  
	                // let's just get the first email  
	                if (cursor.moveToFirst()) {  
	                    email = cursor.getString(emailIdx);  
	                    Log.v(DEBUG_TAG, "Got email: " + email);  
	                } else {  
	                    Log.w(DEBUG_TAG, "No results");  
	                }  
	            } catch (Exception e) {  
	                Log.e(DEBUG_TAG, "Failed to get email data", e);  
	            } finally {  
	                if (cursor != null) {  
	                    cursor.close();  
	                }  
	                //EditText emailEntry = (EditText) findViewById(R.id.invite_email);  
	                //contactEmail.setText(email);  
	                Toast.makeText(this, email,  
                            Toast.LENGTH_LONG).show();
	                this.adapter.add(email);
	                this.adapter.notifyDataSetChanged();
	                this.contactsSpinner.setAdapter(adapter);
	                
	                if (email.length() == 0) {  
	                    Toast.makeText(this, "No email found for contact.",  
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
	//TODO export names and numbers to (database, collection)
	//TODO figure out if return is nessicary
	private int readRosterFromFile(String path, String filename){
		Log.d(DEBUG_TAG, "path:" + path);
		Log.d(DEBUG_TAG, "filename:" + filename);
		return 0;
		
	}
}
