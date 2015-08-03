package org.jessica.core.orm.router.rules.ibatis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jessica.core.orm.router.support.IBatisRoutingFact;

public class IBatisNamespaceRule extends AbstractIBatisOrientedRule {

	public IBatisNamespaceRule(String pattern, String action) {
		super(pattern, action);
	}

	public boolean isDefinedAt(IBatisRoutingFact routingFact) {
		Validate.notNull(routingFact);
		String namespace = StringUtils.substringBeforeLast(routingFact.getAction(), ".");
		return StringUtils.equals(namespace, getTypePattern());
	}

	@Override
	public String toString() {
		return "IBatisNamespaceRule [getAction()=" + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
	}

}
