package org.jessica.core.orm.router.rules.ibatis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jessica.core.orm.router.support.IBatisRoutingFact;
 
public class IBatisSqlActionRule extends AbstractIBatisOrientedRule {

	public IBatisSqlActionRule(String pattern, String action) {
		super(pattern, action);
	}

	public boolean isDefinedAt(IBatisRoutingFact routeFact) {
		Validate.notNull(routeFact);
		return StringUtils.equals(getTypePattern(), routeFact.getAction());
	}

	@Override
	public String toString() {
		return "IBatisSqlActionRule [getAction()=" + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
	}

}
