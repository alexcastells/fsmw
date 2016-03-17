package dev.axt.fsmw.delegate;

public class FuncFalse<T> extends FuncBoolean<T> {

	@Override
	protected boolean value(T param) {
		log().debug("Returning false for " + param);
		return false;
	}

	@Override
	public String toString() {
		return "FALSE";
	}

}
