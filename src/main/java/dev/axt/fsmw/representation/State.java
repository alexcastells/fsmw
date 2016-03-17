package dev.axt.fsmw.representation;

/**
 *
 * @author alextremp
 * @param <STATE>
 */
public class State<STATE> {

	private final STATE state;

	public State(STATE state) {
		if (state == null) {
			throw new IllegalArgumentException("State cannot be null");
		}
		this.state = state;
	}

	public STATE getState() {
		return state;
	}

	@Override
	public final int hashCode() {
		int hash = 7;
		hash = 37 * hash + (this.state != null ? this.state.hashCode() : 0);
		return hash;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final State<?> other = (State<?>) obj;
		return !(this.state != other.state && (this.state == null || !this.state.equals(other.state)));
	}

	@Override
	public String toString() {
		return "State[" + state + "]";
	}

}
