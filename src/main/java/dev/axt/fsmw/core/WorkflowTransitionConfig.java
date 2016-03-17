package dev.axt.fsmw.core;

import dev.axt.fsmw.delegate.Action;
import dev.axt.fsmw.delegate.FuncBoolean;
import dev.axt.fsmw.representation.ActorTrigger;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.representation.Trigger;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Workflow's transition configuration
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 * @param <ROLE>
 */
public class WorkflowTransitionConfig<STATE, TRIGGER, ROLE> extends TransitionConfig<STATE, TRIGGER> {

	private ROLE[] allowedRoles;

	public WorkflowTransitionConfig(WorkflowStateConfig<STATE, TRIGGER, ROLE> sourceStateConfig, TRIGGER action, STATE targetState) {
		super(sourceStateConfig, action, targetState);
	}

	/**
	 * Restricts this transition to a set of allowed actor roles. A runtime
	 * actor won't be able to trigger events and reach this transition if the
	 * actor does not have any of the allowed roles.
	 *
	 * @param roles
	 * @return this transition configuration
	 */
	public WorkflowTransitionConfig<STATE, TRIGGER, ROLE> actors(ROLE... roles) {
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
	 * Declares an action to execute when this transition is available and
	 * triggered so the state machine will reach the transition's target state
	 * after this action executes. The action will have the runtime's transition
	 * data when it's executed so action can know the target's state, and also
	 * the source state and trigger action event.
	 *
	 * @param action
	 * @return current transition config
	 */
	@Override
	public WorkflowTransitionConfig<STATE, TRIGGER, ROLE> action(Action<Transition<STATE, TRIGGER>> action) {
		return (WorkflowTransitionConfig<STATE, TRIGGER, ROLE>) super.action(action);
	}

	/**
	 *
	 * @return the source transition's state
	 */
	@Override
	public WorkflowStateConfig<STATE, TRIGGER, ROLE> state() {
		return (WorkflowStateConfig<STATE, TRIGGER, ROLE>) super.state();
	}

	/**
	 * Declares an invalidation check for this transition for revision at
	 * runtime. When the check is true, that means the transition is no
	 * available.
	 *
	 * @param condition
	 * @return current transition config
	 */
	@Override
	public WorkflowTransitionConfig<STATE, TRIGGER, ROLE> inactiveWhen(FuncBoolean<Transition<STATE, TRIGGER>> condition) {
		return (WorkflowTransitionConfig<STATE, TRIGGER, ROLE>) super.inactiveWhen(condition);
	}

	@Override
	boolean isActiveFor(Trigger<TRIGGER> trigger) {
		if (allowedRoles == null || (trigger.is(ActorTrigger.class) && trigger.as(ActorTrigger.class).getActor().hasAnyRole(allowedRoles))) {
			return super.isActiveFor(trigger);
		} else {
			if (!trigger.is(ActorTrigger.class)) {
				log().debug(machine().getName() + " - the event " + trigger + " is not an ActorTrigger object so it can't be evaluated on " + this);
			} else {
				log().debug(machine().getName() + " - Actor restriction " + ArrayUtils.toString(allowedRoles) + " has not been acomplished on " + this + " for " + trigger + "");
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Workflow" + super.toString();
	}

}
