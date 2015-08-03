
package org.jessica.core.orm.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jessica.core.orm.router.rules.IRoutingRule;
import org.jessica.core.orm.router.support.IBatisRoutingFact;
import org.jessica.core.orm.router.support.RoutingResult;
import org.jessica.core.orm.support.LRUMap;
import org.jessica.core.orm.support.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class ormClientInternalRouter implements IormRouter<IBatisRoutingFact> {

	private transient final Logger logger = LoggerFactory.getLogger(ormClientInternalRouter.class);

	private LRUMap localCache;
	private boolean enableCache = false;

	public ormClientInternalRouter(boolean enableCache) {
		this(enableCache, 10000);
	}

	public ormClientInternalRouter(int cacheSize) {
		this(true, cacheSize);
	}

	public ormClientInternalRouter(boolean enableCache, int cacheSize) {
		this.enableCache = enableCache;
		if (this.enableCache) {
			localCache = new LRUMap(cacheSize);
		}
	}

	private List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> ruleSequences = new ArrayList<Set<IRoutingRule<IBatisRoutingFact, List<String>>>>();

	public RoutingResult doRoute(IBatisRoutingFact routingFact) throws RoutingException {
		if (enableCache) {
			synchronized (localCache) {
				if (localCache.containsKey(routingFact)) {
					RoutingResult result = (RoutingResult) localCache.get(routingFact);
					logger.info("return routing result:{} from cache for fact:{}", result, routingFact);
					return result;
				}
			}
		}

		RoutingResult result = new RoutingResult();
		result.setResourceIdentities(new ArrayList<String>());

		IRoutingRule<IBatisRoutingFact, List<String>> ruleToUse = null;
		if (!CollectionUtils.isEmpty(getRuleSequences())) {
			for (Set<IRoutingRule<IBatisRoutingFact, List<String>>> ruleSet : getRuleSequences()) {
				ruleToUse = searchMatchedRuleAgainst(ruleSet, routingFact);
				if (ruleToUse != null) {
					break;
				}
			}
		}

		if (ruleToUse != null) {
			logger.info("matched with rule:{} with fact:{}", ruleToUse, routingFact);
			result.getResourceIdentities().addAll(ruleToUse.action());
		} else {
			logger.info("No matched rule found for routing fact:{}", routingFact);
		}

		if (enableCache) {
			synchronized (localCache) {
				localCache.put(routingFact, result);
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

	public LRUMap getLocalCache() {
		return localCache;
	}

	public synchronized void clearLocalCache() {
		this.localCache.clear();
	}

	public boolean isEnableCache() {
		return enableCache;
	}

	public void setRuleSequences(List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> ruleSequences) {
		this.ruleSequences = ruleSequences;
	}

	public List<Set<IRoutingRule<IBatisRoutingFact, List<String>>>> getRuleSequences() {
		return ruleSequences;
	}

}
