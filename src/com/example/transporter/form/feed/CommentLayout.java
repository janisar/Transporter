package com.example.transporter.form.feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.transporter.R;

public class CommentLayout extends LinearLayout {

	private Context context;
	private JSONObject comments;
	private LinearLayout.LayoutParams defaultParams;
	private LinearLayout commentLayout;
	private boolean active = false;

	
	public CommentLayout(Context context, JSONObject comments) {
		super(context);
		this.context = context;
		this.comments = comments;
		try {
			addView(getCommentLayout());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		hideComment();
	}

	private View getCommentLayout() throws JSONException {
		if (comments != null) {
			defaultParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
			commentLayout = new LinearLayout(context);
			commentLayout.setOrientation(LinearLayout.VERTICAL);
			commentLayout.setLayoutParams(defaultParams);
			
			JSONArray array = comments.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				LinearLayout commentDataLayout = processComment(object);
				commentLayout.addView(commentDataLayout);
			}
			return commentLayout;
		} else return null;
	}

	private LinearLayout processComment(JSONObject object) throws JSONException {
		LinearLayout commentDataLayout = getTextWithoutDateLayout();
		commentDataLayout.setBackgroundColor(Color.parseColor("#ECECEC"));
		
		String imageUrl = object.getJSONObject("from").getJSONObject("picture").getJSONObject("data").getString("url");
		String message = object.getString("message");
		String date = object.getString("created_time");
		String name = object.getJSONObject("from").getString("name");
		
		LinearLayout imageLayout = getImageLayout();
		LinearLayout textLayout = getTextLayout();
		LinearLayout textWithoutDateLayout = getTextWithoutDateLayout();
		
		TextView nameText = getNameText(name);
		TextView m = getMessage(message);
		TextView dateText = getDate(date);
		
		textWithoutDateLayout.addView(nameText);
		textWithoutDateLayout.addView(m);
		textLayout.addView(textWithoutDateLayout);
		textLayout.addView(dateText);
		
		commentDataLayout.addView(imageLayout);
		commentDataLayout.addView(textLayout);
		return commentDataLayout;
	}

	private TextView getDate(String date) {
		TextView dateText = new TextView(context);
		dateText.setText(date);
		dateText.setTextColor(Color.parseColor("#686868"));
		dateText.setTextSize(9);
		return dateText;
	}

	private TextView getMessage(String message) {
		TextView m = new TextView(context);
		m.setText(message);
		m.setTextColor(Color.parseColor("#686868"));
		m.setTextSize(10);
		m.setGravity(Gravity.CENTER);
		return m;
	}

	private TextView getNameText(String name) {
		TextView nameText = new TextView(context);
		nameText.setText(name + "  ");
		nameText.setTextColor(Color.parseColor("#6074AB"));
		nameText.setTextSize(10);
		nameText.setGravity(Gravity.CENTER);
		return nameText;
	}

	private LinearLayout getTextWithoutDateLayout() {
		LinearLayout textWithoutDateLayout = new LinearLayout(context);
		textWithoutDateLayout.setOrientation(LinearLayout.HORIZONTAL);
		return textWithoutDateLayout;
	}

	private LinearLayout getTextLayout() {
		LinearLayout textLayout = new LinearLayout(context);
		textLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.setMargins(10, 0, 0, 0);
		textLayout.setLayoutParams(p);
		return textLayout;
	}

	@SuppressLint("NewApi")
	private LinearLayout getImageLayout() {
		LinearLayout imageLayout = new LinearLayout(context);
		imageLayout.setBackground(context.getResources().getDrawable(R.drawable.pilt));
		
		imageLayout.setOrientation(LinearLayout.VERTICAL);
		imageLayout.setPadding(0, 0, 10, 0);
		LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(70, 80);
		imageLayout.setLayoutParams(nameParams);
		
		TextView n = new TextView(context);
		n.setText(" ");
		imageLayout.addView(n);
		return imageLayout;
	}
	
	public void hideComment() {
		defaultParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
		defaultParams.setMargins(90, 0, 0, 0);
		commentLayout.setLayoutParams(defaultParams);
		active = false;
	}
	
	public void showComment() {
		defaultParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(90, 0, 0, 0);
		commentLayout.setLayoutParams(defaultParams);
		active = true;
	}

	public boolean isActive() {
		return active;
	}
}
