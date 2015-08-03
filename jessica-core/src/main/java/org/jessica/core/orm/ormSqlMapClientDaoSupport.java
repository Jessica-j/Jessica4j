package org.jessica.core.orm;

import java.sql.SQLException;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;

public class ormSqlMapClientDaoSupport extends SqlMapClientDaoSupport {

	public int batchInsert(final String statementName, final Collection<?> entities) throws DataAccessException {
		if (isPartitionBehaviorEnabled()) {
			int counter = 0;
			DataAccessException lastEx = null;
			for (Object parameterObject : entities) {
				try {
					getSqlMapClientTemplate().insert(statementName, parameterObject);
					counter++;
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return counter;
		} else {
			return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
				public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
					executor.startBatch();
					for (Object item : entities) {
						executor.insert(statementName, item);
					}
					return executor.executeBatch();
				}
			});
		}
	}

	public int batchDelete(final String statementName, final Collection<?> entities) throws DataAccessException {
		if (isPartitionBehaviorEnabled()) {
			int counter = 0;
			DataAccessException lastEx = null;
			for (Object entity : entities) {
				try {
					counter += getSqlMapClientTemplate().delete(statementName, entity);
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return counter;
		} else {
			return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
				public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
					executor.startBatch();
					for (Object parameterObject : entities) {
						executor.delete(statementName, parameterObject);
					}
					return executor.executeBatch();
				}
			});
		}
	}

	public int batchUpdate(final String statementName, final Collection<?> entities) throws DataAccessException {
		if (isPartitionBehaviorEnabled()) {
			int counter = 0;
			DataAccessException lastEx = null;
			for (Object parameterObject : entities) {
				try {
					counter += getSqlMapClientTemplate().update(statementName, parameterObject);
				} catch (DataAccessException e) {
					lastEx = e;
				}
			}
			if (lastEx != null) {
				throw lastEx;
			}
			return counter;
		} else {
			return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {

				public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
					executor.startBatch();
					for (Object parameterObject : entities) {
						executor.update(statementName, parameterObject);
					}
					return executor.executeBatch();
				}
			});
		}
	}

	protected boolean isPartitionBehaviorEnabled() {
		if (getSqlMapClientTemplate() instanceof ormSqlMapClientTemplate) {
			return ((ormSqlMapClientTemplate) getSqlMapClientTemplate()).isPartitioningBehaviorEnabled();
		}
		return false;
	}
}
