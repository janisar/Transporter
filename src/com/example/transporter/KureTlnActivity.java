package com.example.transporter;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.transporter.core.User;
import com.example.transporter.form.FeedScrollView;
import com.example.transporter.service.ResultProcessor;
import com.example.transporter.service.UserThread;
import com.example.transporter.util.StringUtils;
import com.example.transporter.web.graph.FeedService;

public class KureTlnActivity extends Activity {
	
	private static final String ID = "273758919316437/feed";

	private Context context;
	//private Session session;
	private ScrollView scrollView;
	private LinearLayout baseLayout;
	private LinearLayout buttonsLayout;
	private LinearLayout scrollViewLayout;
	private LinearLayout menuView;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_template);
		this.context = KureTlnActivity.this;
		baseLayout = (LinearLayout) findViewById(R.id.scrollLayout);
		baseLayout.setBackgroundColor(Color.parseColor("#DFDFDF"));
		buttonsLayout = (LinearLayout) findViewById(R.id.buttons_layout);
		menuView = (LinearLayout) findViewById(R.id.menuLayout);
		initMenuView();
		scrollViewLayout = new LinearLayout(context);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		scrollViewLayout.setLayoutParams(params);
		scrollViewLayout.setOrientation(LinearLayout.VERTICAL);
		addDefaultButtons();
		//this.session = Session.getActiveSession();
	    try {
			String result = new FeedService(ID).execute().get();
			Log.i("Kuressaare -Tallinn" , result);
			processResult(result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	    scrollView.addView(scrollViewLayout);
		baseLayout.addView(scrollView);
		
	}

	private void initMenuView() {
		
		Spinner mode = new Spinner(context);
		
		ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
		        R.array.mode, android.R.layout.simple_spinner_item);
		modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mode.setAdapter(modeAdapter);
		
		Spinner route = new Spinner(context);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.kur_tln_route, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		route.setAdapter(adapter);
		
		Spinner sort = new Spinner(context);
		
		ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this,
		        R.array.sort, android.R.layout.simple_spinner_item);
		sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sort.setAdapter(sortAdapter);
		
		LinearLayout friends = getFriendsLayout();
		Button datePicker = new Button(context);
		datePicker.setText("Vali kuupäev");
		datePicker.setOnClickListener(datePickListener(datePicker));
		datePicker.setBackgroundColor(Color.parseColor("#E2E2E2"));
		
		menuView.addView(mode);
		menuView.addView(route);
		menuView.addView(sort);
		menuView.addView(datePicker);
		menuView.addView(friends);
	}

	private OnClickListener datePickListener(final Button datePicker) {
		Calendar calendar = Calendar.getInstance();
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerDialog d = new DatePickerDialog(context,R.style.AppBaseTheme, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						datePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
					}
				}, year, month, day);
				d.show();
			}
		};
	}

	private LinearLayout getFriendsLayout() {
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 80);
		params.setMargins(10, 10, 0, 10);
		LinearLayout friendLayout = new LinearLayout(context);
		friendLayout.setLayoutParams(params);
		CheckBox checkBox = new CheckBox(context);
		TextView friends = getMenuItem(R.string.friends);
		LinearLayout.LayoutParams params2 = 
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params2.setMargins(0, -10, 0, 0);
		
		friends.setLayoutParams(params2);
		friendLayout.addView(checkBox);
		friendLayout.addView(friends);
		return friendLayout;
	}

	@SuppressLint("NewApi")
	private ImageView getMenuIcon() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 7, 0, 0);
		ImageView dropDownIcon = new ImageView(context);
		dropDownIcon.setLayoutParams(params);
		dropDownIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.com_facebook_tooltip_black_bottomnub));
		dropDownIcon.setRotation(-90);
		return dropDownIcon;
	}
	
	private TextView getMenuItem(int id) {
		TextView textView = new TextView(context);
		textView.setText(StringUtils.getString(context, id));
		textView.setTextSize(13);
		textView.setTextColor(Color.parseColor("#7D8289"));
		textView.setTypeface(null, Typeface.BOLD);
		return textView;
	}

	private void processResult(String result) throws JSONException {
		JSONObject object = new JSONObject(result);
		JSONArray dataArray = object.getJSONArray("data");
		JSONObject pagingObject = object.getJSONObject("paging");
		
		scrollView = new FeedScrollView(context, pagingObject.getString("next"));
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) dataArray.get(i);
			String message = jsonObject.getString("message");
			String from = jsonObject.getJSONObject("from").getString("id");
			UserThread thread = new UserThread(from);
			User user = thread.getUser();
			runOnUiThread(new ResultProcessor(context, scrollViewLayout, user, message));
		}
	}

	private void addDefaultButtons() {
		final ImageView menuButton = new ImageView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 10, 10, 10);
		menuButton.setMaxHeight(35);
		menuButton.setLayoutParams(params);
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
		buttonsLayout.addView(menuButton);
		
		TextView header = new TextView(context);
		header.setText(StringUtils.getString(context, R.string.kur_tln));
		LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		headerParams.setMargins(50, 12, 10, 10);
		header.setTextSize(18);
		header.setTypeface(null, Typeface.BOLD_ITALIC);
		header.setTextColor(Color.parseColor("#7D8289"));
		header.setLayoutParams(headerParams);
		buttonsLayout.addView(header);
	}

	@SuppressLint("NewApi")
	private Button createButton(int id) {
		Button button = new Button(context);
		button.setText(StringUtils.getString(context, id));
		button.setBackground(getResources().getDrawable(R.drawable.com_facebook_button_blue));
		button.setTextColor(Color.parseColor("#ffffff"));
		button.setPadding(20, 0, 20, 0);
		return button;
	}
}
