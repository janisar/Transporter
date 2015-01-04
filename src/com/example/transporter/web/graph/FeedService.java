package com.example.transporter.web.graph;

import android.os.AsyncTask;
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
		Request request = new Request(session, id, null, HttpMethod.GET, new Request.Callback() {
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
