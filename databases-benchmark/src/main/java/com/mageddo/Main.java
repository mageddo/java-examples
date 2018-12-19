package com.mageddo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StopWatch;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@SpringBootApplication
@EnableTransactionManagement
public class Main {
	public static void main(String[] args) throws InterruptedException, SQLException {
		final ConfigurableApplicationContext context = SpringApplication.run(Main.class);
		final DataSource dataSource = context.getBean(DataSource.class);
		final Connection conn = dataSource.getConnection();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		final ResultSet rs = conn.prepareStatement("SELECT * FROM PERSON").executeQuery();
		while(rs.next()){
			rs.getString("name");
		}
		stopWatch.stop();
		System.out.println("select: " + stopWatch.getTotalTimeMillis());
	}

	public static void insert(DataSource dataSource) throws SQLException {
		final Connection conn = dataSource.getConnection();
		conn.setAutoCommit(false);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for(int i = 0; i < 50_000;i++){
			final PreparedStatement stm = conn.prepareStatement("insert into person (NAME) values (?)");
			stm.setString(1, UUID.randomUUID().toString());
			stm.execute();
			stm.close();

		}
		stopWatch.stop();
		System.out.println("insert: " + stopWatch.getTotalTimeMillis());
		conn.setAutoCommit(true);
		conn.close();
	}

}
