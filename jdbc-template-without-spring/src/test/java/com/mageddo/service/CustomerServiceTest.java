package com.mageddo.service;

import com.mageddo.dao.CustomerDAO;
import com.mageddo.dao.CustomerDAOH2;
import com.mageddo.dao.DatabaseBuilderDAO;
import com.mageddo.dao.DatabaseBuilderDAOH2;
import com.mageddo.utils.DBUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import static com.mageddo.utils.DBUtils.tx;
import static org.junit.Assert.assertEquals;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * @author elvis
 * @version $Revision: $<br/>
 * $Id: $
 * @since 9/14/17 3:23 PM
 */
public class CustomerServiceTest {

	private final CustomerDAO customerDAO = new CustomerDAOH2();
	private final DatabaseBuilderDAO builderDao = new DatabaseBuilderDAOH2();
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void construct(){
		builderDao.buildDatabase();
	}

	@Test
	public void noTransactional() throws Exception {

		assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

		DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

		assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

	}

	@Test
	public void transactionalUpdate() throws Exception {

		new TransactionTemplate(tx()).execute(st -> {

			assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

			DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

			assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

			st.setRollbackOnly();

			return null;

		});

		assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

	}


	@Test
	public void transactionalUpdateWithManualRollback() throws Exception {

		final TransactionStatus transactionStatus = tx().getTransaction(new DefaultTransactionDefinition());

		assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
		DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
		assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

		tx().rollback(transactionStatus);

		assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

	}

	@Test
	public void transactionalUpdateWithRollbackSuccess() throws Exception {

		final TransactionStatus status = tx().getTransaction(new DefaultTransactionDefinition());
		try {
			assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

			DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

			status.setRollbackOnly();

			assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

			tx().commit(status);
		} catch (Exception ex) {
			tx().rollback(status);
			throw ex;
		}

		assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

	}


	@Test
	public void progragationTest() throws Exception {

		new TransactionTemplate(DBUtils.tx(), new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW)).execute(status -> {

			assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
			DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
			assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

			return new TransactionTemplate(DBUtils.tx(), new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW)).execute(status2 -> {
				assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
				return null;
			});

		});

		assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

	}


	@Test
	public void transactionalUpdateWithRollbackAndTransactionTemplateSuccess() throws Exception {

		new TransactionTemplate(tx()).execute(status -> {

			assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

			DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

			status.setRollbackOnly();

			assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());
			return null;

		});

		assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

	}

	/**
	 * Two templates with same transaction
	 *
	 */
	@Test
	public void transactionalUpdateTwoTemplatesWithSameTransactionRollback() throws Exception {

		final DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		try {

			new TransactionTemplate(tx(), def).execute(status -> {

				assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
				DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
				assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

				return new TransactionTemplate(tx(), def).execute(status2 -> {
					DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X2' WHERE FIRST_NAME='Jeff'");
					assertEquals("X2", customerDAO.findFirstByName("Jeff").getLastName());
					status2.setRollbackOnly();
					return null;
				});

			});

		}catch (Exception e){
			logger.error("msg={}", e.getMessage(), e);
		}

		assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
		assertEquals("Dean", customerDAO.findFirstByName("Jeff").getLastName());

	}

	/**
	 * Force second transaction template to use another transaction
	 */
	@Test
	public void transactionalUpdateTwoTemplatesUsingRequiresNewRollbackError() throws Exception {

		final DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		try {

			new TransactionTemplate(tx(), def).execute(status -> {

				assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
				DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
				assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

				return new TransactionTemplate(tx(), new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW)).execute(status2 -> {
					DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X2' WHERE FIRST_NAME='Jeff'");
					assertEquals("X2", customerDAO.findFirstByName("Jeff").getLastName());
					status2.setRollbackOnly();
					return null;
				});

			});

		}catch (Exception e){
			logger.error("msg={}", e.getMessage(), e);
		}

		assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());
		assertEquals("Dean", customerDAO.findFirstByName("Jeff").getLastName());

	}

	/**
	 * Mesmo sendo REQUIRED, sao usadas duas transacoes porque para o Spring reutilizar a transacao
	 * eh necessario que um transactionTemplate esteja dentro do outro porque o commit soh sera feito no ato
	 *
	 */
	@Test
	public void transactionalUpdateTwoTemplatesWithDifferentTransactionsRollbackError() throws Exception {

		final DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		try {

			new TransactionTemplate(tx(), def).execute(status -> {

					assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
					DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
					assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

					return null;

				});

				new TransactionTemplate(tx(), def).execute(status2 -> {
					DBUtils.template().execute("UPDATE CUSTOMERS SET LAST_NAME='X2' WHERE FIRST_NAME='Jeff'");
					assertEquals("X2", customerDAO.findFirstByName("Jeff").getLastName());
					status2.setRollbackOnly();
					return null;
				});

		}catch (Exception e){
			logger.error("msg={}", e.getMessage(), e);
		}

		assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());
		assertEquals("Dean", customerDAO.findFirstByName("Jeff").getLastName());

	}

}
