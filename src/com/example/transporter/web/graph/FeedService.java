package com.example.transporter.web.graph;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class FeedService extends AsyncTask<Void, Void, String>{

	String id;
	
	public FeedService(String id) {
		this.id = id;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		Session session = Session.getActiveSession();
		Bundle bundle = new Bundle();
		bundle.putString("fields", "feed.limit(15){message,actions,comments.limit(10){from{name,picture},message,created_time,can_comment,can_like,likes},from{name,picture}}");
		Request request = new Request(session, id, bundle, HttpMethod.GET, new Request.Callback() {
			public void onCompleted(Response response) {
				FacebookRequestError error = response.getError();
				if(error!=null){
					Log.e("Error", error.getErrorMessage());
				}

			}
		});
		Log.i("FeedService PATH IS ", request.getGraphPath());
		Response response = Request.executeAndWait(request);
		return response.getRawResponse();
	}
}
