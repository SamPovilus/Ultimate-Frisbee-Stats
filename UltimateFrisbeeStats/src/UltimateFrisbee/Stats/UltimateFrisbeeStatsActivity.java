package UltimateFrisbee.Stats;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UltimateFrisbeeStatsActivity extends Activity {
	/** Called when the activity is first created. */
	 Button ShowStausB;
	 //TextView tv = new TextView(this);

	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//tv.setText("Hello, Android");
		setContentView(R.layout.main);
		
		ShowStausB = (Button) findViewById(R.id.ShowStatusButton);
		
		
		String state = Environment.getExternalStorageState();

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
		ShowStausB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			    	if(mExternalStorageAvailable & mExternalStorageWriteable){
			    		Toast.makeText(UltimateFrisbeeStatsActivity.this,R.string.rw, Toast.LENGTH_LONG).show(); 
			    	}
			    	if(mExternalStorageAvailable){
			    		Toast.makeText(UltimateFrisbeeStatsActivity.this,R.string.read, Toast.LENGTH_LONG).show();
			    	}
			    	if(!mExternalStorageAvailable & !mExternalStorageWriteable){
			    		Toast.makeText(UltimateFrisbeeStatsActivity.this,R.string.none, Toast.LENGTH_LONG).show();
			    	}				
			}

		    

		});
	}

	@Override 
	public void onResume(){
		super.onResume();

	}
}