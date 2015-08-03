
package org.jessica.core.orm.router;

import org.jessica.core.orm.router.support.RoutingResult;

/**
 * @since 1.0
 * @see IBatisRoutingFact
 */
public interface IormRouter<T> {
	RoutingResult doRoute(T routingFact) throws RoutingException;
}
