package com.example.transporter.form.feed;

import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;

public class ImageLoaderTask extends AsyncTask<String, Void, Void>{

	private WebView webView;
	
	public ImageLoaderTask(WebView webView) {
		this.webView = webView;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		this.webView.loadUrl(params[0]);
		return null;
	}
}
