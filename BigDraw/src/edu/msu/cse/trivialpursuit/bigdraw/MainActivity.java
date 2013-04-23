package edu.msu.cse.trivialpursuit.bigdraw;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class MainActivity extends Activity {
	// TODO deleted android support?
	
	/**
	 * The drawing view in this activity's view
	 */
	private DrawView drawView;
	
	private class AccelListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			float thickness = (float) (event.values[2]-.1)*10;
			
			if (thickness < .1) {
				drawView.setEditable(false);
			} else {
				drawView.setEditable(true);
				drawView.setThickness(thickness);
			}
		}
		
	}
	
	SensorManager sensorManager = null;
	private Sensor accelSensor = null;
	private AccelListener accelListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawView = (DrawView) this.findViewById(R.id.drawView);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		if(accelSensor != null) {
			sensorManager.unregisterListener(accelListener);
			sensorManager = null;
			accelListener = null;
			accelSensor = null;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(accelSensor != null) {
			accelListener = new AccelListener();
			sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}
}
