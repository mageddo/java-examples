package com.mageddo.jpa.entity;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity(name = "ImportedFile")
@Table(name = "IMPORTED_FILE")
public class ImportedFileEntity {

	private int id;
	private String name;
	private Date creationDate;

	@Id
	@GeneratedValue
	@Column(name = "IDT_IMPORTED_FILE")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "NAM_FILE", unique = true, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DAT_CREATION",nullable = false)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@PrePersist
	public void prepersist(){
		if(getCreationDate() == null){
			setCreationDate(new Date());
		}
	}

	public static ImportedFileEntity newEntity(File file) {
		final ImportedFileEntity importedFile = new ImportedFileEntity();
		importedFile.setName(file.getName());
		return importedFile;
	}
}
