package edu.msu.cse.trivialpursuit.bigdraw;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
	
	/**
	 * The media recorder to keep track of volume
	 */
	MediaRecorder recorder;
	
	/**
	 * Amplitude of microphone input
	 */
	int amp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawView = (DrawView) this.findViewById(R.id.drawView);
		recorder = new MediaRecorder();
		setUpMediaRecorder();
		Thread thread = new Thread(new Runnable(){

			runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	                risultato.setText("" + recorder.getMaxAmplitude());
	            }
	        });
			
		}
		);
		thread.start();
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

	public void setUpMediaRecorder()
	{
		 recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		 recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		 recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		 recorder.setOutputFile("/dev/null");
		 try {
			recorder.prepare();
	 	 } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	     recorder.start();   // Recording is now started
	}
	
	public void unsetMediaRecorder()
	{
		 recorder.stop();
		 recorder.reset();
  // You can reuse the object by going back to setAudioSource() step
		 recorder.release(); // Now the object cannot be reused
		 
	}
}
