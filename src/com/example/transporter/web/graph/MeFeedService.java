package com.example.transporter.web.graph;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.transporter.core.User;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MeFeedService extends AsyncTask<Void, Void, User>{

	@Override
	protected User doInBackground(Void... params) {
		Session session = Session.getActiveSession();
		Bundle bundle = new Bundle();
		bundle.putString("fields", "picture,name");
		FacebookCallback callback = new FacebookCallback();
		Request request = new Request(session, "me", bundle, HttpMethod.GET, callback);
		Log.i("FeedService PATH IS ", request.getGraphPath());
		Response response = Request.executeAndWait(request);
		
		if (callback.getErrorCode() == 0) {
			try {
				JSONObject object = new JSONObject(response.getRawResponse());
				String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
				String name = object.getString("name");
				User user = new User(name, image);
				return user;
			} catch (JSONException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	
}
