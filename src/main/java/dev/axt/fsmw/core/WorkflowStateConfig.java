package dev.axt.fsmw.core;

import dev.axt.fsmw.representation.ActorTrigger;
import dev.axt.fsmw.representation.Trigger;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Workflow's state configuration
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 * @param <ROLE>
 */
public class WorkflowStateConfig<STATE, TRIGGER, ROLE> extends StateConfig<STATE, TRIGGER> {

	private ROLE[] allowedRoles;

	public WorkflowStateConfig(WorkflowConfig<STATE, TRIGGER, ROLE> stateMachine, STATE source) {
		super(stateMachine, source);
	}

	/**
	 * Restricts this state to a set of allowed actor roles. A runtime actor
	 * won't be able to trigger events at this state if the actor does not have
	 * any of the allowed roles.
	 *
	 * @param roles
	 * @return this state configuration
	 */
	public WorkflowStateConfig<STATE, TRIGGER, ROLE> actors(ROLE... roles) {
		log().info(machine().getName() + " - added roles restriction for " + ArrayUtils.toString(roles) + " on " + this);
		this.allowedRoles = roles;
		return this;
	}

	/**
	 *
	 * @return the workflow's configuration
	 */
	@Override
	public WorkflowConfig<STATE, TRIGGER, ROLE> machine() {
		return (WorkflowConfig<STATE, TRIGGER, ROLE>) super.machine();
	}

	/**
	 * Creates a self transition to catch actions with no state changes
	 *
	 * @param trigger the trigger that can fire the event
	 * @return the new transition config
	 */
	@Override
	public WorkflowTransitionConfig<STATE, TRIGGER, ROLE> self(TRIGGER trigger) {
		return (WorkflowTransitionConfig<STATE, TRIGGER, ROLE>) super.self(trigger);
	}

	/**
	 * Creates a new outgoing transition from this state
	 *
	 * @param trigger the trigger that can fire the event
	 * @param target the state after transition succeeds
	 * @return the new transition config
	 */
	@Override
	public WorkflowTransitionConfig<STATE, TRIGGER, ROLE> transition(TRIGGER trigger, STATE target) {
		return (WorkflowTransitionConfig<STATE, TRIGGER, ROLE>) super.transition(trigger, target);
	}

	@Override
	protected TransitionConfig instantiateTransitionConfig(TRIGGER trigger, STATE target) {
		return new WorkflowTransitionConfig<>(this, trigger, target);
	}

	@Override
	WorkflowTransitionConfig<STATE, TRIGGER, ROLE> getFirstActiveTransition(Trigger<TRIGGER> trigger) {
		if (allowedRoles == null || (trigger.is(ActorTrigger.class) && trigger.as(ActorTrigger.class).getActor().hasAnyRole(allowedRoles))) {
			return (WorkflowTransitionConfig<STATE, TRIGGER, ROLE>) super.getFirstActiveTransition(trigger);
		} else {
			log().debug(machine().getName() + " - Actor restriction " + ArrayUtils.toString(allowedRoles) + " has not been acomplished on " + this + " for " + trigger + "");
		}
		return null;
	}

	@Override
	public String toString() {
		return "Workflow" + super.toString();
	}

}
