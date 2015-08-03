package org.jessica.core.orm.router.config.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jessica.core.orm.router.DefaultormClientInternalRouter;
import org.jessica.core.orm.router.config.vo.InternalRule;
import org.jessica.core.orm.router.rules.IRoutingRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisNamespaceRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisNamespaceShardingRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisSqlActionRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisSqlActionShardingRule;
import org.jessica.core.orm.router.support.IBatisRoutingFact;
import org.jessica.core.orm.support.utils.CollectionUtils;
import org.jessica.core.orm.support.utils.MapUtils;

public class InternalRuleLoader4DefaultInternalRouter {

	public void loadRulesAndEquipRouter(List<InternalRule> rules, DefaultormClientInternalRouter router, Map<String, Object> functionsMap) {
		if (CollectionUtils.isEmpty(rules)) {
			return;
		}

		for (InternalRule rule : rules) {
			String namespace = StringUtils.trimToEmpty(rule.getNamespace());
			String sqlAction = StringUtils.trimToEmpty(rule.getSqlmap());
			String shardingExpression = StringUtils.trimToEmpty(rule.getShardingExpression());
			String destinations = StringUtils.trimToEmpty(rule.getShards());

			Validate.notEmpty(destinations, "destination shards must be given explicitly.");

			if (StringUtils.isEmpty(namespace) && StringUtils.isEmpty(sqlAction)) {
				throw new IllegalArgumentException("at least one of 'namespace' or 'sqlAction' must be given.");
			}
			if (StringUtils.isNotEmpty(namespace) && StringUtils.isNotEmpty(sqlAction)) {
				throw new IllegalArgumentException("'namespace' and 'sqlAction' are alternatives, can't guess which one to use if both of them are provided.");
			}

			if (StringUtils.isNotEmpty(namespace)) {
				List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> ruleSequence = setUpRuleSequenceContainerIfNecessary(router, namespace);

				if (StringUtils.isEmpty(shardingExpression)) {

					ruleSequence.get(3).add(new IBatisNamespaceRule(namespace, destinations));
				} else {
					IBatisNamespaceShardingRule insr = new IBatisNamespaceShardingRule(namespace, destinations, shardingExpression);
					if (MapUtils.isNotEmpty(functionsMap)) {
						insr.setFunctionMap(functionsMap);
					}
					ruleSequence.get(2).add(insr);
				}
			}
			if (StringUtils.isNotEmpty(sqlAction)) {
				List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> ruleSequence = setUpRuleSequenceContainerIfNecessary(router, StringUtils.substringBeforeLast(sqlAction, "."));

				if (StringUtils.isEmpty(shardingExpression)) {
					ruleSequence.get(1).add(new IBatisSqlActionRule(sqlAction, destinations));
				} else {
					IBatisSqlActionShardingRule issr = new IBatisSqlActionShardingRule(sqlAction, destinations, shardingExpression);
					if (MapUtils.isNotEmpty(functionsMap)) {
						issr.setFunctionMap(functionsMap);
					}
					ruleSequence.get(0).add(issr);
				}
			}
		}
	}

	private List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> setUpRuleSequenceContainerIfNecessary(DefaultormClientInternalRouter routerToUse, String namespace) {
		List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> ruleSequence = routerToUse.getRulesGroupByNamespaces().get(namespace);
		if (CollectionUtils.isEmpty(ruleSequence)) {
			ruleSequence = new ArrayList<Set<IRoutingRule<IBatisRoutingFact, List<String>>>>();
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionShardingRules = new HashSet<IRoutingRule<IBatisRoutingFact, List<String>>>();
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionRules = new HashSet<IRoutingRule<IBatisRoutingFact, List<String>>>();
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceShardingRules = new HashSet<IRoutingRule<IBatisRoutingFact, List<String>>>();
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceRules = new HashSet<IRoutingRule<IBatisRoutingFact, List<String>>>();
			ruleSequence.add(sqlActionShardingRules);
			ruleSequence.add(sqlActionRules);
			ruleSequence.add(namespaceShardingRules);
			ruleSequence.add(namespaceRules);
			routerToUse.getRulesGroupByNamespaces().put(namespace, ruleSequence);
		}
		return ruleSequence;
	}
}
