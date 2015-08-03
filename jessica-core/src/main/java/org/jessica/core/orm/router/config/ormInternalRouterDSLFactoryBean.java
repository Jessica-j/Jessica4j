package org.jessica.core.orm.router.config;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.jessica.core.orm.router.ormClientInternalRouter;
import org.jessica.core.orm.router.rules.IRoutingRule;
import org.jessica.core.orm.router.support.IBatisRoutingFact;
import org.springframework.core.io.Resource;

 
public class ormInternalRouterDSLFactoryBean extends AbstractormInternalRouterConfigurationFactoryBean {

	@Override
	protected void assembleRulesForRouter(ormClientInternalRouter router, Resource configLocation, Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionShardingRules, Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionRules,
			Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceShardingRules, Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceRules) throws IOException {
		// TODO Auto-generated method stub

	}

}
