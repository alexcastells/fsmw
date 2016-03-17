package dev.axt.fsmw.representation;

import java.io.Serializable;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author alextremp
 * @param <ROLE>
 */
public class Actor<ROLE> implements Serializable {

	private final ROLE[] roles;

	public Actor(ROLE... roles) {
		this.roles = roles;
	}

	/**
	 *
	 * @param assignable
	 * @return true if this trigger is assignable to a class avoiding a
	 * ClassCastException
	 */
	public boolean is(Class<?> assignable) {
		return assignable.isAssignableFrom(getClass());
	}

	/**
	 *
	 * @param <C> required trigger Class type
	 * @param assignable
	 * @return the trigger as the required type, can launch a runtime exception
	 * if !is(assignable)
	 */
	public <C> C as(Class<C> assignable) {
		if (!is(assignable)) {
			throw new IllegalArgumentException(String.format("The actor [%s::%s] is not assignable to [%s]", toString(), getClass().getName(), assignable.getName()));
		}
		return (C) this;
	}

	@Override
	public String toString() {
		return "Actor[" + getStringRoles() + ']';
	}

	public String getStringRoles() {
		return ArrayUtils.toString(roles);
	}

	public boolean hasRole(ROLE role) {
		if (roles == null && role != null) {
			return false;
		}
		return ArrayUtils.contains(roles, role);
	}

	public boolean hasAnyRole(ROLE... roles) {
		if (roles == null) {
			return true;
		}
		for (ROLE role : roles) {
			if (hasRole(role)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasAllRoles(ROLE... roles) {
		if (roles != null) {
			for (ROLE role : roles) {
				if (!hasRole(role)) {
					return false;
				}
			}
		}
		return true;
	}

	public ROLE[] getRoles() {
		return roles;
	}

}
