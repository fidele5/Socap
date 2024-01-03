package com.example.socap.model;

import java.io.Serializable;

public class Message implements Serializable{
	private long id;
	private String date;
	private boolean read = false;
	private User user;
	private String snippet;
	public Message(long id, String date, boolean read, User user, String snippet) {
		this.id = id;
		this.date = date;
		this.read = read;
		this.user = user;
		this.snippet = snippet;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public boolean isRead() {
		return read;
	}

	public User getFriend() {
		return user;
	}

	public String getSnippet() {
		return snippet;
	}
}