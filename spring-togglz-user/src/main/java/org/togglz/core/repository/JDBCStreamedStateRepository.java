package org.togglz.core.repository;

import org.togglz.core.repository.featurestate.StreamFeatureState;
import org.togglz.core.Feature;
import org.togglz.core.util.Validate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCStreamedStateRepository implements StateRepository {

	private final DataSource dataSource;
	private final String featureTable;
	private final String parameterTable;

	public JDBCStreamedStateRepository(DataSource dataSource, String featureTable, String parameterTable) {
		this.dataSource = dataSource;
		this.featureTable = featureTable;
		this.parameterTable = parameterTable;
	}

	/**
	 * Query timeout in seconds
	 */
	int queryTimeout() {
		return 5;
	}

	@Override
	public FeatureState getFeatureState(Feature feature) {
		return fillFeatureAttrs(new StreamFeatureState(feature, this::getParameter));
	}

	@Override
	public void setFeatureState(FeatureState featureState) {
		if(fillFeatureAttrs(new FeatureState(featureState.getFeature())) == null){
			insertFeatureState(featureState);
		} else {
			updateFeatureState(featureState);
		}
		for (final String parameterName : featureState.getParameterNames()) {
			setParameter(featureState.getFeature().name(), parameterName, featureState.getParameter(parameterName));
		}
	}

	void updateFeatureState(FeatureState featureState) {
		final StringBuilder sql = new StringBuilder()
			.append("UPDATE ").append(featureTable).append(" SET \n")
			.append("	FLG_ACTIVE = ?, \n")
			.append("	COD_STRATEGY = ? \n")
			.append("WHERE NAM_FEATURE = ? \n");
		execute(sql.toString(), stm -> {
			stm.setInt(1, featureState.isEnabled() ? 1 : 0);
			stm.setString(2, featureState.getStrategyId());
			stm.setString(3, featureState.getFeature().name());
			Validate.isTrue(stm.executeUpdate() == 1, "Must update exactly one feature: " + featureState.getFeature().name());
			return null;
		});
	}

	void insertFeatureState(FeatureState featureState) {
		final StringBuilder sql = new StringBuilder()
			.append("INSERT INTO ").append(featureTable).append(" \n")
			.append("	(NAM_FEATURE, FLG_ACTIVE, COD_STRATEGY) VALUES \n")
			.append("	(?, ?, ?) \n");
		execute(sql.toString(), stm -> {
			stm.setString(1, featureState.getFeature().name());
			stm.setInt(2, featureState.isEnabled() ? 1 : 0);
			stm.setString(3, featureState.getStrategyId());
			Validate.isTrue(stm.executeUpdate() == 1, "Must insert exactly one feature: " + featureState.getFeature().name());
			return null;
		});
	}

	FeatureState fillFeatureAttrs(FeatureState feature) {
		final StringBuilder sql = new StringBuilder()
			.append("SELECT \n")
			.append("	FLG_ACTIVE, COD_STRATEGY \n")
			.append("FROM ")
			.append(featureTable)
			.append(" \n")
			.append("WHERE NAM_FEATURE = ? \n");
		return execute(sql.toString(), stm -> {
			stm.setString(1, feature.getFeature().name());
			stm.setQueryTimeout(queryTimeout());
			final ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				feature.setEnabled(rs.getInt("FLG_ACTIVE") > 0);
				feature.setStrategyId(rs.getString("COD_STRATEGY"));
				return feature;
			}
			return null;
		});
	}

	/**
	 * Load parameter value from database
	 */
	String getParameter(String feature, String parameter) {
		final StringBuilder sql = new StringBuilder()
			.append("SELECT \n")
			.append("	VAL_PARAMETER \n")
			.append("FROM ")
			.append(parameterTable)
			.append(" \n")
			.append("WHERE NAM_FEATURE = ? AND NAM_PARAMETER = ? \n");
		return execute(sql.toString(), stm -> {
			stm.setString(1, feature);
			stm.setString(2, parameter);
			stm.setQueryTimeout(queryTimeout());
			final ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getString("VAL_PARAMETER");
			}
			return null;
		});
	}

	void setParameter(String feature, String parameter, String value) {
		if(getParameter(feature, parameter) == null){
			insertParameter(feature, parameter, value);
		} else {
			updateParameter(feature, parameter, value);
		}
	}

	void insertParameter(String feature, String parameter, String value) {
		final StringBuilder sql = new StringBuilder()
			.append("INSERT INTO ")
			.append(parameterTable).append(" \n")
			.append("(NAM_FEATURE, NAM_PARAMETER, VAL_PARAMETER) \n ")
			.append("VALUES (?, ?, ?)")
			;
		execute(sql.toString(), stm -> {
			stm.setString(1, feature);
			stm.setString(2, parameter);
			stm.setString(3, value);
			stm.setQueryTimeout(queryTimeout());
			Validate.isTrue(
			stm.executeUpdate() == 1,
				String.format("Must insert exactly one record, feature=%s, parameter=%s", feature, parameter
			));
			return null;
		});
	}

	void updateParameter(String feature, String parameter, String value) {
		final StringBuilder sql = new StringBuilder()
			.append("UPDATE ")
			.append(parameterTable).append(" SET \n")
			.append("\t VAL_PARAMETER = ? \n")
			.append("WHERE NAM_FEATURE = ? AND NAM_PARAMETER = ? \n")
			;
		execute(sql.toString(), (stm) -> {
			stm.setString(1, value);
			stm.setString(2, feature);
			stm.setString(3, parameter);
			stm.setQueryTimeout(queryTimeout());
			Validate.isTrue(
				stm.executeUpdate() == 1,
				String.format("Must update exactly one record, feature=%s, parameter=%s", feature, parameter
			));
			return null;
		});

	}

	/**
	 * Make it easiest to run sql statements
	 */
	<R>R execute(final String sql, StmConsumer<R> fn){
		try (
			final Connection conn = dataSource.getConnection();
			final PreparedStatement stm = conn.prepareStatement(sql)
		) {
			return fn.accept(stm);
		} catch (SQLException e) {
			throw new IllegalStateException(
				String.format("Database returned an error: %s", e.getMessage()), e
			);
		}
	}

	@FunctionalInterface
	interface StmConsumer<R> {
		R accept(PreparedStatement stm) throws SQLException;
	}

}
