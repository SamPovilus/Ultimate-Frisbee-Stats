/*
    File: DynamicButtons.java
 	Date     	 Author      Changes
   	Aug 22  2011 Sam Povilus    Created  
 */

package UltimateFrisbee.Stats;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

// TODO: Auto-generated Javadoc
/**
 * The Class DynamicButtons.
 */
public class DynamicButtons extends Activity {

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TableLayout layout = new TableLayout (this);
        layout.setLayoutParams( new TableLayout.LayoutParams(2,2) );

        layout.setPadding(1,1,1,1);

        for (int f=0; f<=20	; f++) {
            TableRow tr = new TableRow(this);
            for (int c=0; c<=9; c++) {
                Button b = new Button (this);
                b.setText(""+f+c);
                b.setTextSize(10.0f);
                b.setTextColor(Color.rgb( 0, 200, 0));
                //b.setOnClickListener(this);
                tr.addView(b, 60,60);
            } // for
            layout.addView(tr);
        } // for

        super.setContentView(layout);
    } // ()

    /**
     * On click.
     *
     * @param view the view
     */
    public void onClick(View view) {
        ((Button) view).setText("*");
        ((Button) view).setEnabled(false);
    }
} // class

