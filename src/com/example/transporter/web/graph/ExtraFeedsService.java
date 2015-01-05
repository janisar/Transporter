package com.example.transporter.web.graph;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class ExtraFeedsService extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		HttpGet getMethod = new HttpGet(params[0]);
		HttpClient client = new DefaultHttpClient();
		
		HttpResponse response = null;
		try {
			response = client.execute(getMethod);
			result = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
