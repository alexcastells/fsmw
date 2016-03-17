package dev.axt.fsmw.delegate;

import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.representation.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alextremp
 * @param <STATE>
 * @param <TRIGGER>
 */
class ActionPackImpl<STATE, TRIGGER> implements ActionPack<Transition<STATE, TRIGGER>> {

	private static final Logger LOG = LoggerFactory.getLogger(ActionPackImpl.class);

	private final Action<Transition<STATE, TRIGGER>> underlyingAction;

	private final Transition<STATE, TRIGGER> transition;

	ActionPackImpl(Action<Transition<STATE, TRIGGER>> underlyingAction, Transition<STATE, TRIGGER> transition) {
		if (underlyingAction == null) {
			throw new IllegalArgumentException("Underlying Action cannot be null");
		}
		this.underlyingAction = underlyingAction;
		this.transition = transition;
	}

	@Override
	public void execute() {
		try {
			LOG.info(getClass().getSimpleName() + " - Running underlying action [" + underlyingAction + "]");
			underlyingAction.run(getParam());
		} catch (ActionException ae) {
			throw new RuntimeException("Underlying action execution failed transitioning " + transition, ae);
		}
	}

	@Override
	public Transition<STATE, TRIGGER> getParam() {
		return transition;
	}

	@Override
	public String toString() {
		return "ActionPack[" + underlyingAction + " with " + transition + ']';
	}

}
