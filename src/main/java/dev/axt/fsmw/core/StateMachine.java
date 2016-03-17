package dev.axt.fsmw.core;

import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.exception.NoAvailableTransitionException;
import dev.axt.fsmw.exception.InvalidStateException;
import dev.axt.fsmw.representation.State;
import dev.axt.fsmw.representation.Trigger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finite state machine instance
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 */
public class StateMachine<STATE, TRIGGER> implements Serializable {

	private final Logger LOG;

	private final StateMachineConfig<STATE, TRIGGER> stateMachineConfig;

	private State<STATE> state;

	/**
	 * Initialize the instance with a state.
	 *
	 * @param stateMachineConfig
	 * @param state
	 */
	public StateMachine(StateMachineConfig<STATE, TRIGGER> stateMachineConfig, STATE state) {
		LOG = LoggerFactory.getLogger(getClass());
		LOG.debug(stateMachineConfig.getName() + " - initializing at " + state);
		this.stateMachineConfig = stateMachineConfig;
		this.state = new State<>(state);
		StateConfig<STATE, TRIGGER> stateConfig = stateMachineConfig.getStateConfig(this.state);
		if (stateConfig == null) {
			LOG.warn(stateMachineConfig.getName() + " - does not contain " + state);
		}
	}

	/**
	 *
	 * @return the current machine's state
	 */
	public STATE getState() {
		if (state != null) {
			return state.getState();
		}
		return null;
	}

	/**
	 * Fires a trigger event using a runtime evaluable trigger declaration.
	 *
	 * @param trigger the trigger will be put in evaluable transitions, so there
	 * can be parameters, context data, ...
	 * @throws NoAvailableTransitionException if there is no transition
	 * available from the machine's current state for the fired trigger
	 * @throws InvalidStateException if the 'state' is not declared in the
	 * 'stateMachine'
	 * @throws ActionException if action is declared and fails
	 */
	public void fire(Trigger<TRIGGER> trigger) throws NoAvailableTransitionException, InvalidStateException, ActionException {
		LOG.info(stateMachineConfig.getName() + " - attempting to fire " + trigger + " from " + getStateRepresentation());
		StateConfig<STATE, TRIGGER> stateConfig = stateMachineConfig.getStateConfig(this.state);
		if (stateConfig == null) {
			LOG.error(stateMachineConfig.getName() + " - cannot fire " + trigger);
			throw new InvalidStateException(state);
		}
		TransitionConfig<STATE, TRIGGER> firstActiveTransition = getFirstActiveTransition(trigger);
		if (firstActiveTransition == null) {
			LOG.error(stateMachineConfig.getName() + " - cannot fire " + trigger);
			throw new NoAvailableTransitionException(getStateRepresentation(), trigger);
		}
		LOG.info(stateMachineConfig.getName() + " - firing " + firstActiveTransition);
		firstActiveTransition.executeActionWhenTriggered(trigger);
		state = new State<>(firstActiveTransition.getRepresentation().getTargetState().getState());
		LOG.info(stateMachineConfig.getName() + " - changed internal state to " + getStateRepresentation());
	}

	/**
	 * Determines if a trigger can be fired in the stateMachine for the current
	 * instance's state, using a runtime evaluable trigger declaration.
	 *
	 * @param trigger the trigger will be put in evaluable transitions, so there
	 * can be parameters, context data, ...
	 * @return
	 */
	public boolean canFire(Trigger<TRIGGER> trigger) {
		return getFirstActiveTransition(trigger) != null;
	}

	/**
	 *
	 * @param state
	 * @return true if 'state' is the current instance's state
	 */
	public boolean isState(STATE state) {
		return getState() != null && getState().equals(state);
	}

	protected StateMachineConfig<STATE, TRIGGER> getConfig() {
		return stateMachineConfig;
	}

	State<STATE> getStateRepresentation() {
		return state;
	}

	private TransitionConfig<STATE, TRIGGER> getFirstActiveTransition(Trigger<TRIGGER> trigger) {
		StateConfig<STATE, TRIGGER> stateConfig = getConfig().getStateConfig(getStateRepresentation());
		if (stateConfig != null) {
			return stateConfig.getFirstActiveTransition(trigger);
		} else {
			LOG.debug(stateMachineConfig.getName() + " - there's no active transition for " + trigger);
		}
		return null;
	}

	public List<STATE> getConfiguredStates() {
		List<STATE> states = new ArrayList<>();
		states.addAll(stateMachineConfig.getConfiguredStates());
		return states;
	}
	
	public List<TRIGGER> getAvailableTriggers() {
		List<TRIGGER> triggers = new ArrayList<>();
		StateConfig<STATE, TRIGGER> stateConfig = getConfig().getStateConfig(getStateRepresentation());
		if (stateConfig != null) {
			triggers.addAll(stateConfig.getConfiguredTriggers());
		}
		return triggers;
	}

}
