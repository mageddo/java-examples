package com.mageddo.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mageddo.jpa.entity.ImportedFileEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ImportedFileDAOHSQLDB implements ImportedFileDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void save(ImportedFileEntity entity) {
		entityManager.persist(entity);
	}

	@Override
	public ImportedFileEntity findByName(String name) {
		return entityManager.createQuery("FROM ImportedFile WHERE name = :name", ImportedFileEntity.class)
			.setParameter("name", name)
			.getSingleResult();
	}
}
