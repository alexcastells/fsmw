package dev.axt.fsmw.exception;

import dev.axt.fsmw.representation.State;
import dev.axt.fsmw.representation.Trigger;

/**
 *
 * @author alextremp
 */
public class NoAvailableTransitionException extends Exception {

	private final State sourceState;
	private final Trigger sourceAction;

	public NoAvailableTransitionException(State sourceState, Trigger sourceAction) {
		super("Can't get an available transition at " + sourceState + " on action " + sourceAction);
		this.sourceState = sourceState;
		this.sourceAction = sourceAction;
	}

	public State getSourceState() {
		return sourceState;
	}

	public Trigger getSourceAction() {
		return sourceAction;
	}

}
