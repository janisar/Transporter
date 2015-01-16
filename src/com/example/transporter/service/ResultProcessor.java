package com.example.transporter.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.transporter.R;
import com.example.transporter.core.User;
import com.example.transporter.form.MessageTextView;
import com.example.transporter.form.feed.CommentLayout;
import com.example.transporter.form.feed.ContentLayout;
import com.example.transporter.form.feed.HeaderLayout;

public class ResultProcessor extends Activity implements Runnable {
	
	private Context context;
	private LinearLayout scrollViewLayout;
	private JSONObject object;
	private String message;
	private User user;
	private String regexMather1 = "(.*[Kk][uU][Rr](e)?(ssaare)?([->\\s]*)[Tt](a)?[Ll](lin)?[Nn].*)|(.*Kuressaarest.*)|(.*[Tt](a)?llinnasse.*)";
	private static final String FACEBOOK_PENDING_GIF = "https://fbstatic-a.akamaihd.net/rsrc.php/v2/yb/r/GsNJNwuI-UM.gif";
	
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
		layout.addView(new HeaderLayout(context, user));
		ContentLayout contentLayout = new ContentLayout(context, comments, message);
		layout.addView(contentLayout);
		scrollViewLayout.addView(layout);
		contentLayout.addCommentListener(scrollViewLayout);
	}
}
