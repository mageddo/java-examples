package com.mageddo.sqldatapartitioning.utils;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class JdbcHelper {

	public static void setInteger(PreparedStatement ps, int offset, Integer value) throws SQLException {
		ps.setObject(offset, value, Types.INTEGER);
	}

	public static void setLong(PreparedStatement ps, int offset, Long value) throws SQLException {
		ps.setObject(offset, value, Types.BIGINT);
	}

	public static Integer getInteger(ResultSet rs, String label) throws SQLException {
		return getInteger(rs, label, null);
	}

	public static Integer getInteger(ResultSet rs, String label, Integer defaultValue) throws SQLException {
		return Optional.ofNullable(rs.getObject(label, Integer.class)).orElse(defaultValue);
	}

	public static Long getLong(ResultSet rs, String col) throws SQLException {
		return getLong(rs, col, null);
	}

	public static Long getLong(ResultSet rs, String col, Long defaultValue) throws SQLException {
		return Optional.ofNullable(rs.getObject(col, Long.class)).orElse(defaultValue);
	}

	public static LocalDate getLocalDate(ResultSet rs, String label) throws SQLException {
		return getLocalDate(rs, label, null);
	}

	public static LocalDate getLocalDate(ResultSet rs, String label, LocalDate defaultValue) throws SQLException {
		final Date v = rs.getDate(label);
		if (v == null) {
			return defaultValue;
		}
		return v.toLocalDate();
	}

	public static LocalDateTime getLocalDateTime(ResultSet rs, String label) throws SQLException {
		return getLocalDateTime(rs, label, null);
	}

	public static LocalDateTime getLocalDateTime(ResultSet rs, String label, LocalDateTime defaultValue) throws SQLException {
		final Timestamp v = rs.getTimestamp(label);
		if (v == null) {
			return defaultValue;
		}
		return v.toLocalDateTime();
	}

	public static Double getDouble(ResultSet rs, String label) throws SQLException {
		return getDouble(rs, label, null);
	}

	private static Double getDouble(ResultSet rs, String label, Double defaultValue) throws SQLException {
		return Optional.ofNullable(rs.getString(label)).map(Double::valueOf).orElse(defaultValue);
	}

	public static String getString(ResultSet rs, String label, String defaultValue) throws SQLException {
		return Optional.ofNullable(rs.getString(label)).orElse(defaultValue);
	}

	public static BigDecimal getBigDecimal(ResultSet rs, String label, BigDecimal defaultValue) throws SQLException {
		return Optional.ofNullable(rs.getBigDecimal(label)).orElse(defaultValue);
	}

}
