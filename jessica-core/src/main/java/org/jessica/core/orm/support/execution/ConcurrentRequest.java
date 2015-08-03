package org.jessica.core.orm.support.execution;

import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import org.springframework.orm.ibatis.SqlMapClientCallback;

/**
 * {@link #action} will be executed on {@link #dataSource} with
 * {@link #executor} asynchronously.<br>
 * 
 * @author fujohnwang
 * @since 1.0
 */
public class ConcurrentRequest {
	private SqlMapClientCallback action;
	private DataSource dataSource;
	private ExecutorService executor;

	public SqlMapClientCallback getAction() {
		return action;
	}

	public void setAction(SqlMapClientCallback action) {
		this.action = action;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

}
