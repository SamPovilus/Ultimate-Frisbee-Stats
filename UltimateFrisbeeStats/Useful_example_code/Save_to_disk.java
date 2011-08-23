import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import UltimateFrisbee.Stats.R;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

SaveB = (Button) findViewById(R.id.SaveButton);
edittext = (EditText) findViewById(R.id.SaveText);



//Save a text file with the text from the text box to disk as foo.txt
SaveB.setOnClickListener(new OnClickListener(){
	public void onClick(View v){
		generateNoteOnSD("foo.txt",edittext.getText().toString());
	}
});

/**
	 * Generate note on sd.
	 *
	 * @param sFileName the s file name
	 * @param sBody the s body
	 */
	public void generateNoteOnSD(String sFileName, String sBody){
	    try
	    {
	        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        File gpxfile = new File(root, sFileName);
	        FileWriter writer = new FileWriter(gpxfile);
	        writer.append(sBody);
	        writer.flush();
	        writer.close();
	        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
	    }
	    catch(IOException e)
	    {
	    	Toast.makeText(this, "Couldn't Save", Toast.LENGTH_SHORT).show();
	         e.printStackTrace();
	         Log.e(TAG, "No write");
	         //importError = e.getMessage();
	         //iError();
	    }
	}