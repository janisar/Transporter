package com.example.transporter.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.example.transporter.core.User;
import com.example.transporter.form.feed.ContentLayout;
import com.example.transporter.form.feed.HeaderLayout;

public class ResultProcessor extends Activity implements Runnable {
	
	private Context context;
	private LinearLayout scrollViewLayout;
	private JSONObject object;
	private String message;
	private User user;
	private String regexMather1 = "(.*[Kk][uU][Rr](e)?(ssaare)?([->\\s]*)[Tt](a)?[Ll](lin)?[Nn].*)|(.*Kuressaarest.*)|(.*[Tt](a)?llinnasse.*)";
	private User me;
	private int width;
	private static final String FACEBOOK_PENDING_GIF = "https://fbstatic-a.akamaihd.net/rsrc.php/v2/yb/r/GsNJNwuI-UM.gif";
	
	public ResultProcessor(Context context, LinearLayout scrollViewLayout, JSONObject jsonObject, int width, User me) {
		this.context = context;
		this.scrollViewLayout = scrollViewLayout;
		this.object = jsonObject;
		this.width = width;
		this.me = me;
		generateUser();
	}


	private void generateUser() {
		JSONObject from = null;
		try {
			from = object.getJSONObject("from");
		} catch (JSONException e) {
			Log.e("JSONOBJECT", object.toString());
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
		layout.addView(new HeaderLayout(context, user, width));
		ContentLayout contentLayout = new ContentLayout(context, comments, message, me, object.getString("id"));
		layout.addView(contentLayout);
		scrollViewLayout.addView(layout);
		contentLayout.addCommentListener(scrollViewLayout);
	}
}
