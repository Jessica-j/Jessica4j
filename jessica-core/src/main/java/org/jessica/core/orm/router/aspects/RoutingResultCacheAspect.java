package org.jessica.core.orm.router.aspects;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jessica.core.orm.support.LRUMap;

/**
 * @see {@link IormRouter}
 * @see {@link AbstractormClientInternalRouterFactoryBean}
 * @see {@link DefaultormClientInternalRouter}
 * @see {@link StaticormClientInternalRouterFactoryBean}
 */
public class RoutingResultCacheAspect implements MethodInterceptor {

	private LRUMap internalCache = new LRUMap(1000);

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		if (args.length != 1) {
			throw new IllegalArgumentException("unexpected argument status on method:" + invocation.getMethod() + ", args:" + Arrays.toString(args));
		}

		synchronized (internalCache) {
			if (internalCache.containsKey(args[0])) {
				return internalCache.get(args[0]);
			}
		}

		Object result = null;
		try {
			result = invocation.proceed();
		} finally {
			synchronized (internalCache) {
				internalCache.put(args[0], result);
			}
		}

		return result;
	}

	public void setInternalCache(LRUMap internalCache) {
		if (internalCache == null) {
			throw new IllegalArgumentException("Null Cache Map is not allowed.");
		}
		this.internalCache = internalCache;
	}

	public LRUMap getInternalCache() {
		return internalCache;
	}

}
