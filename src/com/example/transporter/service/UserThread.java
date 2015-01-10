package com.example.transporter.service;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

import com.example.transporter.core.User;
import com.example.transporter.web.down.DownloadImageTask;

public class UserThread implements Runnable {

	private volatile User user;
	private JSONObject from;
	
	public UserThread(JSONObject from) {
		this.from = from;
		generateUser();
		run();
	}

	@Override
	public void run() {
		if (user != null) {
			Drawable image = null;
			try {
				image = new DownloadImageTask().execute(user.getImageUrl()).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			user.setDrawable(image);
		}
		this.user = user;
	}
	
	private void generateUser() {
		String name = null;
		String imageUrl = null;
		try {
			name = from.getString("name");
			imageUrl = from.getJSONObject("picture").getJSONObject("data").getString("url");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		this.user = new User(name, imageUrl);
	}

	public User getUser() {
		return user;
	}
}
