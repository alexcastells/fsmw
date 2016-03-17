package dev.axt.fsmw.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Boolean function. Provides static methods to work with boolean functions.
 *
 * @author alextremp
 * @param <T> parameter type. Use Void if no parameter needed.
 */
public abstract class FuncBoolean<T> {

	private final Logger LOG;

	public FuncBoolean() {
		this.LOG = LoggerFactory.getLogger(getClass());
	}

	protected final Logger log() {
		return LOG;
	}

	/**
	 *
	 * @param param
	 * @return boolean value
	 */
	protected abstract boolean value(T param);

	/**
	 * Function evaluation
	 *
	 * @param param
	 * @return
	 */
	public final boolean evaluate(T param) {
		LOG.debug("Evaluating function " + toString() + " for " + param);
		long start = System.currentTimeMillis();
		boolean b = false;
		try {
			b = value(param);
		} catch (Exception e) {
			LOG.error("Function " + toString() + " cannot be evaluated due an uncaught exception for" + param, e);
		} finally {
			LOG.debug("Function " + toString() + " evaluated to " + b + " for " + param + ". Spent " + (System.currentTimeMillis() - start) + "ms");
		}
		return b;
	}

	/**
	 * TRUE function
	 *
	 * @param <T>
	 * @return a function that always returns true
	 */
	public static <T> FuncBoolean<T> TRUE() {
		return new FuncTrue<>();
	}

	/**
	 * FALSE function
	 *
	 * @param <T>
	 * @return a function that always returns false
	 */
	public static <T> FuncBoolean<T> FALSE() {
		return new FuncFalse<>();
	}

	/**
	 * Not function
	 *
	 * @param <T>
	 * @param original
	 * @return a new function that ensures value = !(original.value)
	 */
	public static <T> FuncBoolean<T> not(final FuncBoolean<T> original) {
		return new FuncNot<>(original);
	}

	/**
	 * And function
	 *
	 * @param <T>
	 * @param originals
	 * @return a new function that ensures value = originals1 && originals2 &&
	 * ... && originalsN
	 */
	public static <T> FuncBoolean<T> and(final FuncBoolean<T>... originals) {
		return new FuncAnd<>(originals);
	}

	/**
	 * Or function
	 *
	 * @param <T>
	 * @param originals
	 * @return a new function that ensures value = originals1 || originals2 ||
	 * ... || originalsN
	 */
	public static <T> FuncBoolean<T> or(final FuncBoolean<T>... originals) {
		return new FuncOr<>(originals);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
