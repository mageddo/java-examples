package com.mageddo.imgur;

import java.io.InputStream;

public class ImageUploadReq {

	private InputStream in;
	private String name;
	private String title;
	private String description;

	public InputStream getIn() {
		return in;
	}

	public ImageUploadReq setIn(InputStream in) {
		this.in = in;
		return this;
	}

	public String getName() {
		return name;
	}

	public ImageUploadReq setName(String name) {
		this.name = name;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public ImageUploadReq setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ImageUploadReq setDescription(String description) {
		this.description = description;
		return this;
	}
}
