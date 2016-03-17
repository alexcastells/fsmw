package dev.axt.fsmw.delegate;

import dev.axt.fsmw.exception.ActionException;

/**
 * Conditional Action performing if else declarations
 *
 * @author alextremp
 * @param <T>
 */
public class ActionConditional<T> extends Action<T> {

	private final Action<T> actionIf;
	private final Action<T> actionElse;
	private final FuncBoolean<T> funcBoolean;

	public ActionConditional(Action<T> actionIf, FuncBoolean<T> funcBoolean) {
		this.actionIf = actionIf;
		this.actionElse = null;
		this.funcBoolean = funcBoolean;
	}

	public ActionConditional(Action<T> actionIf, FuncBoolean<T> funcBoolean, Action<T> actionElse) {
		this.actionIf = actionIf;
		this.actionElse = actionElse;
		this.funcBoolean = funcBoolean;
	}

	@Override
	protected void execute(T param) throws ActionException {
		if (funcBoolean != null && actionIf != null) {
			log().info("Running conditional action");
			if (funcBoolean.value(param)) {
				log().info("condition [" + funcBoolean + "] is true, running action [" + actionIf + "]");
				actionIf.execute(param);
			} else if (actionElse != null) {
				log().info("condition [" + funcBoolean + "] is false, running action [" + actionElse + "]");
				actionElse.execute(param);
			}
		} else {
			log().warn("condition [" + this + "] has bad configuration");
		}
	}

	@Override
	public String toString() {
		return "ActionConditional[if(" + funcBoolean + ") then " + actionIf + (actionElse != null ? (" else " + actionElse) : "") + "]";
	}

}
