package com.mageddo.jpa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/30/17 3:08 PM
 */
@Entity
public class Car {

	/**
	 * @see <a href="http://stackoverflow.com/questions/31158509/how-to-generate-custom-id-using-hibernate-while-it-must-be-primary-key-of-table">Custom ID generator</a>
	 */
	@Id
	@GenericGenerator(name = "carIdGenerator", strategy = "com.mageddo.jpa.generator.CarGenerator")
	@GeneratedValue(generator = "carIdGenerator")
	private String id;

	public Car() {
	}

	public Car(String model, int year) {
		this.model = model;
		this.year = year;
	}

	@Column
	private String model;

	@Column
	private int year;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
