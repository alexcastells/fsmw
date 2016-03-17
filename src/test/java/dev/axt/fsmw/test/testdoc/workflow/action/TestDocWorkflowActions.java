package dev.axt.fsmw.test.testdoc.workflow.action;

import dev.axt.fsmw.delegate.Action;
import dev.axt.fsmw.representation.Transition;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent;

/**
 *
 * @author alextremp
 */
public class TestDocWorkflowActions {

	public static final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> transactionalAction(final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>>... acciones) {
		return new TestDocActionTX(acciones);
	}

	public static final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> userNotification() {
		return new UserNotificationAction();
	}

	public static final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> adminNotification() {
		return new AdminNotificationAction();
	}

	public static final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> save() {
		return new SaveAction();
	}

	public static final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> copyOnReclamation() {
		return new CopyOnReclamationAction();
	}

	public static final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> recovery() {
		return new RecoveryAction();
	}

	public static final Action<Transition<TestDocStatus.STATUS, TestDocEvent.EVENT>> changeStatus() {
		return new ChangeStatusAction();
	}

}
