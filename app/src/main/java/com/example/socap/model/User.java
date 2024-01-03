package com.example.socap.model;

import java.io.Serializable;

public class User implements Serializable {
	private int id;
	private String name;
	private int photo;

	public User(int id, String name, int photo) {
		this.id = id;
		this.name = name;
		this.photo = photo;
	}

	public User(String name, int photo) {
		this.name = name;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPhoto() {
		return photo;
	}
}
