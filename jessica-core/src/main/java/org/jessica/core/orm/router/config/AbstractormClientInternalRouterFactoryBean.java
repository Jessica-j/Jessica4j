package org.jessica.core.orm.router.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jessica.core.orm.router.DefaultormClientInternalRouter;
import org.jessica.core.orm.router.IormRouter;
import org.jessica.core.orm.router.aspects.RoutingResultCacheAspect;
import org.jessica.core.orm.router.config.support.InternalRuleLoader4DefaultInternalRouter;
import org.jessica.core.orm.router.config.vo.InternalRule;
import org.jessica.core.orm.router.support.IBatisRoutingFact;
import org.jessica.core.orm.support.LRUMap;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @see DefaultormClientInternalRouter
 * @see DefaultormClientInternalRouterXmlFactoryBean
 * @see StaticormClientInternalRouterFactoryBean
 */
public abstract class AbstractormClientInternalRouterFactoryBean implements FactoryBean, InitializingBean {
	private IormRouter<IBatisRoutingFact> router;

	private Map<String, Object> functionsMap = new HashMap<String, Object>();

	private InternalRuleLoader4DefaultInternalRouter ruleLoader = new InternalRuleLoader4DefaultInternalRouter();

	private boolean enableCache;
	private int cacheSize = -1;

	public Object getObject() throws Exception {
		return router;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return IormRouter.class;
	}

	public boolean isSingleton() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {

		DefaultormClientInternalRouter routerToUse = new DefaultormClientInternalRouter();

		List<InternalRule> rules = loadRulesFromExternal();

		getRuleLoader().loadRulesAndEquipRouter(rules, routerToUse, getFunctionsMap());

		if (isEnableCache()) {
			ProxyFactory proxyFactory = new ProxyFactory(routerToUse);
			proxyFactory.setInterfaces(new Class[] { IormRouter.class });
			RoutingResultCacheAspect advice = new RoutingResultCacheAspect();
			if (cacheSize > 0) {
				advice.setInternalCache(new LRUMap(cacheSize));
			}
			proxyFactory.addAdvice(advice);
			this.router = (IormRouter<IBatisRoutingFact>) proxyFactory.getProxy();
		} else {
			this.router = routerToUse;
		}
	}

	protected abstract List<InternalRule> loadRulesFromExternal() throws Exception;

	public IormRouter<IBatisRoutingFact> getRouter() {
		return router;
	}

	public void setRouter(IormRouter<IBatisRoutingFact> router) {
		this.router = router;
	}

	public Map<String, Object> getFunctionsMap() {
		return functionsMap;
	}

	public void setFunctionsMap(Map<String, Object> functionsMap) {
		this.functionsMap = functionsMap;
	}

	public InternalRuleLoader4DefaultInternalRouter getRuleLoader() {
		return ruleLoader;
	}

	public void setRuleLoader(InternalRuleLoader4DefaultInternalRouter ruleLoader) {
		this.ruleLoader = ruleLoader;
	}

	public void setEnableCache(boolean enableCache) {
		this.enableCache = enableCache;
	}

	public boolean isEnableCache() {
		return enableCache;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public int getCacheSize() {
		return cacheSize;
	}

}
