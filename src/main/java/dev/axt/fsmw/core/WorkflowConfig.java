package dev.axt.fsmw.core;

import dev.axt.fsmw.representation.State;

/**
 * Workflow configuration, that in fact is a finite state machine that can be
 * state-restricted or transition-restricted to a determined set of actors
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 * @param <ROLE>
 */
public class WorkflowConfig<STATE, TRIGGER, ROLE> extends StateMachineConfig<STATE, TRIGGER> {

	public WorkflowConfig(String name) {
		super(name);
	}

	/**
	 *
	 * @return current workflow's configuration
	 */
	@Override
	public WorkflowConfig<STATE, TRIGGER, ROLE> machine() {
		return this;
	}

	/**
	 * creates or configures a state
	 *
	 * @param state
	 * @return the state config
	 */
	@Override
	public WorkflowStateConfig<STATE, TRIGGER, ROLE> state(STATE state) {
		return (WorkflowStateConfig<STATE, TRIGGER, ROLE>) super.state(state);
	}

	@Override
	protected WorkflowStateConfig<STATE, TRIGGER, ROLE> createStateConfig(StateMachineConfig stateMachine, STATE source) {
		return new WorkflowStateConfig<>(this, source);
	}

	@Override
	WorkflowStateConfig<STATE, TRIGGER, ROLE> getStateConfig(State<STATE> state) {
		return (WorkflowStateConfig<STATE, TRIGGER, ROLE>) super.getStateConfig(state);
	}

}
