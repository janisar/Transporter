package com.example.transporter.activity;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.transporter.R;
import com.example.transporter.core.User;
import com.example.transporter.form.FeedScrollView;
import com.example.transporter.form.MenuButtonsLayout;
import com.example.transporter.form.MenuView;
import com.example.transporter.service.ResultProcessor;
import com.example.transporter.web.graph.ExtraFeedsService;
import com.example.transporter.web.graph.FeedService;
import com.example.transporter.web.graph.MeFeedService;

public abstract class AbstractActivity extends Activity{
	
	protected abstract String getCommunityId();
	protected abstract String getCommunityName();
	
	private Context context;
	private ScrollView scrollView;
	private LinearLayout baseLayout;
	private RelativeLayout buttonsLayout;
	private LinearLayout scrollViewLayout;
	private LinearLayout menuView;
	private User me;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean processResult = false;
		String result = null;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
			result = new FeedService(this, getCommunityId()).execute().get();
			if (result.equals("ERROR")) {
				new AlertDialog.Builder(this).setTitle("Viga").setMessage("Logige Facebooki sisse ning proovige uuesti.").setNeutralButton("OK", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						finishActivity(100);
					}
				}).show();
				onDestroy();
			} else {
				processResult = true;
			}
		} catch (Exception e) {
			Log.e("AbstractActivity", "Can't get result from" + getCommunityId());
			e.printStackTrace();
		}
		setContentView(R.layout.base_template);
		this.context = AbstractActivity.this;

		baseLayout = (LinearLayout) findViewById(R.id.scrollLayout);
		baseLayout.setBackgroundColor(Color.parseColor("#DFDFDF"));
		buttonsLayout = (RelativeLayout) findViewById(R.id.buttons_layout);
		menuView = new MenuView(context, (LinearLayout) findViewById(R.id.menuLayout)).getMenuView();
		initScrollView();

		int width = getWindowWidth();
		initScrollViewLayout(width);

		addDefaultButtons(width);
		if (savedInstanceState != null) {
			
		} else {
			try {
				if (processResult) {
					processResult(new JSONObject(result).getJSONObject("feed"), width);
				}
			} catch (Exception e) {
				Log.e("AbstractActivity", "Can't get result from" + getCommunityId());
				e.printStackTrace();
			}
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
	
	private void processResult(JSONObject object, int width) throws JSONException, InterruptedException, ExecutionException {
		JSONArray dataArray = object.getJSONArray("data");
		JSONObject pagingObject = object.getJSONObject("paging");
		me = new MeFeedService().execute().get();
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) dataArray.get(i);
			Log.i("FEED IS ", jsonObject.toString());
			runOnUiThread(new ResultProcessor(context, scrollViewLayout, jsonObject, width, me));
		}
		addRefreshButtonToScrollViewLayout(pagingObject.getString("next"));
	}
	
	private void addRefreshButtonToScrollViewLayout(final String nextFeed) {
		final ImageView refreshButton = new ImageView(context);
		refreshButton.setImageDrawable(context.getResources().getDrawable(R.drawable.refresh_button));
		refreshButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	        	try {
	        		scrollViewLayout.removeView(refreshButton);
					String newFeed = new ExtraFeedsService().execute(nextFeed).get();
					Log.i("FEED IS ", new JSONObject(newFeed).toString());
					
					processResult(new JSONObject(newFeed), getWindowWidth());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		scrollViewLayout.addView(refreshButton);
	}
	
	private void addDefaultButtons(int width) {
		buttonsLayout.setId(1005);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		buttonsLayout.setLayoutParams(params);
		
		MenuButtonsLayout layout = new MenuButtonsLayout(context, menuView, getCommunityName());
		buttonsLayout.addView(layout);
	}
}