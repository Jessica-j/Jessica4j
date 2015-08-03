package org.jessica.core.orm.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jessica.core.orm.router.rules.IRoutingRule;
import org.jessica.core.orm.router.support.IBatisRoutingFact;
import org.jessica.core.orm.router.support.RoutingResult;
import org.jessica.core.orm.support.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DefaultormClientInternalRouter} receive a map which will maintain a
 * group of rules as per SQ-map namespaces.<br>
 * it will evaluate the rules as per namesapce and a sequence from specific
 * rules to more generic rules.<br>
 * usually, the users don't need to care about these internal details, to use
 * {@link DefaultormClientInternalRouter}, just turn to
 * {@link DefaultormClientInternalRouterXmlFactoryBean} for instantiation.<br>
 * 
 * @since 1.0
 * @see DefaultormClientInternalRouterXmlFactoryBean
 */
public class DefaultormClientInternalRouter implements IormRouter<IBatisRoutingFact> {

	private transient final Logger logger = LoggerFactory.getLogger(DefaultormClientInternalRouter.class);

	private Map<String, List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>>> rulesGroupByNamespaces = new HashMap<String, List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>>>();

	public RoutingResult doRoute(IBatisRoutingFact routingFact) throws RoutingException {
		Validate.notNull(routingFact);
		String action = routingFact.getAction();
		Validate.notEmpty(action);
		String namespace = StringUtils.substringBeforeLast(action, ".");
		List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> rules = getRulesGroupByNamespaces().get(namespace);

		RoutingResult result = new RoutingResult();
		result.setResourceIdentities(new ArrayList<String>());

		if (!CollectionUtils.isEmpty(rules)) {
			IRoutingRule<IBatisRoutingFact, List<String>> ruleToUse = null;
			for (Set<IRoutingRule<IBatisRoutingFact, List<String>>> ruleSet : rules) {
				ruleToUse = searchMatchedRuleAgainst(ruleSet, routingFact);
				if (ruleToUse != null) {
					break;
				}
			}

			if (ruleToUse != null) {
				logger.info("matched with rule:{} with fact:{}", ruleToUse, routingFact);
				result.getResourceIdentities().addAll(ruleToUse.action());
			} else {
				logger.info("No matched rule found for routing fact:{}", routingFact);
			}
		}

		return result;
	}

	private IRoutingRule<IBatisRoutingFact, List<String>> searchMatchedRuleAgainst(Set<IRoutingRule<IBatisRoutingFact, List<String>>> rules, IBatisRoutingFact routingFact) {
		if (CollectionUtils.isEmpty(rules)) {
			return null;
		}
		for (IRoutingRule<IBatisRoutingFact, List<String>> rule : rules) {
			if (rule.isDefinedAt(routingFact)) {
				return rule;
			}
		}
		return null;
	}

	public void setRulesGroupByNamespaces(Map<String, List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>>> rulesGroupByNamespaces) {
		this.rulesGroupByNamespaces = rulesGroupByNamespaces;
	}

	public Map<String, List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>>> getRulesGroupByNamespaces() {
		return rulesGroupByNamespaces;
	}

}
