package com.example.transporter.form;

import java.util.Calendar;

import com.example.transporter.R;
import com.example.transporter.util.StringUtils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuView {

	private Context context;
	private LinearLayout menuView;
	
	public MenuView(Context context, LinearLayout layout) {
		this.context = context;
		this.menuView = layout;
		initMenuView();
	}
	
	private void initMenuView() {
		
		Spinner mode = initSpinner(R.array.mode);
		Spinner route = initSpinner(R.array.kur_tln_route);
		Spinner sort = initSpinner(R.array.sort);
		
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

	private Spinner initSpinner(int id) {
		Spinner sort = new Spinner(context);
		
		ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(context,
		        id, android.R.layout.simple_spinner_item);
		sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sort.setAdapter(sortAdapter);
		return sort;
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

	private TextView getMenuItem(int id) {
		TextView textView = new TextView(context);
		textView.setText(StringUtils.getString(context, id));
		textView.setTextSize(13);
		textView.setTextColor(Color.parseColor("#7D8289"));
		textView.setTypeface(null, Typeface.BOLD);
		return textView;
	}
	
	public LinearLayout getMenuView() {
		return this.menuView;
	}
}
