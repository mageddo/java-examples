package com.mageddo.jpa.service;

import java.io.File;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import utils.IntegratedTest;

@RunWith(SpringRunner.class)
@IntegratedTest
public class ImportedFileServiceTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Autowired
	private ImportedFileService importedFileService;

	@Test
	public void importFile_successImportAndFileiIsDeleted() throws Exception {

		// assert
		final File f = temporaryFolder.newFile("tmp.tmp");

		// act
		importedFileService.importFile(f);

		// assert
		Assert.assertEquals("tmp.tmp", importedFileService.findByName(f.getName()).getName());
		Assert.assertFalse(f.exists());
	}

	@Test
	@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:import_file.sql")
	public void importFile_successImportAndFileDeleted() throws Exception {

		// arrange
		final File f = temporaryFolder.newFile("tmp.tmp");

		// act
		try {
			importedFileService.importFile(f);
		}catch (Exception e){
			Assert.assertEquals(DataIntegrityViolationException.class, e.getClass());
		}

		// assert
		Assert.assertEquals("tmp.tmp", importedFileService.findByName(f.getName()).getName());
		Assert.assertTrue(f.exists());
	}

}
