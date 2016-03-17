package dev.axt.fsmw.delegate;

import org.apache.commons.lang3.ArrayUtils;

public class FuncOr<T> extends FuncBoolean<T> {

	private final FuncBoolean<T>[] originals;

	public FuncOr(FuncBoolean<T>[] funcs) {
		this.originals = funcs;
	}

	@Override
	protected boolean value(T param) {
		if (originals == null) {
			log().warn(toString() + " does not contain evaluable functions. Returning false for " + param);
			return false;
		}
		boolean value = false;
		for (FuncBoolean<T> func : originals) {
			if (func != null) {
				value = value || func.value(param);
			}
		}
		if (!value) {
			log().debug(toString() + " all evaluated to false for " + param);
		}
		return value;
	}

	@Override
	public String toString() {
		return "OR[" + ArrayUtils.toString(originals) + "]";
	}

}
