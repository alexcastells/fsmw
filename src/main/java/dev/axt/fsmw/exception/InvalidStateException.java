package dev.axt.fsmw.exception;

import dev.axt.fsmw.representation.State;

/**
 *
 * @author alextremp
 */
public class InvalidStateException extends Exception {

	private final State sourceState;

	public InvalidStateException(State sourceState) {
		super("Workflow does not contain: " + sourceState);
		this.sourceState = sourceState;
	}

	public State getSourceState() {
		return sourceState;
	}
}
