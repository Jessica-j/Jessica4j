package org.jessica.core.orm.audit;
public interface ISqlAuditor {
	void audit(String id, String sql, Object sqlContext);
}
