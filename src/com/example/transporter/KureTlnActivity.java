package com.example.transporter;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_template);
		this.context = KureTlnActivity.this;
		baseLayout = (LinearLayout) findViewById(R.id.baseLayout);
		
		buttonsLayout = (LinearLayout) findViewById(R.id.buttons_layout);
		
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
		Button date = createButton(R.string.choose_date);
		Button route = createButton(R.string.route);
		Button price = createButton(R.string.price);
		Button friends = createButton(R.string.friends);
		
		buttonsLayout.addView(date);
		buttonsLayout.addView(route);
		buttonsLayout.addView(price);
		buttonsLayout.addView(friends);
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
