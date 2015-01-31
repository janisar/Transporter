package com.example.transporter.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.example.transporter.core.User;
import com.example.transporter.form.feed.ContentLayout;
import com.example.transporter.form.feed.HeaderLayout;

public class SingleResultProcessor implements Runnable {
	
	private Context context;
	private volatile LinearLayout result;
	private JSONObject object;
	private String message;
	private User user;
	private String regexMather1 = "(.*[Kk][uU][Rr](e)?(ssaare)?([->\\s]*)[Tt](a)?[Ll](lin)?[Nn].*)|(.*Kuressaarest.*)|(.*[Tt](a)?llinnasse.*)";
	private int width;
	private volatile ContentLayout contentLayout;
	
	public SingleResultProcessor(Context context, JSONObject jsonObject, int width) {
		this.context = context;
		this.object = jsonObject;
		this.width = width;
		generateUser();
		run();
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
			createView(comments);
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

	private void createView(JSONObject comments) throws JSONException {
		result = new LinearLayout(context);
		result.setOrientation(LinearLayout.HORIZONTAL);
		result.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 10, 20, 0);
		result.setLayoutParams(params);
		result.addView(new HeaderLayout(context, user, width));
	}
	
	public LinearLayout getView() {
		synchronized (result) {
			return result;
		}
	}
	
	public ContentLayout getContentLayout() {
		return contentLayout;
	}
}
