package com.example.transporter.form.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.transporter.R;
import com.example.transporter.core.User;

public class HeaderLayout extends LinearLayout {

	private Context context;
	private User user;
	
	public HeaderLayout(Context context, User user) {
		super(context);
		this.context = context;
		this.user = user;
		this.addView(getHeader());
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
			
			WebView imageView = new WebView(context);
			imageView.loadUrl(user.getImageUrl());
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
