package com.example.transporter.core;

public class Entry {
	
	private User user;
	private String date;
	private String text;
	private Route route;
	
	public Entry(User user, String date, String text) {
		this.user = user;
		this.date = date;
		this.text = text;
		this.route = this.route.define(text);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
}
