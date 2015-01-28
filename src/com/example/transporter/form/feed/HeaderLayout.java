package com.example.transporter.form.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.example.transporter.R;
import com.example.transporter.core.User;

public class HeaderLayout extends LinearLayout {

	private Context context;
	private User user;
	private int width;
	
	public HeaderLayout(Context context, User user, int width) {
		super(context);
		this.context = context;
		this.user = user;
		this.width = width;
		this.addView(getHeader());
	}
	
	@SuppressLint("NewApi")
	private LinearLayout getHeader() {
		LinearLayout result = new LinearLayout(context);
		result.setBackground(context.getResources().getDrawable(R.drawable.pilt));
		
		result.setOrientation(LinearLayout.VERTICAL);
		int layoutWidth = (width / 8 > 90) ? width / 8 : 90;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				layoutWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 15, 0, 0);
		result.setLayoutParams(params);
		result.setPadding(0, 5, 15, 20);
		if (user != null) {
			TextView textView = getUserNameView(user);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			p.setMargins(5, 0, 0, 0);
			WebView imageView = new WebView(context);
			imageView.loadUrl(user.getImageUrl());
			imageView.setLayoutParams(p);
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
		textView.setTextSize(9);
		textView.setPadding(0, 0, 0, 0);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.parseColor("#686868"));
		textView.setLayoutParams(params);
		textView.setText(user.getName());
		return textView;
	}

}
