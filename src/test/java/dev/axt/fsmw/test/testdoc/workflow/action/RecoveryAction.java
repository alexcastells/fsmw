package dev.axt.fsmw.test.testdoc.workflow.action;

import dev.axt.fsmw.exception.ActionException;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.model.TestDoc;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;
import org.apache.commons.lang3.StringUtils;

public class RecoveryAction extends AbstractTestDocWorkflowAction {

	@Override
	public void execute(Transition<TestDocStatus.STATUS, TestDocEvent.EVENT> param) throws ActionException {
		System.out.println("RecoveryAction::" + param);
		TestDocEvent testDocEvent = param.getTrigger().as(TestDocEvent.class);
		if (StringUtils.isBlank(testDocEvent.getIdRecovery())) {
			throw new ActionException("The recovery ID cannot be null");
		}
		// call a service..
		TestDoc testDoc = new TestDoc();
		testDoc.setId(testDocEvent.getIdRecovery());
		testDocEvent.setTestDoc(testDoc);
	}

}
