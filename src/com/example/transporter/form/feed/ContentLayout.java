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
import com.example.transporter.core.User;
import com.example.transporter.form.MessageTextView;

public class ContentLayout extends LinearLayout{

	private Context context;
	private JSONObject comments;
	private String message;
	private CommentLayout commentLayout;
	private AddCommentLayout newCommentLayout;
	private User me;
	private String id;
	
	public ContentLayout(Context context, JSONObject comments, String message, User me, String id) {
		super(context);
		this.context = context;
		this.comments = comments;
		this.message = message;
		this.me = me;
		this.id = id;
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
		vertical.addView(getDropDownLayout());
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
		
		result.addView(route);
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
		getCommentInfo();
		getDropDownLayout();
		return result;
	}
	
	@SuppressLint("NewApi")
	private View getDropDownLayout() throws JSONException {
		RelativeLayout layout = new RelativeLayout(context);
		ImageView dropView = new ImageView(context);
		dropView.setBackground(context.getResources().getDrawable(R.drawable.fast44));
		dropView.getBackground().setAlpha(100);
		RelativeLayout innerLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams innerParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		innerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		innerLayout.setLayoutParams(innerParams);
		innerLayout.addView(dropView);
		
		RelativeLayout commentInfo = getCommentInfo();
		
		layout.addView(innerLayout);
		layout.addView(commentInfo);
		layout.setBackgroundColor(Color.parseColor("#F9F9F9"));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 100, 0, 0);
		layout.setLayoutParams(params);
		layout.setOnClickListener(commentListener(dropView));
		return layout;
	}

	private RelativeLayout getCommentInfo() throws JSONException {
		RelativeLayout l = new RelativeLayout(context);
		LinearLayout ll = new LinearLayout(context);
		ImageView comment = new ImageView(context);
		TextView commentCount = new TextView(context);
		if (comments != null) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 10, 0);
			commentCount.setLayoutParams(params);
			int c = comments.getJSONArray("data").length();
			commentCount.setText(c + "");
			commentCount.setTextColor(Color.parseColor("#686868"));
			commentCount.setTextSize(11);
			commentCount.setGravity(Gravity.CENTER);
		}
		comment.setImageDrawable(context.getResources().getDrawable(R.drawable.comment_box));
		RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		likeParams.setMargins(0, 4, 15, 0);
		likeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		l.setLayoutParams(likeParams);
		
		ll.addView(commentCount);
		ll.addView(comment);
		l.addView(ll);
		return l;
	}
	
	private OnClickListener commentListener(final ImageView dropView) {
		return new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (commentLayout != null && commentLayout.isActive()) {
					dropView.setRotation(0);
					commentLayout.hideComment();
					newCommentLayout.hideComment();
				} else if (commentLayout != null) {
					commentLayout.showComment();
					newCommentLayout.showComment();
					dropView.setRotation(180);
				}
			}
		};
	}
	
	public void addCommentListener(LinearLayout scrollViewLayout) {
			commentLayout = new CommentLayout(context, comments);
			newCommentLayout = new AddCommentLayout(context, commentLayout, me, id);
			scrollViewLayout.addView(commentLayout);
			scrollViewLayout.addView(newCommentLayout);
			
	}
}
