package com.example.transporter.activity;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transporter.R;
import com.example.transporter.core.User;
import com.example.transporter.form.FeedScrollView;
import com.example.transporter.form.MenuView;
import com.example.transporter.service.ResultProcessor;
import com.example.transporter.service.UserThread;
import com.example.transporter.web.graph.ExtraFeedsService;
import com.example.transporter.web.graph.FeedService;

public abstract class AbstractActivity extends Activity{
	
	protected abstract String getCommunityId();
	protected abstract String getCommunityName();
	
	private Context context;
	//private Session session;
	private ScrollView scrollView;
	private LinearLayout baseLayout;
	private LinearLayout buttonsLayout;
	private LinearLayout scrollViewLayout;
	private LinearLayout menuView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_template);
		this.context = AbstractActivity.this;

		baseLayout = (LinearLayout) findViewById(R.id.scrollLayout);
		baseLayout.setBackgroundColor(Color.parseColor("#DFDFDF"));
		buttonsLayout = (LinearLayout) findViewById(R.id.buttons_layout);
		menuView = new MenuView(context, (LinearLayout) findViewById(R.id.menuLayout)).getMenuView();
		initScrollView();

		int width = getWindowWidth();
		initScrollViewLayout(width);

		addDefaultButtons();
	    try {
			String result = new FeedService(getCommunityId()).execute().get();
			processResult(result);
		} catch (Exception e) {
			Log.e("AbstractActivity", "Can't get result from" + getCommunityId());
		}
	    scrollView.addView(scrollViewLayout);
		baseLayout.addView(scrollView);
	}

	private void initScrollViewLayout(int width) {
		scrollViewLayout = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		scrollViewLayout.setLayoutParams(params);
		scrollViewLayout.setOrientation(LinearLayout.VERTICAL);
		scrollViewLayout.setId(20000);
	}

	private void initScrollView() {
		scrollView = new FeedScrollView(context) {
			
			@Override
			public void onSwipeLeft() {
				LinearLayout.LayoutParams params;
				if (menuView.getWidth() > 10) {
					params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
					menuView.setLayoutParams(params);
				}
			}
			
			@Override
			public void onSwipeRight() {
				LinearLayout.LayoutParams params;
				if (menuView.getWidth() < 10) {
					params = new LinearLayout.LayoutParams(270, LayoutParams.MATCH_PARENT);
					menuView.setLayoutParams(params);
				}
			}
		};
	}
	
	@SuppressLint("NewApi")
	private int getWindowWidth() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		return width;
	}
	
	private void processResult(String result) throws JSONException {
		JSONObject object = new JSONObject(result);
		JSONArray dataArray = object.getJSONArray("data");
		JSONObject pagingObject = object.getJSONObject("paging");
		
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) dataArray.get(i);
			String message = null;
			String from = jsonObject.getJSONObject("from").getString("id");
			try {
				message = jsonObject.getString("message");
				UserThread thread = new UserThread(from);
				User user = thread.getUser();
				runOnUiThread(new ResultProcessor(context, scrollViewLayout, user, message));
			} catch (JSONException ex) {
				Log.w("FeedProcessor", "Skipping feed '" + jsonObject.getString("id") + "', no message found");
			}
		}
		addRefreshButtonToScrollViewLayout(pagingObject.getString("next"));
	}
	
	private void addRefreshButtonToScrollViewLayout(final String nextFeed) {
		ImageView refreshButton = new ImageView(context);
		refreshButton.setImageDrawable(context.getResources().getDrawable(R.drawable.refresh_button));
		refreshButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	        	try {
					String newFeed = new ExtraFeedsService().execute(nextFeed).get();
					Toast.makeText(context, newFeed, Toast.LENGTH_SHORT).show();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		scrollViewLayout.addView(refreshButton);
	}
	
	private void addDefaultButtons() {
		final ImageView menuButton = new ImageView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 10, 10, 10);
		menuButton.setMaxHeight(35);
		menuButton.setLayoutParams(params);
		menuButton.setImageDrawable(context.getResources().getDrawable(R.drawable.menu_icon));
		menuButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout.LayoutParams params;
				if (menuView.getWidth() > 10) {
					params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
					
				} else {
					params = new LinearLayout.LayoutParams(270, LayoutParams.MATCH_PARENT);
				}
				menuView.setLayoutParams(params);
			}
		});
		buttonsLayout.addView(menuButton);
		
		TextView header = new TextView(context);
		header.setText(getCommunityName());
		RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		headerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		header.setTextSize(18);
		header.setTypeface(null, Typeface.BOLD_ITALIC);
		header.setTextColor(Color.parseColor("#7D8289"));
		header.setLayoutParams(headerParams);
		buttonsLayout.addView(header);
	}
}
