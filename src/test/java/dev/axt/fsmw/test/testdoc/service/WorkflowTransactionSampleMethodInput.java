package dev.axt.fsmw.test.testdoc.service;

import dev.axt.fsmw.delegate.ActionPack;

/**
 *
 * @author alextremp
 */
public class WorkflowTransactionSampleMethodInput {

	private ActionPack actionPack;

	public WorkflowTransactionSampleMethodInput() {
	}

	public WorkflowTransactionSampleMethodInput(ActionPack actionPack) {
		this.actionPack = actionPack;
	}

	public ActionPack getActionPack() {
		return actionPack;
	}

	public void setActionPack(ActionPack actionPack) {
		this.actionPack = actionPack;
	}

	@Override
	public String toString() {
		return "WorkflowTransactionSampleMethodInput[" + actionPack + ']';
	}

}
