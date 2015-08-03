package org.jessica.core.orm.datasources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.jessica.core.orm.datasources.ha.IHADataSourceCreator;
import org.jessica.core.orm.datasources.ha.NonHADataSourceCreator;
import org.jessica.core.orm.datasources.ha.support.IDataSourcePostProcessor;
import org.jessica.core.orm.support.utils.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

public class DefaultormDataSourceService implements IormDataSourceService, InitializingBean {
	private Set<ormDataSourceDescriptor> dataSourceDescriptors = new HashSet<ormDataSourceDescriptor>();
	private List<IDataSourcePostProcessor> dataSourcePostProcessor = new ArrayList<IDataSourcePostProcessor>();
	private IHADataSourceCreator haDataSourceCreator;
	private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

	public Map<String, DataSource> getDataSources() {
		return dataSources;
	}

	public void afterPropertiesSet() throws Exception {
		if (getHaDataSourceCreator() == null) {
			setHaDataSourceCreator(new NonHADataSourceCreator());
		}
		if (CollectionUtils.isEmpty(dataSourceDescriptors)) {
			return;
		}

		for (ormDataSourceDescriptor descriptor : getDataSourceDescriptors()) {
			Validate.notEmpty(descriptor.getIdentity());
			Validate.notNull(descriptor.getTargetDataSource());

			DataSource dataSourceToUse = descriptor.getTargetDataSource();

			if (descriptor.getStandbyDataSource() != null) {
				dataSourceToUse = getHaDataSourceCreator().createHADataSource(descriptor);
				if (CollectionUtils.isNotEmpty(dataSourcePostProcessor)) {
					for (IDataSourcePostProcessor pp : dataSourcePostProcessor) {
						dataSourceToUse = pp.postProcess(dataSourceToUse);
					}
				}
			}

			dataSources.put(descriptor.getIdentity(), new LazyConnectionDataSourceProxy(dataSourceToUse));
		}
	}

	public void setDataSourceDescriptors(Set<ormDataSourceDescriptor> dataSourceDescriptors) {
		this.dataSourceDescriptors = dataSourceDescriptors;
	}

	public Set<ormDataSourceDescriptor> getDataSourceDescriptors() {
		return dataSourceDescriptors;
	}

	public void setHaDataSourceCreator(IHADataSourceCreator haDataSourceCreator) {
		this.haDataSourceCreator = haDataSourceCreator;
	}

	public IHADataSourceCreator getHaDataSourceCreator() {
		return haDataSourceCreator;
	}

}
