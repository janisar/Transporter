package com.example.transporter.form.feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Xml.Encoding;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.example.transporter.R;

public class CommentLayout extends LinearLayout {

	public static final int IMAGE_URL_ID = 9001; 
	
	private Context context;
	private JSONObject comments;
	private LinearLayout.LayoutParams defaultParams;
	private LinearLayout commentLayout;
	private boolean active = false;

	
	public CommentLayout(Context context, JSONObject comments) {
		super(context);
		this.context = context;
		this.comments = comments;
		try {
			addView(getCommentLayout());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		hideComment();
	}

	private View getCommentLayout() throws JSONException {
		if (comments != null) {
			defaultParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
			commentLayout = new LinearLayout(context);
			commentLayout.setOrientation(LinearLayout.VERTICAL);
			commentLayout.setLayoutParams(defaultParams);
			
			JSONArray array = comments.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				processComment(object);
			}
			return commentLayout;
		} else return null;
	}

	private void processComment(JSONObject object) throws JSONException {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 2, 0, 1);
		LinearLayout commentDataLayout = getTextWithoutDateLayout();
		commentDataLayout.setBackgroundColor(Color.parseColor("#ECECEC"));
		commentDataLayout.setLayoutParams(params);
		
		String imageUrl = object.getJSONObject("from").getJSONObject("picture").getJSONObject("data").getString("url");
		String message = object.getString("message");
		String date = object.getString("created_time");
		String name = object.getJSONObject("from").getString("name");
		
		LinearLayout imageLayout = getImageLayout(imageUrl);
		LinearLayout textLayout = getTextLayout();
		LinearLayout textWithoutDateLayout = getTextWithoutDateLayout();
		
		TextView nameText = getNameText(name, message);
		TextView dateText = getDate(date);
		TextView imageUrlText = getImageUrlText(imageUrl);
		
		textWithoutDateLayout.addView(nameText);
		textLayout.addView(textWithoutDateLayout);
		textLayout.addView(dateText);
		
		commentDataLayout.addView(imageLayout);
		commentDataLayout.addView(textLayout);
		commentLayout.addView(commentDataLayout);
		commentLayout.addView(imageUrlText);
	}

	private TextView getImageUrlText(String imageUrl) {
		TextView textView = new TextView(context);
		textView.setText(imageUrl);
		textView.setTextSize(1);
		textView.setVisibility(View.INVISIBLE);
		textView.setId(IMAGE_URL_ID);
		return textView;
	}

	private TextView getDate(String date) {
		TextView dateText = new TextView(context);
		dateText.setText(date);
		dateText.setTextColor(Color.parseColor("#686868"));
		dateText.setTextSize(9);
		return dateText;
	}

	private TextView getNameText(String name, String message) {
		TextView nameText = new TextView(context);
		nameText.setText(Html.fromHtml("<font color ='#6074AB'>" + name + "  " + "</font><font color='#686868'>" + message + "</a>"));
		nameText.setTextColor(Color.parseColor("#6074AB"));
		nameText.setTextSize(10);
		return nameText;
	}

	private LinearLayout getTextWithoutDateLayout() {
		LinearLayout textWithoutDateLayout = new LinearLayout(context);
		textWithoutDateLayout.setOrientation(LinearLayout.HORIZONTAL);
		return textWithoutDateLayout;
	}

	private LinearLayout getTextLayout() {
		LinearLayout textLayout = new LinearLayout(context);
		textLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.setMargins(10, 0, 0, 0);
		textLayout.setLayoutParams(p);
		return textLayout;
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private LinearLayout getImageLayout(String imageUrl) {
		LinearLayout imageLayout = new LinearLayout(context);
		imageLayout.setBackgroundColor(Color.parseColor("#ECECEC"));
		
		imageLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(70, 80);
		imageLayout.setLayoutParams(nameParams);
		
		WebView webView = new WebView(context);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadDataWithBaseURL("", "<img src = '" + imageUrl + "' width='25px' height='25px'>", "text/html", "UTF-8", "");
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		imageLayout.addView(webView);
		return imageLayout;
	}
	
	public void hideComment() {
		defaultParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
		defaultParams.setMargins(90, 0, 20, 0);
		commentLayout.setLayoutParams(defaultParams);
		active = false;
	}
	
	public void showComment() {
		defaultParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(90, 0, 20, 0);
		commentLayout.setLayoutParams(defaultParams);
		active = true;
	}

	public boolean isActive() {
		return active;
	}
}