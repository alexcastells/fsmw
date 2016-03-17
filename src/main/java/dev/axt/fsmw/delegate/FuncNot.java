package dev.axt.fsmw.delegate;

public class FuncNot<T> extends FuncBoolean<T> {

	private final FuncBoolean<T> original;

	public FuncNot(FuncBoolean<T> original) {
		this.original = original;
	}

	@Override
	protected boolean value(T param) {
		boolean b = !original.value(param);
		if (!b) {
			log().debug(toString() + " evaluated to false for " + param);
		}
		return b;
	}

	@Override
	public String toString() {
		return "NOT[" + original + "]";
	}

}
