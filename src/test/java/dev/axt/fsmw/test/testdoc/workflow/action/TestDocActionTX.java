package dev.axt.fsmw.test.testdoc.workflow.action;

import dev.axt.fsmw.delegate.Action;
import static dev.axt.fsmw.delegate.Action.sequence;
import static dev.axt.fsmw.delegate.Action.pack;
import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.service.WorkflowTransactionSampleMethodInput;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;
import org.apache.commons.lang3.ArrayUtils;

class TestDocActionTX extends AbstractTestDocWorkflowAction {

	private final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>>[] actions;

	public TestDocActionTX(Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>>... actions) {
		this.actions = actions;
	}

	@Override
	public void execute(Transition<TestDocStatus.STATUS, TestDocEvent.EVENT> param) throws ActionException {
		System.out.println("TestDocActionTX::" + param);
		ServiceContext.testDocSampleService().workflowTransactionSampleMethod(new WorkflowTransactionSampleMethodInput(pack(sequence(actions), param)));
	}

	@Override
	public String toString() {
		return "TestDocActionTX[" + ArrayUtils.toString(actions) + "]";
	}

}
