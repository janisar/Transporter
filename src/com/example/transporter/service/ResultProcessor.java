package com.example.transporter.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.transporter.R;
import com.example.transporter.core.User;

public class ResultProcessor implements Runnable {
	
	private Context context;
	private LinearLayout scrollViewLayout;
	private String message;
	private User user;
	private String regexMather1 = "(.*[Kk][uU][Rr](e)?(ssaare)?([->\\s]*)[Tt](a)?[Ll](lin)?[Nn].*)|(.*Kuressaarest.*)|(.*[Tt](a)?llinnasse.*)";
	
	public ResultProcessor(Context context, LinearLayout scrollViewLayout,
			User user, String message) {
		this.context = context;
		this.scrollViewLayout = scrollViewLayout;
		this.user = user;
		this.message = message;
	}

	@Override
	public void run() {
		String newline = System.getProperty("line.separator");
		String[] splitted = message.split(newline);
		if (matchesPattern(splitted)) {
			addToScrollView(message, user);
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

	private void addToScrollView(String message, User user) {
		TextView messageText = new TextView(context);
		messageText.setText(message);
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 10, 20, 0);
		layout.setLayoutParams(params);
		LinearLayout l = getHeader(user);
		layout.addView(l);
		layout.addView(getMessageLayout());
		
		scrollViewLayout.addView(layout);
	}

	
	@SuppressLint("NewApi")
	private LinearLayout getMessageLayout() {
		LinearLayout result = new LinearLayout(context);
		result.setBackground(context.getResources().getDrawable(R.drawable.message));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			     LayoutParams.WRAP_CONTENT, 115);
		params.setMargins(15, 30, 25, 0);
		result.setLayoutParams(params);
		TextView textView = new TextView(context);
		textView.setText(message);
		textView.setTextColor(Color.parseColor("#686868"));
		textView.setTextSize(11);
		textView.setGravity(Gravity.CENTER);
		result.addView(textView);
		return result;
	}

	@SuppressLint("NewApi")
	private LinearLayout getHeader(User user) {
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
