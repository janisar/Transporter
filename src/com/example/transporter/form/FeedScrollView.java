package com.example.transporter.form;

import java.util.concurrent.ExecutionException;

import com.example.transporter.web.graph.ExtraFeedsService;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

public class FeedScrollView extends ScrollView{

	private Context context;
	private String nextFeed;
	
	public FeedScrollView(Context context, String nextFeed) {
		super(context);
		this.context = context;
		this.nextFeed = nextFeed;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View view = (View) getChildAt(getChildCount()-1);
        int diff = (view.getBottom()-(getHeight()+getScrollY()+view.getTop()));
        if(diff == 0){  
        	//TODO: Add new feeds to scroll view.
        	try {
				String newFeed = new ExtraFeedsService().execute(nextFeed).get();
				Toast.makeText(context, newFeed, Toast.LENGTH_SHORT).show();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
        }
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
