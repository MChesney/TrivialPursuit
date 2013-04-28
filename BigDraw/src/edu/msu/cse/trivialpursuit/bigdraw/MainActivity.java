package edu.msu.cse.trivialpursuit.bigdraw;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import java.util.List;

import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	private LocationManager locationManager = null;
    
    private double origLatitude = 0;
    private double origLongitude = 0;
    private double currLatitude = 0;
    private double currLongitude = 0;
    private static double Xpixel = 0;
    private static double Ypixel=0;
    public static Context mainActivity = null;

    
    public static double getXpixel() {
		return Xpixel;
	}

	public void setXpixel(double xpixel) {
		Xpixel = xpixel;
	}

	public static Context getContext(){
		return mainActivity;
	}
	public static double getYpixel() {
		return Ypixel;
	}

	public void setYpixel(double ypixel) {
		Ypixel = ypixel;
	}

	private SharedPreferences settings = null;
    
    public static final String PREFERENCES = "prefs";
    
    boolean exception = false;
    List<Address> locations;
    	
	/**
	 * The drawing view in this activity's view
	 */
	private DrawView drawView;
	
		
	/**
	 * The draw toggle
	 */
	private ToggleButton drawToggle; 
		
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
		// Get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);   
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private class ActiveListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			onLocation(location);
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			registerListeners();			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	};
    private ActiveListener activeListener = new ActiveListener();

	private void registerListeners() {
		unregisterListeners();
		
		// Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        
        String bestAvailable = locationManager.getBestProvider(criteria, true);
        
        if(bestAvailable != null) {
            locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);
            Location location = locationManager.getLastKnownLocation(bestAvailable);
            onLocation(location);
        }
		    
	}
	
	private void unregisterListeners() {
		locationManager.removeUpdates(activeListener);
	}
	
	private void onLocation(Location location) {
        if(location == null) {
            return;
        }
        
        if (currLatitude == 0) {
            currLatitude = location.getLatitude();
            currLongitude = location.getLongitude();
            return;
        }        
        
        currLatitude = location.getLatitude();
        currLongitude = location.getLongitude();
			 
		 Display display = getWindowManager().getDefaultDisplay();
		 Point size = new Point();
		 display.getSize(size);
		 int width = size.x;
		 int height = size.y;
		 
		 //engineering building coordinates
		 double LatA = 42.724987, LatB = 42.724228, LongX = -84.481935, LongY = -84.480143;
		 
		 // Parking lot coordinates
		 //double LatA = 42.725916, LatB = 42.72534, LongX = -84.481854, LongY = -84.480475;
		 	
		 double Z = LongY-LongX;
		 double C = LatA-LatB;
		 double J = currLongitude;
		 double K = currLatitude;
		 Xpixel = ((LongY-J)/Z)*width;
		 Ypixel = ((LatA-K)/C)*height;
		 //double Xpixel = ((180+J)*width/360), Ypixel = ((90-K)*height/180);
		 if (origLatitude != currLatitude) {
			 drawView.updateDrawing((float)Xpixel, (float)Ypixel);
		 }
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
		registerListeners();
	}
	
	public void onSend(View view) {
		ViewSender sender = new ViewSender();
		sender.sendView(this, drawView, "Trivial Pursuit");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterListeners();
		if(accelSensor != null) {
			sensorManager.unregisterListener(accelListener);
			sensorManager = null;
			accelListener = null;
			accelSensor = null;
		}
		super.onPause();
	}
}
