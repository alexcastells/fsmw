package dev.axt.fsmw.core;

import dev.axt.fsmw.representation.Trigger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * State configuration
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 */
public class StateConfig<STATE, TRIGGER> implements Serializable {

	private final Logger LOG;

	private final StateMachineConfig<STATE, TRIGGER> stateMachine;
	private final Map<Trigger<TRIGGER>, List<TransitionConfig>> outcomes = new LinkedHashMap<>();
	private final STATE source;

	/**
	 *
	 * @return state config's state machine
	 */
	public StateMachineConfig<STATE, TRIGGER> machine() {
		return stateMachine;
	}

	/**
	 * Creates a new outgoing transition from this state
	 *
	 * @param trigger the trigger that can fire the event
	 * @param target the state after transition succeeds
	 * @return the new transition config
	 */
	public TransitionConfig<STATE, TRIGGER> transition(TRIGGER trigger, STATE target) {
		stateMachine.getOrCreateStateConfig(target);
		TransitionConfig<STATE, TRIGGER> transitionConfig = instantiateTransitionConfig(trigger, target);
		getOrCreateTriggerConfig(new Trigger<>(trigger)).add(transitionConfig);
		LOG.info(machine().getName() + " - added " + transitionConfig + " on " + this);
		return transitionConfig;
	}

	/**
	 * Creates a self transition to catch actions with no state changes
	 *
	 * @param trigger the trigger that can fire the event
	 * @return the new transition config
	 */
	public TransitionConfig<STATE, TRIGGER> self(TRIGGER trigger) {
		return transition(trigger, source);
	}

	protected TransitionConfig instantiateTransitionConfig(TRIGGER trigger, STATE target) {
		return new TransitionConfig<>(this, trigger, target);
	}

	StateConfig(StateMachineConfig<STATE, TRIGGER> stateMachine, STATE source) {
		LOG = LoggerFactory.getLogger(getClass());
		this.source = source;
		this.stateMachine = stateMachine;
	}

	List<TransitionConfig> getTransitions(Trigger<TRIGGER> trigger) {
		return outcomes.get(trigger);
	}

	TransitionConfig getFirstActiveTransition(Trigger<TRIGGER> trigger) {
		List<TransitionConfig> available = getTransitions(trigger);
		if (available != null) {
			for (TransitionConfig<STATE, TRIGGER> transitionConfig : available) {
				if (transitionConfig.isActiveFor(trigger)) {
					return transitionConfig;
				}
			}
		} else {
			LOG.debug(machine().getName() + " - there's no transitions defined on " + this + " for " + trigger);
		}
		return null;
	}

	STATE getSourceState() {
		return source;
	}

	Collection<TRIGGER> getConfiguredTriggers() {
		Collection<TRIGGER> triggers = new ArrayList<>();
		for (Trigger<TRIGGER> trigger : outcomes.keySet()) {
			triggers.add(trigger.getTrigger());
		}
		return triggers;
	}

	private List<TransitionConfig> getOrCreateTriggerConfig(Trigger<TRIGGER> trigger) {
		List<TransitionConfig> list = outcomes.get(trigger);
		if (list == null) {
			list = new ArrayList<>();
			outcomes.put(trigger, list);
			LOG.info(machine().getName() + " - added " + trigger + " on " + this);
		}
		return list;
	}

	protected Logger log() {
		return LOG;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + (this.source != null ? this.source.hashCode() : 0);
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
		final StateConfig<?, ?> other = (StateConfig<?, ?>) obj;
		return !(this.source != other.source && (this.source == null || !this.source.equals(other.source)));
	}

	@Override
	public String toString() {
		return "StateConfig[" + source + "]";
	}

}
