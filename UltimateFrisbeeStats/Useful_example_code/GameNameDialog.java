package UltimateFrisbee.Stats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Button;

public class GameNameDialog extends Activity {
	private EditText et;

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_name_dialog);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		// title
		try {
			String s = getIntent().getExtras().getString("title");
			if (s.length() > 0) {
				this.setTitle(s);
			}
		} catch (Exception e) {
		}
		// value

		try {
			et = ((EditText) findViewById(R.id.gameNameET));
			et.setText(getIntent().getExtras().getString("value"));
		} catch (Exception e) {
		}
		// button
		((Button) findViewById(R.id.GameNameOKB)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				executeDone();
			}

		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		executeDone();
		super.onBackPressed();
	}

	/**
	 *
	 */
	private void executeDone() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra("value", GameNameDialog.this.et.getText().toString());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}