package com.example.socap.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageDetails implements Serializable{
	@SerializedName("id")
	private long id;
	@SerializedName("date")
	private String date;
	@SerializedName("to_user")
	private User user;
	@SerializedName("text")
	private String content;
	@SerializedName("self")
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