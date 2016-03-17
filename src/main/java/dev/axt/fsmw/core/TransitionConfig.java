package dev.axt.fsmw.core;

import dev.axt.fsmw.delegate.Action;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.delegate.FuncBoolean;
import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.representation.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transition configuration
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 */
public class TransitionConfig<STATE, TRIGGER> {

	private final Logger LOG;

	private final StateConfig<STATE, TRIGGER> sourceStateConfig;
	private final Transition<STATE, TRIGGER> representation;
	private FuncBoolean<Transition<STATE, TRIGGER>> deactivation;
	private Action<Transition<STATE, TRIGGER>> executeWhenTriggered;

	public TransitionConfig(StateConfig<STATE, TRIGGER> sourceStateConfig, TRIGGER action, STATE targetState) {
		LOG = LoggerFactory.getLogger(getClass());
		this.sourceStateConfig = sourceStateConfig;
		this.representation = new Transition<>(sourceStateConfig.getSourceState(), action, targetState);
	}

	/**
	 *
	 * @return the state machine where source transition's state is
	 */
	public StateMachineConfig<STATE, TRIGGER> machine() {
		return state().machine();
	}

	/**
	 *
	 * @return the source transition's state
	 */
	public StateConfig<STATE, TRIGGER> state() {
		return sourceStateConfig;
	}

	/**
	 * Declares an invalidation check for this transition for revision at
	 * runtime. When the check is true, that means the transition is no
	 * available.
	 *
	 * @param condition
	 * @return current transition config
	 */
	public TransitionConfig<STATE, TRIGGER> inactiveWhen(FuncBoolean<Transition<STATE, TRIGGER>> condition) {
		LOG.info(machine().getName() + " - set deactivation using " + condition + " on " + this);
		deactivation = condition;
		return this;
	}

	/**
	 * Declares an action to execute when this transition is available and
	 * triggered so the state machine will reach the transition's target state
	 * after this action executes. The action will have the runtime's transition
	 * data when it's executed so action can know the target's state, and also
	 * the source state and trigger action event.
	 *
	 * @param action
	 * @return current transition config
	 */
	public TransitionConfig<STATE, TRIGGER> action(Action<Transition<STATE, TRIGGER>> action) {
		LOG.info(machine().getName() + " - set action using " + action + " on " + this);
		executeWhenTriggered = action;
		return this;
	}

	boolean isActiveFor(Trigger<TRIGGER> trigger) {
		if (deactivation == null || !deactivation.evaluate(getRuntimeRepresentation(trigger))) {
			return true;
		} else {
			LOG.debug(machine().getName() + " - inactive " + this + " for deactivation rule " + deactivation);
			return false;
		}
	}

	void executeActionWhenTriggered(Trigger<TRIGGER> trigger) throws ActionException {
		if (executeWhenTriggered != null) {
			LOG.info(machine().getName() + " - running action triggered by " + trigger + " - " + executeWhenTriggered + " on " + this);
			executeWhenTriggered.run(getRuntimeRepresentation(trigger));
		}
	}

	Transition<STATE, TRIGGER> getRepresentation() {
		return representation;
	}

	Transition<STATE, TRIGGER> getRuntimeRepresentation(Trigger<TRIGGER> trigger) {
		return representation.runtimeTransition(trigger);
	}

	protected Logger log() {
		return LOG;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + (this.representation != null ? this.representation.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TransitionConfig<?, ?> other = (TransitionConfig<?, ?>) obj;
		return !(this.representation != other.representation && (this.representation == null || !this.representation.equals(other.representation)));
	}

	@Override
	public String toString() {
		return "TransitionConfig[" + representation + "]";
	}

}
