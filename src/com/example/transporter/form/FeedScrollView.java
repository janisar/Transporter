package com.example.transporter.form;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

public class FeedScrollView extends ScrollView{

	Context context;
	
	public FeedScrollView(Context context) {
		super(context);
		this.context = context;
	}
	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View view = (View) getChildAt(getChildCount()-1);
        int diff = (view.getBottom()-(getHeight()+getScrollY()+view.getTop()));
        if(diff == 0){  
        	
        }
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
