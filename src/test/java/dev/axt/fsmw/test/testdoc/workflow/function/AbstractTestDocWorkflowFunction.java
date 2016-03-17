package dev.axt.fsmw.test.testdoc.workflow.function;

import dev.axt.fsmw.delegate.FuncBoolean;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;

/**
 * FuncBoolean for those conditions that are evaluated in a trigger conatining a
 * TestDoc
 *
 * @author alextremp
 */
public abstract class AbstractTestDocWorkflowFunction extends FuncBoolean<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> {

	protected abstract boolean funcValue(Transition<TestDocStatus.STATUS, TestDocEvent.EVENT> param);

	/**
	 * Evaluates only if param trigger is a TestDocEvent, or returns false
	 *
	 * @param param
	 * @return
	 */
	@Override
	public boolean value(Transition<TestDocStatus.STATUS, TestDocEvent.EVENT> param) {
		if (!param.getTrigger().is(TestDocEvent.class)) {
			return false;
		}
		return funcValue(param);
	}

}
