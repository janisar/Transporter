package com.example.transporter.activity;


import java.util.Arrays;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transporter.R;
import com.example.transporter.util.StringUtils;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

public class MainActivity extends Activity {

	private LoginButton loginButton;
	private Button krTlnButton, hpsTlnButton;
	private RelativeLayout baseLayout;
	private TextView greeting;
	private TextView userName;
	private Context context;
	private Session session;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_main);
		setDefaultLocale();
		this.context = MainActivity.this;
		initGreetingTextView();
		initLoginButton();
		initButtons();
		
		baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
		baseLayout.addView(loginButton);
		authUser();  
	}

	private void initButtons() {
		initButton(krTlnButton, R.id.kur_tln, R.string.kur_tln, buttonListener("android.intent.action.KURETLN"));
		initButton(hpsTlnButton, R.id.hps_tln, R.string.hps_tln, buttonListener("android.intent.action.HPSTLN"));
	}

	@SuppressLint("NewApi")
	private void initButton(Button button, int id, int text, OnClickListener listener) {
		button = (Button) findViewById(id);
		button.setText(StringUtils.getString(context, text));
		button.setTextColor(Color.parseColor("#ffffff"));
		button.setBackground(getResources().getDrawable(R.drawable.com_facebook_button_blue));
		button.setOnClickListener(listener);
	}

	private OnClickListener buttonListener(final String intentName) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(intentName);
				intent.putExtra("userName", userName.getText());
				startActivity(intent);
			}
		};
	}

	
	private void authUser() {
		session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
	        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	          
	        	@Override
	            public void onCompleted(GraphUser user, Response response) {
	                if (session == Session.getActiveSession()) {
	                    if (user != null) {
	                        String profileName = user.getName();
	                        userName = new TextView(context);
	                        userName.setText(profileName);
	                        greeting.setText(StringUtils.getString(context, R.string.greeting) + " " + profileName + "!");
	                        greeting.setTextColor(Color.parseColor("#AFAFAF"));
	                        greeting.setVisibility(View.VISIBLE);
	                        loginButton.setVisibility(View.INVISIBLE);
	                    }   
	                }   
	            }   
	        }); 
	        Request.executeBatchAsync(request);
	    }
	}

	private void initGreetingTextView() {
		greeting = (TextView) findViewById(R.id.greeting_text);
		greeting.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("NewApi")
	private void initLoginButton() {
		loginButton = new LoginButton(MainActivity.this);
		loginButton.setTextColor(Color.parseColor("#FFFFFF"));
		loginButton.setTextSize(12);
		loginButton.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public void onError(FacebookException arg0) {
				Log.e("MainActivity", arg0.getMessage());
				Toast.makeText(MainActivity.this, StringUtils.getString(context, R.string.facebook_login_error), Toast.LENGTH_SHORT).show();
			}
		});
		loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_friends"));
		loginButton.setSessionStatusCallback(new Session.StatusCallback() {
			   
			   @Override
			   public void call(Session activeSession, SessionState state, Exception exception) {
				   if (activeSession.isOpened()) {
					   loginButton.setText(StringUtils.getString(context, R.string.log_out_button));
					   session = activeSession;
					   authUser();
				   } else {
					  loginButton.setTag(StringUtils.getString(context, R.string.log_in));
				   }
			   }
		});
		loginButton.setBackground(getResources().getDrawable(R.drawable.com_facebook_button_blue_normal));
	}

	private void setDefaultLocale() {
		String languageToLoad  = "ee";
	    Locale locale = new Locale(languageToLoad); 
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		session = Session.getActiveSession();
		session.onActivityResult(this, requestCode, resultCode, data);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
