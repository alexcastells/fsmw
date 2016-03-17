package dev.axt.fsmw.representation;

/**
 *
 * @author alextremp
 * @param <TRIGGER>
 */
public class Trigger<TRIGGER> {

	private final TRIGGER trigger;

	public Trigger(TRIGGER trigger) {
		this.trigger = trigger;
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
			throw new IllegalArgumentException(String.format("The trigger [%s::%s] is not assignable to [%s]", toString(), getClass().getName(), assignable.getName()));
		}
		return (C) this;
	}

	public TRIGGER getTrigger() {
		return trigger;
	}

	@Override
	public final int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.trigger != null ? this.trigger.hashCode() : 0);
		return hash;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		final Trigger<?> other = (Trigger<?>) obj;
		return !(this.trigger == null || !this.trigger.equals(other.trigger));
	}

	@Override
	public String toString() {
		return "Trigger[" + trigger + "]";
	}

}
