package org.jessica.core.orm.router.rules;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * vertical partitioning oriented rule that matches against entity/table type.
 * <br>
 * 
 * @author fujohnwang
 * @param <F>,
 *            the fact type
 * @param <T>,
 *            the action result type
 * @see AbstractIBatisOrientedRule
 */
public abstract class AbstractEntityTypeRule<F, T> implements IRoutingRule<F, T> {

	private String typePatten; // when
	private String action; // then, it's a more generic expression, although for
							// us, a String[] is preferable.

	public AbstractEntityTypeRule(String pattern, String action) {
		Validate.notEmpty(StringUtils.trim(pattern));
		Validate.notEmpty(StringUtils.trim(action));

		this.typePatten = pattern;
		this.action = action;
	}

	public void setTypePattern(String leftExpression) {
		this.typePatten = leftExpression;
	}

	public String getTypePattern() {
		return typePatten;
	}

	public void setAction(String rightExpression) {
		this.action = rightExpression;
	}

	public String getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((typePatten == null) ? 0 : typePatten.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntityTypeRule other = (AbstractEntityTypeRule) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (typePatten == null) {
			if (other.typePatten != null)
				return false;
		} else if (!typePatten.equals(other.typePatten))
			return false;
		return true;
	}

}
