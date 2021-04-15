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
    final var sql = "DELETE FROM TRASH WHERE IDT_TRASH IN ('%s')";
    final var stm = connection.createStatement();
    try (stm) {
      int skip = 0;
      while (true) {

        final var batchIds = ids
            .stream()
            .skip(skip)
            .limit(1000)
            .collect(Collectors.toList());

        if (batchIds.isEmpty()) {
          break;
        }
        skip += batchIds.size();

        final var param = batchIds.stream()
            .map(String::valueOf)
            .collect(Collectors.joining("', '"));
        final var affected = stm.executeUpdate(String.format(sql, param));

        Validate.isTrue(
            affected == batchIds.size(),
            "Didn't delete all records, expected=%d, actual=%d",
            batchIds.size(), affected
        );
      }
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
  public void deleteOneByOne(Connection connection, List<UUID> ids) throws SQLException {
    for (UUID id : ids) {
      this.deleteOneByOne(connection, id);
    }
  }

  public void deleteOneByOne(Connection connection, UUID id) throws SQLException {
    final var sql = "DELETE FROM TRASH WHERE IDT_TRASH = ?";
    final var stm = connection.prepareStatement(sql);
    try (stm) {
      stm.setString(1, id.toString());
      Validate.isTrue(stm.executeUpdate() == 1, "Didn't update record");
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
