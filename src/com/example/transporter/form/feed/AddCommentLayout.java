package com.example.transporter.form.feed;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.transporter.R;
import com.example.transporter.core.User;
import com.example.transporter.web.graph.CommentService;

public class AddCommentLayout extends CommentLayout {

	private Context context;
	private EditText newComment;
	private User me;
	private String id;
	private CommentLayout commentLayout;
	
	public AddCommentLayout(Context context, CommentLayout commentLayout, User me, String id) {
		super(context, null);
		this.context = context;
		this.commentLayout = commentLayout;
		this.me = me;
		this.id = id;
		defaultParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(defaultParams);
		try {
			addView(getNewCommentLayout());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		hideComment();
	}

	private LinearLayout getNewCommentLayout() throws JSONException {
		LinearLayout commentLayout = initCommentLayout(null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 2, 0, 1);
		LinearLayout commentDataLayout = getTextWithoutDateLayout();
		commentDataLayout.setBackgroundColor(Color.parseColor("#ECECEC"));
		commentDataLayout.setLayoutParams(params);
		
		LinearLayout imageLayout = getImageLayout(me.getImageUrl());
		
		newComment = getNewCommentEditText();
		
		RelativeLayout l = new RelativeLayout(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		l.setLayoutParams(lp);
		RelativeLayout buttonLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(100, 55);
		p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		p.setMargins(0, 0, 10, 10);
		buttonLayout.setLayoutParams(p);
		Button button = getNewCommentButton();
		buttonLayout.addView(button);
		
		l.addView(buttonLayout);
		LinearLayout textLayout = getTextLayout();
		
		textLayout.addView(newComment);
		textLayout.addView(l);
		commentDataLayout.addView(imageLayout);
		commentDataLayout.addView(textLayout);
		
		commentLayout.addView(commentDataLayout);
		return commentLayout;
	}
	
	@SuppressLint("NewApi")
	protected Button getNewCommentButton() {
		Button button = new Button(context);
		button.setText("Postita");
		button.setTextSize(10);
		button.setWidth(80);
		button.setHeight(40);
		button.setTextColor(Color.parseColor("#ffffff"));
		button.setBackground(getResources().getDrawable(R.drawable.com_facebook_button_blue));
		button.setOnClickListener(newCommentListener());
		return button;
	}

	private OnClickListener newCommentListener() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v)  {
				if (newComment != null && !newComment.getText().toString().equals("")) {
					Toast.makeText(context, "Postitan '" + id + "' sõnumi : " + newComment.getText() , Toast.LENGTH_LONG).show();
					//new CommentAdderService(id).execute(newComment.getText().toString());
					newComment.setText("");
					try {
						JSONObject o = new JSONObject(new CommentService().execute(id).get());
						commentLayout.removeAllViews();
						commentLayout.addView(commentLayout.initCommentLayout(o.getJSONObject("comments")));
						setDefaultParams();
					} catch (JSONException | InterruptedException
							| ExecutionException e) {
						e.printStackTrace();
					}
					commentLayout = new CommentLayout(context, null);
				} else {
					Toast.makeText(context, "Perse mine", Toast.LENGTH_SHORT).show();
				}
			}
			
			private void setDefaultParams() {
				defaultParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				defaultParams.setMargins(20, 0, 20, 0);
				commentLayout.setLayoutParams(defaultParams);
			}
		};
	}
}
