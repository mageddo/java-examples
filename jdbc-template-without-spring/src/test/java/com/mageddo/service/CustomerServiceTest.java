package com.mageddo.service;

import static com.mageddo.utils.DBUtils.getTx;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

import com.mageddo.dao.CustomerDAO;
import com.mageddo.dao.CustomerDAOH2;
import com.mageddo.dao.DatabaseBuilderDAO;
import com.mageddo.dao.DatabaseBuilderDAOH2;
import com.mageddo.utils.DBUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

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

		Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

		DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

		Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

	}

	@Test
	public void transactionalUpdate() throws Exception {


		new TransactionTemplate(getTx()).execute(st -> {

			Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

			DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

			Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

			st.setRollbackOnly();

			return null;

		});

		Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

	}

	@Test
	public void transactionalUpdateWithRollbackSuccess() throws Exception {

		final TransactionStatus status = getTx().getTransaction(new DefaultTransactionDefinition());
		try {
			Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

			DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

			status.setRollbackOnly();

			Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

			getTx().commit(status);
		} catch (Exception ex) {
			getTx().rollback(status);
			throw ex;
		}

		Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

	}


	@Test
	public void progragationTest() throws Exception {

		new TransactionTemplate(DBUtils.getTx(), new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW)).execute(status -> {

			Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
			DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
			Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

			return new TransactionTemplate(DBUtils.getTx(), new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW)).execute(status2 -> {
				Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
				return null;
			});

		});

		Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

	}


	@Test
	public void transactionalUpdateWithRollbackAndTransactionTemplateSuccess() throws Exception {

		new TransactionTemplate(getTx()).execute(status -> {

			Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

			DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");

			status.setRollbackOnly();

			Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());
			return null;

		});

		Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());

	}

	/**
	 * Two templates with same transaction
	 *
	 */
	@Test
	public void transactionalUpdateTwoTemplatesWithSameTransactionRollback() throws Exception {

		final DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		try {

			new TransactionTemplate(getTx(), def).execute(status -> {

				Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
				DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
				Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

				return new TransactionTemplate(getTx(), def).execute(status2 -> {
					DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X2' WHERE FIRST_NAME='Jeff'");
					Assert.assertEquals("X2", customerDAO.findFirstByName("Jeff").getLastName());
					status2.setRollbackOnly();
					return null;
				});

			});

		}catch (Exception e){
			logger.error("msg={}", e.getMessage(), e);
		}

		Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
		Assert.assertEquals("Dean", customerDAO.findFirstByName("Jeff").getLastName());

	}

	/**
	 * Force second transaction template to use another transaction
	 */
	@Test
	public void transactionalUpdateTwoTemplatesUsingRequiresNewRollbackError() throws Exception {

		final DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		try {

			new TransactionTemplate(getTx(), def).execute(status -> {

				Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
				DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
				Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

				return new TransactionTemplate(getTx(), new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW)).execute(status2 -> {
					DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X2' WHERE FIRST_NAME='Jeff'");
					Assert.assertEquals("X2", customerDAO.findFirstByName("Jeff").getLastName());
					status2.setRollbackOnly();
					return null;
				});

			});

		}catch (Exception e){
			logger.error("msg={}", e.getMessage(), e);
		}

		Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());
		Assert.assertEquals("Dean", customerDAO.findFirstByName("Jeff").getLastName());

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

			new TransactionTemplate(getTx(), def).execute(status -> {

					Assert.assertEquals("Long", customerDAO.findFirstByName("Mark").getLastName());
					DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X' WHERE FIRST_NAME='Mark'");
					Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());

					return null;

				});

				new TransactionTemplate(getTx(), def).execute(status2 -> {
					DBUtils.getTemplate().execute("UPDATE CUSTOMERS SET LAST_NAME='X2' WHERE FIRST_NAME='Jeff'");
					Assert.assertEquals("X2", customerDAO.findFirstByName("Jeff").getLastName());
					status2.setRollbackOnly();
					return null;
				});

		}catch (Exception e){
			logger.error("msg={}", e.getMessage(), e);
		}

		Assert.assertEquals("X", customerDAO.findFirstByName("Mark").getLastName());
		Assert.assertEquals("Dean", customerDAO.findFirstByName("Jeff").getLastName());

	}

}
