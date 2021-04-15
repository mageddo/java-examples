package jdbc.delete_in_vs_batch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface TrashDAO {

  void insert(Connection connection, List<UUID> ids) throws SQLException;

  void deleteUsingIn(Connection connection, List<UUID> ids) throws SQLException;

  void deleteUsingBatch(Connection connection, List<UUID> ids) throws SQLException;

  List<UUID> find(Connection connection) throws SQLException;
}
