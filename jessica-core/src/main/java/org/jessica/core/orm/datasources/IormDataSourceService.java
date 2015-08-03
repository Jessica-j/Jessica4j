 package org.jessica.core.orm.datasources;

import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
 
public interface IormDataSourceService {
	Map<String, DataSource> getDataSources();
	Set<ormDataSourceDescriptor> getDataSourceDescriptors();
}
