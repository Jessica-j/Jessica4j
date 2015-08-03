 package org.jessica.core.orm.datasources.ha.support;

import javax.sql.DataSource;

 
public interface IDataSourcePostProcessor {
	DataSource postProcess(DataSource dataSource);
}
