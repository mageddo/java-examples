package com.mageddo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql", "classpath:balance_test.sql"})
public class BalanceTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSource dataSource;

	/**
	 *
	 * One of the two updates must fail by the concurrency issues, preventing isolation problems when updating the user balance,
	 * this update will guarantee balance consistence
	 */
	@Test
	public void balanceUpdate__ValidatingSerializableIsolation() throws Exception {

		final BalanceRequest[] balanceRequests = {new BalanceRequest(10.0, 6000), new BalanceRequest(20.0, 3000)};
		final List<String> results = Flux.just(balanceRequests).parallel()
			.runOn(Schedulers.elastic())
			.map(b -> {
				try {
					return subtractBalance(b.subValue, b.sleepTime, Connection.TRANSACTION_SERIALIZABLE);
				} catch (Exception e) {
					return e.getMessage();
				}
			})
			.sequential()
			.collectSortedList().block(Duration.of(50, ChronoUnit.SECONDS));

			assertThat(results, hasSize(2));
			assertThat(results, contains("30.0", "transaction rollback: serialization failure"));

	}

	/**
	 * Read commited have not a concurrency controll, by this reason the two updates will be successful the get different
	 * values to customer balance having isolation issues
	 */
	@Test
	public void balanceUpdate__ValidatingReadCommittedIsolation() throws Exception {

		final BalanceRequest[] balanceRequests = {new BalanceRequest(10.0, 6000), new BalanceRequest(20.0, 3000)};
		final List<String> results = Flux.just(balanceRequests).parallel()
			.runOn(Schedulers.elastic())
			.map(b -> {
				try {
					return subtractBalance(b.subValue, b.sleepTime, Connection.TRANSACTION_READ_COMMITTED);
				} catch (Exception e) {
					return e.getMessage();
				}
			})
			.sequential()
			.collectSortedList().block(Duration.of(50, ChronoUnit.SECONDS));

		assertThat(results, hasSize(2));
		assertThat(results, contains("30.0", "40.0"));

	}

	private String subtractBalance(double subValue, long millis, int isolation) throws SQLException, InterruptedException {

		try (final Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(isolation);
			logger.info("status=start, subValue={}, millis={}", subValue, millis);
			try (
				final PreparedStatement stm = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE ID=1");
				final ResultSet rs = stm.executeQuery()
			) {
				int i = 0;
				for (; rs.next(); i++) {
					final double balance = rs.getDouble("BALANCE");
					final int id = rs.getInt("ID");
					logger.info("subValue={}, millis={}, originalBalance={}", subValue, millis, balance);
					Thread.sleep(millis);
					try (
						final PreparedStatement stm2 = conn.prepareStatement("UPDATE CUSTOMER SET BALANCE=? WHERE ID=?")
					) {
						stm2.setDouble(1, balance - subValue);
						stm2.setInt(2, id);
						logger.info("updated={}, millis={}, subValue={}", stm2.executeUpdate(), millis, subValue);
					}

				}
				try (
					final ResultSet rs2 = stm.executeQuery()
				) {
					for (; rs2.next();) {
						final String balance = String.valueOf(rs2.getDouble("BALANCE"));
						logger.info("finalBalance={}", balance);
						return balance;
					}
				} finally {
					conn.setAutoCommit(true);
				}

				logger.info("status=end, i={}", i);
			}

		}
		return "ok";
	}

	class BalanceRequest {

		double subValue;
		long sleepTime;

		public BalanceRequest(double subValue, long sleepTime) {
			this.subValue = subValue;
			this.sleepTime = sleepTime;
		}
	}

}
