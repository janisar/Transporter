package com.example.transporter.form.feed;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.transporter.R;
import com.example.transporter.form.MessageTextView;

public class ContentLayout extends LinearLayout{

	private Context context;
	private JSONObject comments;
	private String message;
	private CommentLayout commentLayout;
	
	public ContentLayout(Context context, JSONObject comments, String message) {
		super(context);
		this.context = context;
		this.comments = comments;
		this.message = message;
		try {
			this.addView(getContentLayout());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private LinearLayout getContentLayout() throws JSONException {
		LinearLayout vertical = new LinearLayout(context);
		vertical.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		vertical.setLayoutParams(params);
		RelativeLayout contentHead = getContentHeadLayout();
		LinearLayout result = getMessageLayout();
		RelativeLayout footer = getFooterLayout();
		vertical.addView(contentHead);
		vertical.addView(result);
		vertical.addView(footer);
		return vertical;
	}
	
	private RelativeLayout getContentHeadLayout() {
		RelativeLayout result = new RelativeLayout(context);
		TextView route = new TextView(context);
		route.setText("Kuressaare - Tallinn");
		route.setTextColor(Color.parseColor("#686868"));
		route.setTextSize(10);
		RelativeLayout.LayoutParams routeParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		routeParams.setMargins(25, 0, 0, 0);
		route.setLayoutParams(routeParams);
		
		TextView price = new TextView(context);
		price.setText("Hind €");
		price.setTextColor(Color.parseColor("#686868"));
		price.setTextSize(10);
		RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		routeParams.setMargins(0, 0, 10, 0);
		priceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		price.setLayoutParams(priceParams);
		result.addView(route);
		result.addView(price);
		return result;
	}
	
	@SuppressLint("NewApi")
	private LinearLayout getMessageLayout() {
		LinearLayout result = new LinearLayout(context);
		result.setBackground(context.getResources().getDrawable(R.drawable.message));
		result.setMinimumHeight(110);
		MessageTextView textView = new MessageTextView(context, result);
		textView.setText(message);
		textView.setTextColor(Color.parseColor("#686868"));
		textView.setTextSize(11);
		textView.setGravity(Gravity.CENTER);
		result.addView(textView);
		return result;
	}
	
	@SuppressLint("NewApi")
	private RelativeLayout getFooterLayout() throws JSONException {
		RelativeLayout result = new RelativeLayout(context);
		RelativeLayout.LayoutParams resultParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		result.setLayoutParams(resultParams);
		boolean hasComments = getCommentInfo(result, comments);
		getLikeInfo(result, hasComments);
		return result;
	}
	
	private boolean getCommentInfo(RelativeLayout result, JSONObject comments) throws JSONException {
		boolean hasComments = false;
		RelativeLayout l = new RelativeLayout(context);
		LinearLayout ll = new LinearLayout(context);
		ImageView comment = new ImageView(context);
		TextView commentCount = new TextView(context);
		if (comments != null) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 10, 5);
			commentCount.setLayoutParams(params);
			int c = comments.getJSONArray("data").length();
			commentCount.setText(c + "");
			commentCount.setTextColor(Color.parseColor("#686868"));
			commentCount.setTextSize(11);
			commentCount.setGravity(Gravity.CENTER);
			hasComments = true;
		}
		comment.setImageDrawable(context.getResources().getDrawable(R.drawable.comment_box));
		RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		likeParams.setMargins(0, 4, 15, 0);
		likeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		l.setLayoutParams(likeParams);
		l.setOnClickListener(commentListener());
		
		ll.addView(commentCount);
		ll.addView(comment);
		l.addView(ll);
		result.addView(l);
		return hasComments;
	}
	
	private OnClickListener commentListener() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (commentLayout != null && commentLayout.isActive()) {
					commentLayout.hideComment();
				} else if (commentLayout != null) {
					commentLayout.showComment();
				}
			}
		};
	}
	
	private void getLikeInfo(RelativeLayout result, boolean hasComments) {
		ImageView like = new ImageView(context);
		like.setImageDrawable(context.getResources().getDrawable(R.drawable.facebook_like));
		RelativeLayout.LayoutParams commentParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (hasComments) {
			commentParams.setMargins(0, 0, 80, 7);
		} else {
			commentParams.setMargins(0, 0, 60, 7);
		}
		commentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		like.setLayoutParams(commentParams);
		result.addView(like);
	}

	public void addCommentListener(LinearLayout scrollViewLayout) {
		if (this.comments != null) {
			commentLayout = new CommentLayout(context, comments);
			scrollViewLayout.addView(commentLayout);
		}
	}
}
