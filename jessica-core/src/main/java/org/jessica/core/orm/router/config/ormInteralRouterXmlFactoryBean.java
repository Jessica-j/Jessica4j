package org.jessica.core.orm.router.config;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jessica.core.orm.router.ormClientInternalRouter;
import org.jessica.core.orm.router.config.vo.InternalRule;
import org.jessica.core.orm.router.config.vo.InternalRules;
import org.jessica.core.orm.router.rules.IRoutingRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisNamespaceRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisNamespaceShardingRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisSqlActionRule;
import org.jessica.core.orm.router.rules.ibatis.IBatisSqlActionShardingRule;
import org.jessica.core.orm.router.support.IBatisRoutingFact;
import org.jessica.core.orm.support.utils.CollectionUtils;
import org.jessica.core.orm.support.utils.MapUtils;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;

public class ormInteralRouterXmlFactoryBean extends AbstractormInternalRouterConfigurationFactoryBean {

	@Override
	protected void assembleRulesForRouter(ormClientInternalRouter router, Resource configLocation,
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionShardingRules, 
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionRules,
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceShardingRules, 
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceRules) throws IOException {
		XStream xstream = new XStream();
		xstream.alias("rules", InternalRules.class);
		xstream.alias("rule", InternalRule.class);
		xstream.addImplicitCollection(InternalRules.class, "rules");
		xstream.useAttributeFor(InternalRule.class, "merger");

		InternalRules internalRules = (InternalRules) xstream.fromXML(configLocation.getInputStream());
		List<InternalRule> rules = internalRules.getRules();
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
				if (StringUtils.isEmpty(shardingExpression)) {
					namespaceRules.add(new IBatisNamespaceRule(namespace, destinations));
				} else {
					IBatisNamespaceShardingRule insr = new IBatisNamespaceShardingRule(namespace, destinations, shardingExpression);
					if (MapUtils.isNotEmpty(getFunctionsMap())) {
						insr.setFunctionMap(getFunctionsMap());
					}
					namespaceShardingRules.add(insr);
				}
			}
			if (StringUtils.isNotEmpty(sqlAction)) {
				if (StringUtils.isEmpty(shardingExpression)) {
					sqlActionRules.add(new IBatisSqlActionRule(sqlAction, destinations));
				} else {
					IBatisSqlActionShardingRule issr = new IBatisSqlActionShardingRule(sqlAction, destinations, shardingExpression);
					if (MapUtils.isNotEmpty(getFunctionsMap())) {
						issr.setFunctionMap(getFunctionsMap());
					}
					sqlActionShardingRules.add(issr);
				}
			}
		}

	}

}
