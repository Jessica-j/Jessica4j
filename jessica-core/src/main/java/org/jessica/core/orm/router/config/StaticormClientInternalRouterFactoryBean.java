package org.jessica.core.orm.router.config;

import java.util.List;

import org.jessica.core.orm.router.config.vo.InternalRule;

public class StaticormClientInternalRouterFactoryBean extends AbstractormClientInternalRouterFactoryBean {

	private List<InternalRule> rules;

	public void setRules(List<InternalRule> rules) {
		this.rules = rules;
	}

	public List<InternalRule> getRules() {
		return rules;
	}

	@Override
	protected List<InternalRule> loadRulesFromExternal() {
		return getRules();
	}

}
