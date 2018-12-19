package com.mageddo.jpa.dao;

import com.mageddo.jpa.entity.ImportedFileEntity;

public interface ImportedFileDAO {
	void save(ImportedFileEntity entity);
	ImportedFileEntity findByName(String name);
}
