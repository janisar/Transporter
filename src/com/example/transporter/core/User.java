package com.example.transporter.core;

import android.graphics.drawable.Drawable;


public class User {

	private String name;
	private String imageUrl;
	private Drawable image;
	
	public User(String name, String imageUrl) {
		this.name = name;
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setDrawable(Drawable image) {
		this.image = image;
	}
	
	public Drawable getImage() {
		return image;
	}
}
