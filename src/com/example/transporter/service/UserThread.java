package com.example.transporter.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.transporter.core.User;

public class UserThread implements Runnable {

	private volatile User user;
	private JSONObject from;
	
	public UserThread(JSONObject from) {
		this.from = from;
		run();
	}

	@Override
	public void run() {
		generateUser();
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
