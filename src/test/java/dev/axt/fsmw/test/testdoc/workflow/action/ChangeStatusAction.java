package dev.axt.fsmw.test.testdoc.workflow.action;

import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;

public class ChangeStatusAction extends AbstractTestDocWorkflowAction {

	@Override
	public void execute(Transition<TestDocStatus.STATUS, TestDocEvent.EVENT> param) throws ActionException {
		System.out.println("changeStatus::" + param);
		param.getTrigger().as(TestDocEvent.class).getTestDoc().setStatus(TestDocStatus.STATUS.status2string(param.getTargetState().getState()));
	}

}
