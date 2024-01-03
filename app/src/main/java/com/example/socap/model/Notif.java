package com.example.socap.model;

import java.io.Serializable;

public class Notif implements Serializable{
	private long id;
	private String date;
	private User user;
	private String content;

	public Notif(long id, String date, User user, String content) {
		this.id = id;
		this.date = date;
		this.user = user;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public User getFriend() {
		return user;
	}

	public String getContent() {
		return "<b>"+ user.getName()+"</b> "+content;
	}
}