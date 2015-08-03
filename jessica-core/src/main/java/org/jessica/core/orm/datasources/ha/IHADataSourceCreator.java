 package org.jessica.core.orm.datasources.ha;

import javax.sql.DataSource;

import org.jessica.core.orm.datasources.ormDataSourceDescriptor;
 
public interface IHADataSourceCreator {
	DataSource createHADataSource(ormDataSourceDescriptor descriptor) throws Exception;
}
