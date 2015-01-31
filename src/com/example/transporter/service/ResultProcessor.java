package com.example.transporter.service;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.transporter.R;
import com.example.transporter.core.User;
import com.example.transporter.form.feed.ContentLayout;
import com.example.transporter.web.graph.ExtraFeedsService;
import com.example.transporter.web.graph.MeFeedService;

public class ResultProcessor extends Activity implements Runnable{

	private static final String FACEBOOK_PENDING_GIF = "https://fbstatic-a.akamaihd.net/rsrc.php/v2/yb/r/GsNJNwuI-UM.gif";
	
	private Context context;
	private LinearLayout scrollViewLayout;
	private JSONObject object;
	private int width;

	public ResultProcessor(Context context, LinearLayout scrollViewLayout, JSONObject object, int width) {
		this.context = context;
		this.scrollViewLayout = scrollViewLayout;
		this.object = object;
		this.width = width;
	}
	
	@Override
	public void run() {
		JSONArray dataArray = null;
		JSONObject pagingObject = null;
		String nextFeed = null;
		try {
			dataArray = object.getJSONArray("data");
			pagingObject = object.getJSONObject("paging");
			nextFeed = pagingObject.getString("next");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject jsonObject = null;
			JSONObject comments = null;
			String message = null;
			try {
				jsonObject = (JSONObject) dataArray.get(i);
				comments = getFeedComments(jsonObject);
				message =  jsonObject.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			exportFeedToView(jsonObject, comments, message);
		}
		addRefreshButtonToScrollView(nextFeed);
	
	}

	private JSONObject getFeedComments(JSONObject jsonObject) {
		try {
			return jsonObject.getJSONObject("comments");
		} catch (JSONException e) {
			//Doesn't care if feed doesn't have comments.
		}
		return null;
	}

	private void exportFeedToView(final JSONObject jsonObject, final JSONObject comments, final String message) {
		try {
			final User me = new MeFeedService().execute().get();
		
			Log.i("FEED IS ", jsonObject.toString());
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					SingleResultProcessor processor = new SingleResultProcessor(context, jsonObject, width);
					scrollViewLayout.addView(processor.getView());
					try {
						ContentLayout contentLayout = new ContentLayout(context, comments, message, me, jsonObject.getString("id"));
						contentLayout.addCommentListener(scrollViewLayout);
						processor.getView().addView(contentLayout);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void addRefreshButtonToScrollView(final String nextFeed) {
		final ImageView refreshButton = new ImageView(context);
		refreshButton.setImageDrawable(context.getResources().getDrawable(R.drawable.refresh_button));
		refreshButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	        	try {
	        		scrollViewLayout.removeView(refreshButton);
					String newFeed = new ExtraFeedsService().execute(nextFeed).get();
					Log.i("FEED IS ", new JSONObject(newFeed).toString());
					
					new Thread(new ResultProcessor(context, scrollViewLayout, new JSONObject(newFeed), width)).start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		synchronized (scrollViewLayout) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					scrollViewLayout.addView(refreshButton);
				}
			});
		}
	}

}
