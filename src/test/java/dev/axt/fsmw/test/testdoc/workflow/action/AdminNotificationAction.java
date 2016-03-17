package dev.axt.fsmw.test.testdoc.workflow.action;

import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;


class AdminNotificationAction extends AbstractTestDocWorkflowAction {

	@Override
	public void execute(Transition<TestDocStatus.STATUS, TestDocEvent.EVENT> param) throws ActionException {
		System.out.println("AdminNotificationAction::" + param);
	}

}
