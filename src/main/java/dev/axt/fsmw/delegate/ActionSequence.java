package dev.axt.fsmw.delegate;

import dev.axt.fsmw.exception.ActionException;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author alextremp
 * @param <T>
 */
public class ActionSequence<T> extends Action<T> {

	private final Action<T>[] actions;

	public ActionSequence(Action<T>... actions) {
		this.actions = actions;
	}

	@Override
	protected void execute(T param) throws ActionException {
		if (actions != null) {
			int i = 1;
			for (Action<T> action : actions) {
				if (action != null) {
					log().info("Running action [" + (i++) + "] " + action);
					action.run(param);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "ActionSequence[" + ArrayUtils.toString(actions) + "]";
	}

}
