package com.mageddo.jaxb.writeandparse;

import javax.xml.bind.annotation.*;

//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "book")
@XmlType(name = "book", propOrder = {"publisher", "messageId", "author", "title" })
public class Book {

	private Publisher publisher;
	private GUID messageId;
	private String author;
	private String title;

	@XmlElement(name = "publisher")
	public Publisher getPublisher() {
		return publisher;
	}

	public Book setPublisher(Publisher publisher) {
		this.publisher = publisher;
		return this;
	}

	@XmlElement(name = "messageId")
	public GUID getMessageId() {
		return messageId;
	}

	public Book setMessageId(GUID messageId) {
		this.messageId = messageId;
		return this;
	}

	@XmlElement(name = "author")
	public String getAuthor() {
		return author;
	}

	public Book setAuthor(String author) {
		this.author = author;
		return this;
	}

	@XmlElement(name = "title")
	public String getTitle() {
		return title;
	}

	public Book setTitle(String title) {
		this.title = title;
		return this;
	}
}
