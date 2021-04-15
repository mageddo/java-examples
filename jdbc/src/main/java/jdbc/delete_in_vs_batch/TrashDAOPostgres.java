package jdbc.delete_in_vs_batch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

public class TrashDAOPostgres implements TrashDAO {

  @Override
  public void insert(Connection connection, List<UUID> ids) throws SQLException {
    final var sql = "INSERT INTO TRASH VALUES (?)";
    final var stm = connection.prepareStatement(sql);
    try (stm) {
      for (UUID id : ids) {
        stm.setString(1, String.valueOf(id));
        stm.addBatch();
      }
      Validate.isTrue(
          stm.executeBatch().length == ids.size(),
          "Didn't insert expected quantity"
      );
    }
  }

  @Override
  public void deleteUsingIn(Connection connection, List<UUID> ids) throws SQLException {
    final var sql = "DELETE FROM TRASH WHERE IDT_TRASH IN (?)";
    final var stm = connection.prepareStatement(sql);
    try (stm) {
      final var param = ids.stream()
          .map(String::valueOf)
          .collect(Collectors.joining(", "));
      stm.setString(1, param);
      Validate.isTrue(stm.executeUpdate() == ids.size(), "Didn't update all records");
    }
  }

  @Override
  public void deleteUsingBatch(Connection connection, List<UUID> ids) throws SQLException {
    final var sql = "DELETE FROM TRASH WHERE IDT_TRASH = '%s'";
    final var stm = connection.createStatement();
    try (stm) {
      for (final UUID id : ids) {
        stm.addBatch(String.format(sql, id));
      }
      Validate.isTrue(stm.executeBatch().length == ids.size(), "Didn't update all records");
    }
  }

  @Override
  public List<UUID> find(Connection connection) throws SQLException {
    final var sql = "SELECT * FROM TRASH";
    final var stm = connection.prepareStatement(sql);
    try (stm) {
      final var ids = new ArrayList<UUID>();
      try (var rs = stm.executeQuery()) {
        while (rs.next()) {
          ids.add(UUID.fromString(rs.getString("IDT_TRASH")));
        }
      }
      return ids;
    }
  }
}
