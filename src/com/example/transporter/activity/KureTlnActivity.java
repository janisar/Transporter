package com.example.transporter.activity;

import com.example.transporter.R;


public class KureTlnActivity extends AbstractActivity {
	
	private static final String ID = "273758919316437/feed";

	@Override
	protected String getCommunityId() {
		return ID;
	}

	@Override
	protected String getCommunityName() {
		return getResources().getString(R.string.kur_tln);
	}
}
