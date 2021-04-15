package jdbc.delete_in_vs_batch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrashService {

  private final TrashDAO trashDAO;

  public TrashService(TrashDAO trashDAO) {
    this.trashDAO = trashDAO;
  }

  public void insertAndDeleteUsingIn(Connection connection) throws SQLException {
    final var ids = this.insert(connection);
    this.trashDAO.deleteUsingIn(connection, ids);
  }

  public void insertAndDeleteUsingBatch(Connection connection) throws SQLException {
    final var ids = this.insert(connection);
    this.trashDAO.deleteUsingBatch(connection, ids);
  }

  public void insertAndDeleteOneByOne(Connection connection) throws SQLException {
    final var ids = this.insert(connection);
    this.trashDAO.deleteOneByOne(connection, ids);
  }

  List<UUID> insert(Connection connection) throws SQLException {
    final var ids = new ArrayList<UUID>();
    for (int i = 0; i < 100_000; i++) {
      ids.add(UUID.randomUUID());
    }
    this.trashDAO.insert(connection, ids);
    return ids;
  }
}
