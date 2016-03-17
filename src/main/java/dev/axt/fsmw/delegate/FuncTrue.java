package dev.axt.fsmw.delegate;

public class FuncTrue<T> extends FuncBoolean<T> {

	@Override
	protected boolean value(T param) {
		log().debug("Returning true for " + param);
		return true;
	}

	@Override
	public String toString() {
		return "TRUE";
	}

}
