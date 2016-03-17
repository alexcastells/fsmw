package dev.axt.fsmw.representation;

/**
 * Transition definition
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 */
public class Transition<STATE, TRIGGER> {

	private final State<STATE> sourceState;
	private final State<STATE> targetState;
	private final Trigger<TRIGGER> trigger;

	/**
	 * Creates a transition from a source state to a target state, raised when
	 * an action is triggered
	 *
	 * @param sourceState
	 * @param action
	 * @param targetState
	 */
	public Transition(STATE sourceState, TRIGGER action, STATE targetState) {
		this(new State<>(sourceState), new Trigger<>(action), new State<>(targetState));
	}

	Transition(State<STATE> sourceState, Trigger<TRIGGER> action, State<STATE> targetState) {
		this.sourceState = sourceState;
		this.targetState = targetState;
		this.trigger = action;
	}

	/**
	 *
	 * @return the source (from) state
	 */
	public State<STATE> getSourceState() {
		return sourceState;
	}

	/**
	 *
	 * @return the target (to) state
	 */
	public State<STATE> getTargetState() {
		return targetState;
	}

	/**
	 *
	 * @return the action container that raises de state change
	 */
	public Trigger<TRIGGER> getTrigger() {
		return trigger;
	}

	/**
	 *
	 * @param runtimeTrigger
	 * @return
	 */
	public Transition<STATE, TRIGGER> runtimeTransition(Trigger<TRIGGER> runtimeTrigger) {
		return new Transition<>(sourceState, runtimeTrigger, targetState);
	}

	@Override
	public final int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.sourceState != null ? this.sourceState.hashCode() : 0);
		hash = 97 * hash + (this.targetState != null ? this.targetState.hashCode() : 0);
		hash = 97 * hash + (this.trigger != null ? this.trigger.hashCode() : 0);
		return hash;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Transition<?, ?> other = (Transition<?, ?>) obj;
		if (this.sourceState != other.sourceState && (this.sourceState == null || !this.sourceState.equals(other.sourceState))) {
			return false;
		}
		if (this.targetState != other.targetState && (this.targetState == null || !this.targetState.equals(other.targetState))) {
			return false;
		}
		return !(this.trigger == null || !this.trigger.equals(other.trigger));
	}

	@Override
	public String toString() {
		return "Transition[" + trigger + "===" + sourceState + (!sourceState.equals(targetState) ? ">" + targetState : "") + "]";
	}

}
