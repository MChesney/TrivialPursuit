package edu.msu.cse.trivialpursuit.bigdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	
	/**
	 * The actual drawing
	 */
	private Drawing drawing; 
	
	/** 
	 * The crosshair image
	 */
	private Bitmap crosshair = BitmapFactory.decodeResource(getResources(), R.drawable.crosshair);
	private float crosshairX = 0;
	private float crosshairY = 0;
	
	public DrawView(Context context) {
		super(context);
		drawing = new Drawing(context, this);
	}

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		drawing = new Drawing(context, this);
	}

	public DrawView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		drawing = new Drawing(context, this);
	}
	
	/**
	 * Draw the canvas (Drawing)
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		drawing.draw(canvas);
		canvas.drawBitmap(crosshair, crosshairX, crosshairY, null);
	}
	
	/**
	 * Handle a touch event
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		crosshairX = event.getX(0) - crosshair.getWidth()/2;
		crosshairY = event.getY(0) - crosshair.getHeight()/2;
		return drawing.onTouchEvent(this, event);
	}
	
	/**
	 * Set the stroke color
	 */
	public void setColor(int color) {
		drawing.setCurrColor(color);
	}
	
	/**
	 * Set the stroke thickness
	 */
	public void setThickness(float thickness) {
		drawing.setCurrThickness(thickness);
	}
	
	/**
	 * Set if the image is editable
	 */
	public void setEditable(boolean isEditable) {
		drawing.setEditable(isEditable);
	}
	
//	/**
//	 * Load the drawing
//	 */
//	public void loadView(Bundle bundle) {
//		drawing.loadDrawing(bundle);
//	}
//	
//	/**
//	 * Save the drawing
//	 */
//	public void saveView(Bundle bundle) {
//		drawing.saveDrawing(bundle);
//	}
}