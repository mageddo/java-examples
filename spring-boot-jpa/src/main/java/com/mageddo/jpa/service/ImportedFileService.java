package com.mageddo.jpa.service;

import java.io.File;

import com.mageddo.jpa.dao.ImportedFileDAO;
import com.mageddo.jpa.entity.ImportedFileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class ImportedFileService {

	private final ImportedFileDAO importedFileDAO;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public ImportedFileService(ImportedFileDAO importedFileDAO) {
		this.importedFileDAO = importedFileDAO;
	}

	@Transactional
	public ImportedFileEntity importFile(File file){
		if(!file.exists() || !file.canWrite() || !file.canRead() || file.isDirectory()){
			throw new RuntimeException("Invalid file, check if it exists is not a directory or if you have permissions to manage it");
		}
		TransactionSynchronizationManager.registerSynchronization(new FileDeletioner(file));

		final ImportedFileEntity importedFile = ImportedFileEntity.newEntity(file);
		importedFileDAO.save(importedFile);
		return importedFile;
	}

	@Transactional
	public ImportedFileEntity findByName(String name){
		return importedFileDAO.findByName(name);
	}

	static class FileDeletioner extends TransactionSynchronizationAdapter {
	  private final Logger logger = LoggerFactory.getLogger(getClass());
		private final File file;

		FileDeletioner(final File file) {
			this.file = file;
		}

		@Override
		public void afterCompletion(final int status) {
			logger.info("file={}, status={}", file, status);
			switch (status) {
			case STATUS_COMMITTED:
				logger.info("file={}, deleted={}", file, file.delete());
				break;
			}
		}
	}
}
