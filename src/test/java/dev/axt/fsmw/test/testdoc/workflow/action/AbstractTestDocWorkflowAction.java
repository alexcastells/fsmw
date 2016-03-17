package dev.axt.fsmw.test.testdoc.workflow.action;

import dev.axt.fsmw.delegate.Action;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.service.TestDocSampleService;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;

/**
 *
 * @author alextremp
 */
abstract class AbstractTestDocWorkflowAction extends Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> {

	/**
	 * for testing purpose..
	 */
	static final class ServiceContext {

		static TestDocSampleService testDocSampleService() {
			return new TestDocSampleService();
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
