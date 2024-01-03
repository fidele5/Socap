package com.example.socap.model;

import java.io.Serializable;

public class MessageDetails implements Serializable{
	private long id;
	private String date;
	private User user;
	private String content;
	private boolean fromMe;

	public MessageDetails(long id, String date, User user, String content, boolean fromMe) {
		this.id = id;
		this.date = date;
		this.user = user;
		this.content = content;
		this.fromMe = fromMe;
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
		return content;
	}

	public boolean isFromMe() {
		return fromMe;
	}
}