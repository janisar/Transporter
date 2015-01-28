package com.example.transporter.form;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.example.transporter.R;

public class MenuButtonsLayout extends RelativeLayout {

	private Context context;
	private LinearLayout menuView;
	private String communityName;
	
	
	public MenuButtonsLayout(Context context, LinearLayout menuView, String communityName) {
		super(context);
		this.context = context;
		this.menuView = menuView;
		this.communityName = communityName;
		addMenuButtonLayout();
		addHeaderLayout();
		addNewPostLayout();
	}

	
	private void addNewPostLayout() {
		RelativeLayout layout = new RelativeLayout(context);
		RelativeLayout.LayoutParams newPostParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		newPostParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout.setLayoutParams(newPostParams);
		
		LinearLayout innerLayout = new LinearLayout(context);
		TextView add = new TextView(context);
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		add.setLayoutParams(textParams);
		add.setGravity(Gravity.CENTER_VERTICAL);
		add.setText(context.getResources().getString(R.string.new_post));
		add.setTextSize(12);
		add.setTypeface(null, Typeface.BOLD_ITALIC);
		add.setTextColor(Color.parseColor("#7D8289"));
		innerLayout.addView(add);
		
		RelativeLayout newPostLayout = new RelativeLayout(context);
		ImageView addNewPost = new ImageView(context);
		addNewPost.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_add));
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.CENTER_VERTICAL);
		newPostLayout.setLayoutParams(p);
		newPostLayout.addView(addNewPost);
		
		innerLayout.addView(newPostLayout);
		layout.addView(innerLayout);
		this.addView(layout);
	}
	private void addHeaderLayout() {
		RelativeLayout headerLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 55);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		headerLayout.setLayoutParams(params);
		
		RelativeLayout spinnerLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.CENTER_VERTICAL);
		spinnerLayout.setLayoutParams(p);
		Spinner header = initSpinner(R.array.kur_tln_route);
		spinnerLayout.addView(header);
		//header.setTextSize(16);
		//header.setTextColor(Color.parseColor("#7D8289"));
		headerLayout.addView(spinnerLayout);
		this.addView(headerLayout);
	}
	
	private Spinner initSpinner(int id) {
		Spinner sort = new Spinner(context);
		
		ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(context,
		        id, android.R.layout.simple_spinner_item);
		sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sort.setAdapter(sortAdapter);
		return sort;
	}
	
	private void addMenuButtonLayout() {
		RelativeLayout menuButtonLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 45);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.setMargins(0, 10, 10, 10);
		menuButtonLayout.setLayoutParams(params);

		ImageView menuButton = new ImageView(context);
		menuButton.setMaxHeight(10);
		menuButton.setImageDrawable(context.getResources().getDrawable(R.drawable.menu_icon));
		menuButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout.LayoutParams params;
				if (menuView.getWidth() > 10) {
					params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
					
				} else {
					params = new LinearLayout.LayoutParams(270, LayoutParams.MATCH_PARENT);
				}
				menuView.setLayoutParams(params);
			}
		});
		menuButtonLayout.addView(menuButton);
		this.addView(menuButtonLayout);
	}
}
