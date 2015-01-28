package com.example.transporter.web.graph;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class CommentService extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... params) {
		Session session = Session.getActiveSession();
		Bundle bundle = new Bundle();
		bundle.putString("fields", "comments.limit(10){from{name,picture},message,created_time}");
		FacebookCallback callback = new FacebookCallback();
		Request request = new Request(session, params[0], bundle, HttpMethod.GET, callback);
		Log.i("FeedService PATH IS ", request.getGraphPath());
		Response response = Request.executeAndWait(request);
		
		if (callback.getErrorCode() == 0) {
			return response.getRawResponse();
		} else {
			return "ERROR";
		}
	}

}
