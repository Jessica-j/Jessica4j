 
 package org.jessica.core.orm.audit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

public class ConcurrentSqlAuditor implements ISqlAuditor {
	private ExecutorService executorService;
	
	/**
	 * simple map-reduce results holder 
	 * it's not the final abstraction yet, may be refactored later.
	 */
	private ConcurrentMap<String, Long> statementStatistics = new ConcurrentHashMap<String, Long>();
	
	public void audit(String id, String sql, Object sqlContext) {
		// TODO 
	    // implement application-specific profiling logic here.
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

    public void setStatementStatistics(ConcurrentMap<String, Long> statementStatistics) {
        this.statementStatistics = statementStatistics;
    }

    public ConcurrentMap<String, Long> getStatementStatistics() {
        return statementStatistics;
    }

}
