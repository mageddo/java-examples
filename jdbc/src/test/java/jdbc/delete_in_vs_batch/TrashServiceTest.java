package jdbc.delete_in_vs_batch;

import java.sql.Connection;
import java.sql.SQLException;

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
  void mustInsert() throws SQLException {

    // arrange

    // act
    this.trashService.insert(this.connection);

    // assert
    assertEquals(5_000, this.trashDAO.find(this.connection).size());

  }

}
