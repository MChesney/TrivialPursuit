package edu.msu.cse.trivialpursuit.bigdraw;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	/**
	 * The drawing view in this activity's view
	 */
	private DrawView drawView;
	
	/**
	 * The thickness bar
	 */
	private SeekBar thicknessBar;  
	
	/**
	 * The draw toggle
	 */
	private ToggleButton drawToggle; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawView = (DrawView) this.findViewById(R.id.drawView);
		
		drawToggle = (ToggleButton) findViewById(R.id.drawToggle);
		thicknessBar = (SeekBar) findViewById(R.id.thicknessBar);
			
		thicknessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				drawView.setThickness(progress + 1);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onDrawToggle(View view) {
		drawView.setEditable(!drawToggle.isChecked());
	}

}
