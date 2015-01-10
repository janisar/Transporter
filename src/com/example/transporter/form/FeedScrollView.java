package com.example.transporter.form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class FeedScrollView extends ScrollView implements OnTouchListener {

	private Context context;
	private final GestureDetector gestureDetector;

	public FeedScrollView(Context context) {
		super(context);
		this.context = context;
		
		gestureDetector = new GestureDetector(context, new GestureListener());
		this.setOnTouchListener(this);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View view = (View) getChildAt(getChildCount()-1);
        int diff = (view.getBottom()-(getHeight()+getScrollY()+view.getTop()));
        if(diff == 0){  
        	//TODO: Add new feeds to scroll view.
        }
		super.onScrollChanged(l, t, oldl, oldt);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	

    public void onSwipeLeft() {
    	Toast.makeText(context, "LEFT", Toast.LENGTH_SHORT).show();
    }

    public void onSwipeRight() {
    	Toast.makeText(context, "RIGHT", Toast.LENGTH_SHORT).show();
    }

    private final class GestureListener extends SimpleOnGestureListener implements OnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            }
            return false;
        }
    }
	
}
