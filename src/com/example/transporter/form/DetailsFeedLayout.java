package com.example.transporter.form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

@Deprecated
public class DetailsFeedLayout extends RelativeLayout {

	private Session session;
	private Context context;
	private String url;
	
	public DetailsFeedLayout(Context context, String url) {
		super(context);
		this.context = context;
		this.url = url;
		authUser();
		this.addView(getWebView());
	}

	@SuppressLint("SetJavaScriptEnabled")
	private View getWebView() {
		WebView view = new WebView(context);
		view.setWebViewClient(new WebViewClient());
		view.getSettings().setJavaScriptEnabled(true);
		view.loadUrl(url + "?access_token=" + session.getAccessToken());
		return view;
	}
	
	private void authUser() {
		session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
	        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	          
	        	@Override
	            public void onCompleted(GraphUser user, Response response) {
	                if (session == Session.getActiveSession()) {
	                	
	                }   
	            }   
	        }); 
	        Request.executeBatchAsync(request);
	    }
	}

}
