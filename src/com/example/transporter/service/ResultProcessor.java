package com.example.transporter.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.transporter.R;
import com.example.transporter.core.User;
import com.example.transporter.form.MessageTextView;

public class ResultProcessor extends Activity implements Runnable {
	
	private Context context;
	private LinearLayout scrollViewLayout;
	private JSONObject object;
	private String message;
	private User user;
	private String regexMather1 = "(.*[Kk][uU][Rr](e)?(ssaare)?([->\\s]*)[Tt](a)?[Ll](lin)?[Nn].*)|(.*Kuressaarest.*)|(.*[Tt](a)?llinnasse.*)";
	private String url;
	
	public ResultProcessor(Context context, LinearLayout scrollViewLayout, JSONObject jsonObject) {
		this.context = context;
		this.scrollViewLayout = scrollViewLayout;
		this.object = jsonObject;
		generateUser();
	}

	private void generateUser() {
		JSONObject from = null;
		try {
			from = object.getJSONObject("from");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.user = new UserThread(from).getUser();
	}

	@Override
	public void run() {
		String newline = System.getProperty("line.separator");
		JSONObject comments = null;
		try {
			message = object.getString("message");
			url = ((JSONObject)object.getJSONArray("actions").get(0)).getString("link");
			try {
				comments = object.getJSONObject("comments");
			} catch (JSONException ex) {
				//ignore, don't care
			}
			String[] splitted = message.split(newline);
			addToScrollView(comments);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean matchesPattern(String[] splitted) {
		boolean result = false;
		for (String s : splitted) {
			if (s.matches(regexMather1) && !s.contains("?")) {
				result = true;
			}
		}
		return result;
	}

	private void addToScrollView(JSONObject comments) throws JSONException {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 10, 20, 0);
		layout.setLayoutParams(params);
		LinearLayout l = getHeader();
		layout.addView(l);
		layout.addView(getContentLayout(comments));
		
		scrollViewLayout.addView(layout);
	}

	
	private LinearLayout getContentLayout(JSONObject comments) throws JSONException {
		LinearLayout vertical = new LinearLayout(context);
		vertical.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		vertical.setLayoutParams(params);
		RelativeLayout contentHead = getContentHeadLayout();
		LinearLayout result = getMessageLayout();
		RelativeLayout footer = getFooterLayout(comments);
		vertical.addView(contentHead);
		vertical.addView(result);
		vertical.addView(footer);
		return vertical;
	}

	@SuppressLint("NewApi")
	private RelativeLayout getFooterLayout(JSONObject comments) throws JSONException {
		RelativeLayout result = new RelativeLayout(context);
		RelativeLayout.LayoutParams resultParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		result.setLayoutParams(resultParams);
		
		getCommentInfo(result, comments);
		//getLikeInfo(result);
		
		ImageView fb = new ImageView(context);
		fb.setImageDrawable(context.getResources().getDrawable(R.drawable.facebook_icon));
		RelativeLayout.LayoutParams fbParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		fbParams.setMargins(5, 0, 0, 9);
		fbParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		fb.setLayoutParams(fbParams);
		result.addView(fb);
		return result;
	}

	private void getCommentInfo(RelativeLayout result, JSONObject comments) throws JSONException {
		RelativeLayout l = new RelativeLayout(context);
		LinearLayout ll = new LinearLayout(context);
		ImageView comment = new ImageView(context);
		TextView commentCount = new TextView(context);
		if (comments != null) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 10, 5);
			commentCount.setLayoutParams(params);
			int c = comments.getJSONArray("data").length();
			commentCount.setText(c + "");
			commentCount.setTextColor(Color.parseColor("#686868"));
			commentCount.setTextSize(11);
			commentCount.setGravity(Gravity.CENTER);
		}
		comment.setImageDrawable(context.getResources().getDrawable(R.drawable.comment_box));
		RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		likeParams.setMargins(0, 4, 15, 0);
		likeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		l.setLayoutParams(likeParams);
		l.setOnClickListener(commentListener());
		
		ll.addView(commentCount);
		ll.addView(comment);
		l.addView(ll);
		result.addView(l);
	}

	private OnClickListener commentListener() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				context.startActivity(intent);				
//				DetailsFeedLayout imageDetailsView = new DetailsFeedLayout(context, url);
//				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//    			alertDialog.setView(imageDetailsView);
//    			alertDialog.show();
			}
		};
	}

	private void getLikeInfo(RelativeLayout result) {
		ImageView like = new ImageView(context);
		like.setImageDrawable(context.getResources().getDrawable(R.drawable.facebook_like));
		RelativeLayout.LayoutParams commentParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		commentParams.setMargins(0, 0, 60, 7);
		commentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		like.setLayoutParams(commentParams);
		result.addView(like);
	}

	private RelativeLayout getContentHeadLayout() {
		RelativeLayout result = new RelativeLayout(context);
		TextView route = new TextView(context);
		route.setText("Kuressaare - Tallinn");
		route.setTextColor(Color.parseColor("#686868"));
		route.setTextSize(10);
		RelativeLayout.LayoutParams routeParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		routeParams.setMargins(25, 0, 0, 0);
		route.setLayoutParams(routeParams);
		
		TextView price = new TextView(context);
		price.setText("Hind €");
		price.setTextColor(Color.parseColor("#686868"));
		price.setTextSize(10);
		RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		routeParams.setMargins(0, 0, 10, 0);
		priceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		price.setLayoutParams(priceParams);
		result.addView(route);
		result.addView(price);
		return result;
	}

	@SuppressLint("NewApi")
	private LinearLayout getMessageLayout() {
		LinearLayout result = new LinearLayout(context);
		result.setBackground(context.getResources().getDrawable(R.drawable.message));
		result.setMinimumHeight(110);
		MessageTextView textView = new MessageTextView(context, result);
		textView.setText(message);
		textView.setTextColor(Color.parseColor("#686868"));
		textView.setTextSize(11);
		textView.setGravity(Gravity.CENTER);
		result.addView(textView);
		return result;
	}

	@SuppressLint("NewApi")
	private LinearLayout getHeader() {
		LinearLayout result = new LinearLayout(context);
		result.setBackground(context.getResources().getDrawable(R.drawable.pilt));
		
		result.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			     140, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 15, 0, 0);
		result.setLayoutParams(params);
		result.setPadding(0, 5, 15, 20);
		if (user != null) {
			TextView textView = getUserNameView(user);
			ImageView imageView = new ImageView(context);
			imageView.setMinimumHeight(80);
			imageView.setMinimumWidth(80);
			
			imageView.setImageDrawable(user.getImage());
			result.addView(imageView);
			result.addView(textView);
		} else {
		}
		return result;
	}

	private TextView getUserNameView(User user) {
		TextView textView = new TextView(context);
		LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT
					);
		textView.setTextSize(11);
		textView.setPadding(0, 0, 0, 0);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.parseColor("#686868"));
		textView.setLayoutParams(params);
		textView.setText(user.getName());
		return textView;
	}
}
