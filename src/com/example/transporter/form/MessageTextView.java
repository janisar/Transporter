package com.example.transporter.form;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageTextView extends TextView {

	private LinearLayout parent;
	
	boolean sizeChanged = false;
	
	public MessageTextView(Context context, LinearLayout parent) {
		super(context);
		this.parent = parent;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (!sizeChanged) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, h + 40);
			this.setLayoutParams(params);
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, h + 40);
			layoutParams.setMargins(15, 3, 25, 20);
			parent.setLayoutParams(layoutParams);
		}
		sizeChanged = true;
	}
}
