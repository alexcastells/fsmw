package dev.axt.fsmw.core;

/**
 * Workflow instance based on a finite state machine
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 * @param <ROLE>
 */
public class Workflow<STATE, TRIGGER, ROLE> extends StateMachine<STATE, TRIGGER> {

	public Workflow(WorkflowConfig<STATE, TRIGGER, ROLE> workflowConfig, STATE state) {
		super(workflowConfig, state);
	}

}
