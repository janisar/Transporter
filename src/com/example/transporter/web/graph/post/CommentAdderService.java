package com.example.transporter.web.graph.post;

import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Session;

public class CommentAdderService extends AsyncTask<String, Void, Void> {

	String postId;
	
	public CommentAdderService(String postId) {
		this.postId = postId + "/comments";
	}
	
	@Override
	protected Void doInBackground(String... params) {
		Session session = Session.getActiveSession();
		Bundle bundle = new Bundle();
		bundle.putString("message", params[0]);
		new Request(session, postId, bundle, HttpMethod.POST);
		return null;
	}
}
