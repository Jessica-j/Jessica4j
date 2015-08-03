package org.jessica.core.orm.router.rules;

/**
 * a rule acts in a "when-then" behavior, in our case, when the fact
 * {@link #isDefinedAt(Object)} or matches, then we will return action result.
 * the {@link IormRouter} will decide how to use these action result.
 * 
 * @author fujohnwang
 * @since 1.0
 */
public interface IRoutingRule<F, T> {
	/**
	 * @param <F>,
	 *            the type of the routing fact
	 * @param routeFact,
	 *            the fact to check against
	 * @return
	 */
	boolean isDefinedAt(F routingFact);

	/**
	 * if a update or delete will involve multiple data sources, we have to
	 * return a group of data sources to use.<br>
	 * for rules the matches only one data source, return a set with size==1.
	 * <br>
	 * 
	 * @return
	 */
	T action();
}
