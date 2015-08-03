package org.jessica.core.orm.router.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jessica.core.orm.router.config.vo.InternalRule;
import org.jessica.core.orm.router.config.vo.InternalRules;
import org.jessica.core.orm.support.utils.CollectionUtils;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * {@link DefaultormClientInternalRouterXmlFactoryBean} will load rule
 * definitions from external xml configuration files.<br>
 * if you want to directly define rules in spring's IoC Container, see
 * {@link StaticormClientInternalRouterFactoryBean}.
 * 
 * @see StaticormClientInternalRouterFactoryBean
 */
public class DefaultormClientInternalRouterXmlFactoryBean extends AbstractormClientInternalRouterFactoryBean {

	private Resource configLocation;
	private Resource[] configLocations;

	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public Resource[] getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(Resource[] configLocations) {
		this.configLocations = configLocations;
	}

	@Override
	protected List<InternalRule> loadRulesFromExternal() throws IOException {
		XStream xstream = new XStream();
		xstream.alias("rules", InternalRules.class);
		xstream.alias("rule", InternalRule.class);
		xstream.addImplicitCollection(InternalRules.class, "rules");
		xstream.useAttributeFor(InternalRule.class, "merger");

		List<InternalRule> rules = new ArrayList<InternalRule>();

		if (getConfigLocation() != null) {
			InternalRules internalRules = (InternalRules) xstream.fromXML(getConfigLocation().getInputStream());
			if (!CollectionUtils.isEmpty(internalRules.getRules())) {
				rules.addAll(internalRules.getRules());
			}
		}
		if (getConfigLocations() != null && getConfigLocations().length > 0) {
			for (Resource resource : getConfigLocations()) {
				InternalRules internalRules = (InternalRules) xstream.fromXML(resource.getInputStream());
				if (!CollectionUtils.isEmpty(internalRules.getRules())) {
					rules.addAll(internalRules.getRules());
				}
			}
		}

		return rules;
	}

}
