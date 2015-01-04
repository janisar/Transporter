package com.example.transporter.service;

import java.util.concurrent.ExecutionException;

import android.graphics.drawable.Drawable;

import com.example.transporter.core.User;
import com.example.transporter.web.down.DownloadImageTask;
import com.example.transporter.web.graph.UserService;

public class UserThread implements Runnable {

	private volatile User user;
	private String from;
	
	public UserThread(String from) {
		this.from = from;
		run();
	}

	@Override
	public void run() {
		User user = null;
		try {
			user = new UserService().execute(from).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
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
	
	public User getUser() {
		return user;
	}
}
