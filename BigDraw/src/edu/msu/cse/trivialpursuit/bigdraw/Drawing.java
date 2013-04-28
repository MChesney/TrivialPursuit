package edu.msu.cse.trivialpursuit.bigdraw;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class Drawing {

		// Strings for saving/loading the drawing
		public static final String DRAWING = "drawing";
		public static final String PARAMETERS = "parameters";
		public static final String COLORS = "colors";
		public static final String THICKNESSES = "thicknesses";
		public static final String START_POINTS = "start_points";
		public static final String END_POINTS = "end_points";
		public static final String EDITABLE = "editable";
		
		/**
		 * The class for a single line segment
		 * Connected by two points, with color and thickness (stroke width)
		 */
	    private class Segment {
	        /**
	         * Color of segment
	         */
	        private int color = Color.BLACK;

			/**
	         * Thickness of segment
	         */
	        private float thickness = 10; 
	        
	        /**
	         * Last point of segment
	         */
	        private Point lastPoint = null;
	        
	        
	        /**
	         * Current point of segment
	         */
	        private Point currPoint = null;
	        
			public Point getLastPoint() {
				return lastPoint;
			}

			public Point getCurrPoint() {
				return currPoint;
			}   
			
			public int getColor() {
				return color;
			}
			
			public float getThickness() {
				return thickness;
			}
			
			/**
			 * Constructor for segment
			 */
			public Segment(Point lastPoint, Point currPoint, int color, float thickness) {
				this.lastPoint = new Point(lastPoint);
				this.currPoint = new Point(currPoint);
				this.color = color;
				this.thickness = thickness;
			}
	           
	    }
	    
	    /**
	     * Local class to handle the touch status for one touch.
	     * We will have one object of this type for each of the 
	     * two possible touches.
	     */
	    private class Touch {
	        /**
	         * Touch id
	         */
	        public int id = -1;
	        
	        /**
	         * Current touch location
	         */
	        public Point currTouch = new Point(0,0);       
	        
	        /**
	         * Previous touch location
	         */
	        public Point lastTouch = new Point(0,0);
	        
	        /**
	         * Change in values from previous
	         */
	        public Point deltas = new Point(0,0);       
	        
	        /**
	         * Copy the current values to the previous values
	         */
	        public void copyToLast() {
	            lastTouch.x = currTouch.x;
	            lastTouch.y = currTouch.y;
	        }
	        
	        /**
	         * Compute the values of dX and dY
	         */
	        public void computeDeltas() {
	            deltas.x = currTouch.x - lastTouch.x;
	            deltas.y = currTouch.y - lastTouch.y;
	        }
	    }
	    
	    private class Point {
	    	/** 
	    	 * x-value of point
	    	 */
	    	public float x = 0;
	    	
	    	/**
	    	 * y-value of point
	    	 */
	    	public float y = 0;
	    	
	    	/**
	    	 * Constructor for Point with two integers
	    	 */
	    	public Point(float x, float y) {
	    		
	    		this.x = x;
	    		this.y = y;
	    	}
	    	
	    	/**
	    	 * Constructor for Point with Point (essentially copy constructor)
	    	 */
	    	public Point(Point point) {
	    		this.x = point.x;
	    		this.y = point.y;
	    	}
	    }
	    
	    /**
	     * Array of segments that make up the drawing
	     */
	    private ArrayList<Segment> segments = new ArrayList<Segment>();
	    /**
	     * Most recent X touch when dragging
	     */
	    private float lastX;
	    
	    /**
	     * Most recent Y touch when dragging
	     */
	    private float lastY;
	    
	    /**
	     * The current paint
	     */
	    private Paint paint;
	    
	    /**
	     * The current thickness
	     */
	    private float thickness = 10;
	    
	    /**
	     * The current color
	     */
	    private int color = Color.BLACK;
	    
	    /**
	     * Whether or not drawing is editable
	     */
	    private boolean editable = true;
	    
	    /**
	     * First touch status
	     */
	    private Touch touch1 = new Touch();
	    
	    /**
	     * Second touch status
	     */
	    private Touch touch2 = new Touch();
	    
	    /**
	     * Translate X
	     */
	   private float translateX = 0;
	   
	   /**
	     * Translate Y
	     */
	    private float translateY = 0;
	    
	   /**
	    * Angle of rotation (for color)
	    */
	    private float angle = 0;
	    
		/**
	     * The drawing view in this activity's view
	     */
	    private DrawView drawView;
		
		/**
		 * Constructor for Drawing
		 * Initializes paint color and thickness
		 */
		public Drawing(Context context, DrawView drawView) {
			this.drawView = drawView;
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(color);
			paint.setStrokeWidth(thickness);
		}
		
	    public int getCurrColor() {
			return color;
		}

		public void setCurrColor(int currColor) {
			color = currColor;
			paint.setColor(currColor);
		}

		public float getCurrThickness() {
			return thickness;
		}

		public void setCurrThickness(float currThickness) {
			thickness = currThickness;
			paint.setStrokeWidth(currThickness);
		}
		
		public void setEditable(boolean isEditable) {
			editable = isEditable;
		}

		/**
		 * Draw the drawing by iterating through the array of segments
		 */
		public void draw(Canvas canvas) {
			canvas.save();
		
			float prevX, prevY, currX, currY;
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			for (Segment segment : segments) {
				prevX = segment.getLastPoint().x;
				prevY = segment.getLastPoint().y;
				currX = segment.getCurrPoint().x;
				currY = segment.getCurrPoint().y;
				paint.setColor(segment.getColor());
				paint.setStrokeWidth(segment.getThickness());
				canvas.drawLine(prevX, prevY, currX, currY, paint);
				canvas.drawCircle(currX, currY, segment.getThickness()/2, paint);
			}
			canvas.restore();
		}
		
		/**
		 * Handle a touch event from the view.
		 * @param view The view that is the source of the touch
		 * @param event The motion event describing the touch
		 * @return true if the touch is handled
		 */
		public boolean onTouchEvent(View view, MotionEvent event) {
			if(editable) {
				float x = event.getX();
				float y = event.getY();
				int id = event.getPointerId(event.getActionIndex());
		        switch(event.getActionMasked()) {
		        case MotionEvent.ACTION_DOWN:
		            touch1.id = id;
		            touch2.id = -1;
		            getPositions(event);
		        	touch1.copyToLast();
		            return true;
		            
		        case MotionEvent.ACTION_POINTER_DOWN:
		            if(touch1.id >= 0 && touch2.id < 0) {
		                touch2.id = id;
		                getPositions(event);
		                touch2.copyToLast();
		                return true;
		            }
		            break;
		            
		        case MotionEvent.ACTION_UP:
		        case MotionEvent.ACTION_CANCEL:
		            touch1.id = -1;
		            touch2.id = -1;
		            drawView.invalidate();
		            return true;
		            
		        case MotionEvent.ACTION_POINTER_UP:
		            if(id == touch2.id) {
		                touch2.id = -1;
		            } else if(id == touch1.id) {
		                // Make what was touch2 now be touch1 by 
		                // swapping the objects.
		                Touch t = touch1;
		                touch1 = touch2;
		                touch2 = t;
		                touch2.id = -1;
		            }
		            drawView.invalidate();
		            return true;
		            
		        case MotionEvent.ACTION_MOVE:
		        	lastX = x;
	        		lastY = y;
	                getPositions(event);
	        		move();
	        		drawView.invalidate();		            
		            return true;
		        }
			}
			return false;
		}
		
		/**
		 * Every time the finger moves, add a segment to the array of segments
		 */
		public void addSegments(float x, float y) {
			Segment segment = new Segment(new Point(lastX, lastY), new Point(x, y), color, thickness);
			segments.add(segment);
		}
		
	    /**
	     * Handle movement of the touches
	     */
	    private void move() {
	        // If no touch1, we have nothing to do
	        // This should not happen, but it never hurts
	        // to check.
	        if(touch1.id < 0 || touch2.id < 0) { 
	            return;
	        }
/*	        
	        if(touch1.id >= 0) {
	            // At least one touch
	            // We are moving
	            touch1.computeDeltas();
/*	            
	            translateX += touch1.deltas.x;
	            translateY += touch1.deltas.y;
	        }
	    */
	        
	        if(touch2.id >= 0) {
	            // Two touches
	            
	            /*
	             * Rotation
	             */
	            float angle1 = angle(touch1.lastTouch.x, touch1.lastTouch.y, touch2.lastTouch.x, touch2.lastTouch.y);
	            float angle2 = angle(touch1.currTouch.x, touch1.currTouch.y, touch2.currTouch.x, touch2.currTouch.y);
	            float da = angle2 - angle1;
		        angle += da;
	            color = computeColor(angle);
	            setCurrColor(color);
	        }
	    }
	    
	    /**
	     * Compute the new color based on the angle
	     */
	    public int computeColor(float angle)
	    {
	        if(angle<90 && angle > 0)
	        {
	        	color = Color.GREEN;
	        }
	        else if(angle > 90&& angle < 180)
	        {
	        	color = Color.BLUE;
	        }
	        else if(angle >180 && angle < 270)
	        {
	        	color = Color.RED;
	        }
	        else
	        {
	        	color = Color.GRAY;
	        }
	        return color;
	    }
	    
	    /**
	     * Rotate the image around the point x1, y1
	     * @param dAngle Angle to rotate in degrees
	     * @param x1 rotation point x
	     * @param y1 rotation point y
	     */
/*	    public void rotate(float dAngle, float x1, float y1) {

	        
	        // Compute the radians angle
	        double rAngle = Math.toRadians(dAngle);
	        float ca = (float) Math.cos(rAngle);
	        float sa = (float) Math.sin(rAngle);
	        
	        float xp = (translateX - x1) * ca - (translateY - y1) * sa + x1;
	        float yp = (translateX - x1) * sa + (translateY - y1) * ca + y1;

	        translateX = xp;
	        translateY = yp;
	    }*/
	    
	    /**
	     * Determine the angle for two touches
	     * @param x1 Touch 1 x
	     * @param y1 Touch 1 y
	     * @param x2 Touch 2 x
	     * @param y2 Touch 2 y
	     * @return computed angle in degrees
	     */
	    private float angle(float x1, float y1, float x2, float y2) {
	        float dx = x2 - x1;
	        float dy = y2 - y1;
	        return (float) Math.toDegrees(Math.atan2(dy, dx));
	    }
	    
	    /**
	     * Get the positions for the two touches and put them
	     * into the appropriate touch objects.
	     * @param event the motion event
	     */
	    private void getPositions(MotionEvent event) {
	        for(int i=0;  i<event.getPointerCount();  i++) {
	            
	            // Get the pointer id
	            int id = event.getPointerId(i);
	            
	            // Convert to image coordinates
	            float x = event.getX(i); 
	            float y = event.getY(i);
	            
	            
	            if(id == touch1.id) {
	            	touch1.copyToLast();
	                touch1.currTouch.x = x;
	                touch1.currTouch.y = y;
	            } else if(id == touch2.id) {
	            	touch2.copyToLast();
	                touch2.currTouch.x = x;
	                touch2.currTouch.y = y;
	            }
	        }
	        
	        drawView.invalidate();
	    }
//		/**
//		 * Load the drawing 
//		 */
//		public void loadDrawing(Bundle bundle) {
//			// Load the drawing parameters
//			params = (Parameters) bundle.getSerializable(PARAMETERS);
//			isEditable = bundle.getBoolean(EDITABLE);
//			
//			// Load the drawing segments
//			int [] colors = bundle.getIntArray(COLORS);
//			float [] thicknesses = bundle.getFloatArray(THICKNESSES);
//			float [] startPoints = bundle.getFloatArray(START_POINTS);
//			float [] endPoints = bundle.getFloatArray(END_POINTS);
//			
//			for (int i = 0; i < colors.length; i++) {
//				Point prevPoint = new Point(startPoints[i*2], startPoints[i*2+1]);
//				Point currPoint = new Point(endPoints[i*2], endPoints[i*2+1]);
//				Segment segment = new Segment(prevPoint, currPoint, colors[i], thicknesses[i]);
//				segments.add(segment);
//			}
//
//		}
//		
//		/**
//		 * Save the drawing
//		 */
//		public void saveDrawing(Bundle bundle) {
//			// Save the drawing parameters
//			bundle.putSerializable(PARAMETERS, params);
//			bundle.putBoolean(EDITABLE, isEditable);
//			
//			// Save the drawing segments
//			int [] colors = new int[segments.size()];
//			float [] thicknesses = new float[segments.size()];
//			float [] startPoints = new float[segments.size()*2];
//			float [] endPoints = new float[segments.size()*2];
//			
//			for (int i=0; i < segments.size(); i++) {
//				Segment segment = segments.get(i);
//				colors[i] = segment.getColor();
//				thicknesses[i] = segment.getThickness();
//				startPoints[i*2] = segment.getLastPoint().x;
//				startPoints[i*2+1] = segment.getLastPoint().y;
//				endPoints[i*2] = segment.getCurrPoint().x;
//				endPoints[i*2+1] = segment.getCurrPoint().y;
//			}
//			
//			bundle.putIntArray(COLORS, colors);
//			bundle.putFloatArray(THICKNESSES, thicknesses);
//			bundle.putFloatArray(START_POINTS, startPoints);
//			bundle.putFloatArray(END_POINTS, endPoints);
//		}

		public boolean updateDrawing (View view, float x, float y){
			addSegments(x, y);
    		lastX = x;
    		lastY = y;
    		view.invalidate();
    		return true;
		}
		
}
