package edu.msu.cse.trivialpursuit.bigdraw;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

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
	    //private Touch touch1 = new Touch();
	    
	    /**
	     * Second touch status
	     */
	    //private Touch touch2 = new Touch();

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
		 * Every time the finger moves, add a segment to the array of segments
		 */
		public void addSegments(float x, float y) {
			Segment segment = new Segment(new Point(lastX, lastY), new Point(x, y), color, thickness);
			segments.add(segment);
		}

		public boolean updateDrawing (View view, float x, float y){
			if (segments.size() == 0) {
				lastX = x;
				lastY = y;
			}
			addSegments(x, y);
    		lastX = x;
    		lastY = y;
    		view.invalidate();
    		return true;
		}
		
}
