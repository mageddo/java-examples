package jdbc.delete_in_vs_batch;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import testing.DBMigration;
import testing.PostgresExtension;

@ExtendWith(PostgresExtension.class)
class TrashServiceTest {

  Connection connection;

  TrashService trashService;

  TrashDAOPostgres trashDAO;

  @BeforeEach
  void beforeEach() throws SQLException {
    this.connection = DBMigration.migrateEmbeddedPostgres().getConnection();
    this.trashDAO = new TrashDAOPostgres();
    this.trashService  = new TrashService(trashDAO);
  }

  @Test
  void mustInsertAndDeleteUsingIn() throws SQLException {

    // arrange

    // act
    this.trashService.insertAndDeleteUsingIn(this.connection);

    // assert
    assertTrue(this.trashDAO.find(this.connection).isEmpty());

  }

  @Test
  void mustInsertAndDeleteUsingBatch() throws SQLException {

    // arrange

    // act
    this.trashService.insertAndDeleteUsingBatch(this.connection);

    // assert
    assertTrue(this.trashDAO.find(this.connection).isEmpty());

  }


  @Test
  void mustInsertAndDeleteUsingOneByOneStrategy() throws SQLException {

    // arrange

    // act
    this.trashService.insertAndDeleteOneByOne(this.connection);

    // assert
    assertTrue(this.trashDAO.find(this.connection).isEmpty());

  }

  @Test
  void batchVsInDeleteVsOneByOnePerformance() throws SQLException {
    // arrange
    final var stopWatch = StopWatch.createStarted();

    // act
    this.trashService.insertAndDeleteUsingBatch(this.connection);
    final var batchTimeDisp = stopWatch.formatTime();
    final var batchTime = stopWatch.getTime();

    stopWatch.split();
    this.trashService.insertAndDeleteUsingIn(this.connection);
    final var inTimeDisp = stopWatch.formatSplitTime();
    final var inTime = stopWatch.getSplitTime();

    stopWatch.split();
    this.trashService.insertAndDeleteOneByOne(this.connection);
    final var oneByOneTimeDisp = stopWatch.formatSplitTime();
    final var oneByOneTime = stopWatch.getSplitTime();

    // assert
    assertTrue(inTime - batchTime < 10 && inTime - batchTime > 0);
    assertTrue(oneByOneTime - batchTime > 1000);
    System.out.printf("batch=%s, in=%s, oneByOne=%s%n", batchTimeDisp, inTimeDisp, oneByOneTimeDisp);
  }

  @Test
  void mustInsert() throws SQLException {

    // arrange

    // act
    this.trashService.insert(this.connection);

    // assert
    assertEquals(5_000, this.trashDAO.find(this.connection).size());

  }

}
