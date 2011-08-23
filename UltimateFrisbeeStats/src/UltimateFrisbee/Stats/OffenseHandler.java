/*
    File: OffenceHandler.java
 	Date     	 Author      Changes
   	Aug 22  2011 Sam Povilus    Created  
 */

package UltimateFrisbee.Stats;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class OffenseHandler.
 */
public class OffenseHandler extends Activity{
	
	/** The point. */
	private int point = -1;
	
	/** The b1c. */
	private Button b1a,b1b,b1c;
	
	/** The t3. */
	private TextView t1,t2,t3;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offense);
		Button b1a = (Button) findViewById(R.id.b1a);
		t1 = (TextView)findViewById(R.id.t1);
		
	}

}
