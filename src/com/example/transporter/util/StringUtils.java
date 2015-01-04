package com.example.transporter.util;

import android.content.Context;

public class StringUtils {
	
	public static String getString(Context context, int id) {
		return context.getResources().getString(id);
	}
}
