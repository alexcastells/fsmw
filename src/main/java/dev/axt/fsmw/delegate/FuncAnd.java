package dev.axt.fsmw.delegate;

import org.apache.commons.lang3.ArrayUtils;

public class FuncAnd<T> extends FuncBoolean<T> {

	private final FuncBoolean<T>[] originals;

	public FuncAnd(FuncBoolean<T>[] funcs) {
		this.originals = funcs;
	}

	@Override
	protected boolean value(T param) {
		if (originals == null) {
			log().warn(toString() + " function does not contain evaluable functions. Returning false for " + param);
			return false;
		}
		boolean value = true;
		for (FuncBoolean<T> func : originals) {
			if (func == null) {
				log().warn(toString() + " contains null functions, returning false for " + param);
				return false;
			} else {
				value = value && func.value(param);
				if (!value) {
					log().debug(toString() + " " + func + " returned false for " + param);
					return value;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "AND[" + ArrayUtils.toString(originals) + "]";
	}

}
