package com.example.transporter.web.graph;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.transporter.core.User;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class UserService extends AsyncTask<String, Void, User>{

	@Override
	protected User doInBackground(String... params) {
		Bundle bundle = new Bundle();
		bundle.putString("fields", "name,picture");
		Session session = Session.getActiveSession();
		Request request = new Request(session, params[0], bundle, HttpMethod.GET, new Request.Callback() {
			public void onCompleted(Response response) {
				FacebookRequestError error = response.getError();
				if(error!=null){
					Log.e("Error", error.getErrorMessage());
				}

			}
		});
		Log.i("UserService PATH IS ", request.getGraphPath());
		Response response = Request.executeAndWait(request);
		return getUserFromResponse(response.getRawResponse());
	}

	private User getUserFromResponse(String response) {
		User user = null;
		
		try {
			JSONObject object = new JSONObject(response);
			String name = object.getString("name");
			String imageUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
			user = new User(name, imageUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return user;
	}
}
