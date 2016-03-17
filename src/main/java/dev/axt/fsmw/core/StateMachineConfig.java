package dev.axt.fsmw.core;

import dev.axt.fsmw.representation.State;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finite State Machine declaration
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 */
public class StateMachineConfig<STATE, TRIGGER> implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(StateMachineConfig.class);

	private final String name;

	private final Map<STATE, StateConfig> machineConfig = createMachineConfigMap();

	public StateMachineConfig(String name) {
		this.name = name == null ? getClass().getName() : name;
		LOG.info(name + " - init");
	}

	protected Map<STATE, StateConfig> createMachineConfigMap() {
		return new HashMap<>();
	}

	protected StateConfig createStateConfig(StateMachineConfig stateMachine, STATE source) {
		return new StateConfig(stateMachine, source);
	}

	/**
	 *
	 * @return state machine instance
	 */
	public StateMachineConfig<STATE, TRIGGER> machine() {
		return this;
	}

	/**
	 * Creates a new State configuration to make it available for transitions
	 *
	 * @param state
	 * @return the new state configuration
	 */
	public StateConfig<STATE, TRIGGER> state(STATE state) {
		return getOrCreateStateConfig(state);
	}

	String getName() {
		return name;
	}

	StateConfig<STATE, TRIGGER> getStateConfig(State<STATE> state) {
		return machineConfig.get(state.getState());
	}

	StateConfig<STATE, TRIGGER> getOrCreateStateConfig(STATE state) {
		StateConfig<STATE, TRIGGER> stateConfig = machineConfig.get(state);
		if (stateConfig == null) {
			stateConfig = createStateConfig(this, state);
			machineConfig.put(state, stateConfig);
			LOG.info(getName() + " - added " + stateConfig);
		}
		return stateConfig;
	}

	Collection<STATE> getConfiguredStates() {
		return machineConfig.keySet();
	}

}
