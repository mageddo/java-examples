package com.mageddo.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private int id;

	@Column(length = 255, nullable = false)
	@Lob
	@Basic(fetch = FetchType.LAZY)

	private String name;

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
