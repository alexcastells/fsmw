package dev.axt.fsmw.delegate;

import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.representation.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Action. Provides static methods to work with action elements.
 *
 * @author alextremp
 * @param <T> parameter type. Use Void if there are no required parameters.
 */
public abstract class Action<T> {

	private final Logger LOG;

	protected abstract void execute(T param) throws ActionException;

	public Action() {
		LOG = LoggerFactory.getLogger(getClass());
	}

	protected final Logger log() {
		return LOG;
	}

	public final void run(T param) throws ActionException {
		LOG.info("Running action " + toString() + " for " + param);
		long start = System.currentTimeMillis();
		try {
			execute(param);
		} catch (ActionException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ActionException("Action " + toString() + " for " + param + " has thrown an uncaught exception", e);
		} finally {
			LOG.info("Exiting Action " + toString() + " for " + param + ". Spent " + (System.currentTimeMillis() - start) + "ms");
		}
	}

	/**
	 * Creates a sequence of actions
	 *
	 * @param <T>
	 * @param actions
	 * @return a new Action that ensures a execution sequence
	 */
	public static <T> Action<T> sequence(final Action<T>... actions) {
		return new ActionSequence<>(actions);
	}

	/**
	 * Creates a conditional Action
	 *
	 * @param <T>
	 * @param action
	 * @param funcBoolean
	 * @return a new Action that ensures 'action' execution only if
	 * 'funcBoolean' is true
	 */
	public static <T> Action<T> conditional(final Action<T> action, final FuncBoolean<T> funcBoolean) {
		return new ActionConditional<>(action, funcBoolean, null);
	}

	/**
	 * Creates a conditional Action
	 *
	 * @param <T>
	 * @param actionIf
	 * @param funcBoolean
	 * @param actionElse
	 * @return a new Action that ensures 'actionIf' execution only if
	 * 'funcBoolean' is true and 'actionElse' if 'funcBoolean' is false
	 */
	public static <T> Action<T> conditional(final Action<T> actionIf, final FuncBoolean<T> funcBoolean, final Action<T> actionElse) {
		return new ActionConditional<>(actionIf, funcBoolean, actionElse);
	}

	/**
	 * Utility Input for transactional actions over single transition. It should
	 * be used as:<br>
	 * 1) create the Action that must call the service<br>
	 * 2) call the transacted service passing this ActionTxInput as input
	 * parameter<br>
	 * 3) call ActionTxInput#execute inside the transacted service block, so the
	 * underlying Action will be exeucted in the transaction context
	 *
	 * @param <STATE>
	 * @param <TRIGGER>
	 * @param underlyingAction the underlyingAction action to run
	 * @param transition the runtime transition
	 * @return
	 */
	public static <STATE, TRIGGER> ActionPack<Transition<STATE, TRIGGER>> pack(final Action<Transition<STATE, TRIGGER>> underlyingAction, final Transition<STATE, TRIGGER> transition) {
		return new ActionPackImpl<>(underlyingAction, transition);
	}
}
