package com.example.transporter.web.graph;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;

public class FeedService extends AsyncTask<Void, Void, String>{

	private Context context;
	String id;
	
	public FeedService(Context context, String id) {
		this.context = context;
		this.id = id;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		Session session = Session.getActiveSession();
		Bundle bundle = new Bundle();
		bundle.putString("fields", "feed.limit(15){message,actions,comments.limit(10){from{name,picture},message,created_time,can_comment,can_like,likes},from{name,picture}}");
		FacebookCallback callback = new FacebookCallback();
		Request request = new Request(session, id, bundle, HttpMethod.GET, callback);
		Log.i("FeedService PATH IS ", request.getGraphPath());
		Response response = Request.executeAndWait(request);
		
		if (callback.getErrorCode() == 0) {
			return response.getRawResponse();
		} else {
			return "ERROR";
		}
	}
}

class FacebookCallback implements Callback {

	private int errorCode = 0;
	
	@Override
	public void onCompleted(Response response) {
		FacebookRequestError error = response.getError();
		if (error!=null) {
			errorCode = 1;
			Log.e("Error", error.getErrorMessage());
		}
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}
