package dev.axt.fsmw.test.testdoc.workflow.function;

import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;
import dev.axt.fsmw.test.testdoc.model.TestDoc;

public class FunctionDatesInvalid extends AbstractTestDocWorkflowFunction {

	@Override
	protected boolean funcValue(Transition<TestDocStatus.STATUS, TestDocEvent.EVENT> param) {
		TestDoc testDoc = param.getTrigger().as(TestDocEvent.class).getTestDoc();
		return testDoc.getDateStart() == null
				|| testDoc.getDateEnd() == null
				|| testDoc.getDateStart().after(testDoc.getDateCreate())
				|| testDoc.getDateEnd().before(testDoc.getDateStart());
	}

}
