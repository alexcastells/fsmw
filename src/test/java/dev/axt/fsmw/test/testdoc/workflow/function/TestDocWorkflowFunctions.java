package dev.axt.fsmw.test.testdoc.workflow.function;

import dev.axt.fsmw.delegate.FuncBoolean;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;

/**
 *
 * @author alextremp
 */
public class TestDocWorkflowFunctions {

	public static final FuncBoolean<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> COND_DATES_INVALID() {
		return new FunctionDatesInvalid();
	}

}
