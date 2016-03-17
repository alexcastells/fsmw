package dev.axt.fsmw.test.testdoc.service;

import dev.axt.fsmw.exception.ActionException;

/**
 *
 * @author alextremp
 */
public class TestDocSampleService {

	/**
	 * Sample transactional service. In a real context it would be a
	 *
	 * @Transactional or equivalent method. If so, all actions packed in
	 * ActionPack at WorkflowTransactionSampleMethodInput will run inside the
	 * transaction context.
	 *
	 * @param input
	 * @return
	 * @throws ActionException
	 */
	public WorkflowTransactionSampleMethodOutput workflowTransactionSampleMethod(WorkflowTransactionSampleMethodInput input) throws ActionException {
		System.out.println("workflowTransactionSampleMethod::" + input);
		WorkflowTransactionSampleMethodOutput out = new WorkflowTransactionSampleMethodOutput();
		input.getActionPack().execute();
		return out;
	}
}
