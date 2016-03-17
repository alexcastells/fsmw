package dev.axt.fsmw.delegate;

/**
 *
 * @author alextremp
 * @param <T>
 */
public interface ActionPack<T> {

	void execute();

	T getParam();
}
