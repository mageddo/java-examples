package ex07;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class JsonMapper implements ColumnMapper<Json> {
  @Override
  public Json map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
    return new Json(r.getString(columnNumber));
  }
}
