 
 package org.jessica.core.orm.datasources.ha;

import javax.sql.DataSource;

import org.jessica.core.orm.datasources.ormDataSourceDescriptor;
 
public class NonHADataSourceCreator implements IHADataSourceCreator {

    public DataSource createHADataSource(ormDataSourceDescriptor descriptor) throws Exception {
        return descriptor.getTargetDataSource();
    }

}
