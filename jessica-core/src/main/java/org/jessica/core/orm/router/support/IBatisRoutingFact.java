package org.jessica.core.orm.router.support;

/**
 * A Wrapper for ibatis-based routing fact.
 * 
 * @author fujohnwang
 * @since 1.0
 */
public class IBatisRoutingFact {
	// SQL identity
	private String action;
	// the argument of SQL action
	private Object argument;

	public IBatisRoutingFact() {
	}

	public IBatisRoutingFact(String sql, Object arg) {
		this.action = sql;
		this.argument = arg;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getArgument() {
		return argument;
	}

	public void setArgument(Object argument) {
		this.argument = argument;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((argument == null) ? 0 : argument.hashCode());
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
		IBatisRoutingFact other = (IBatisRoutingFact) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (argument == null) {
			if (other.argument != null)
				return false;
		} else if (!argument.equals(other.argument))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IBatisRoutingFact [action=" + action + ", argument=" + argument + "]";
	}

}
