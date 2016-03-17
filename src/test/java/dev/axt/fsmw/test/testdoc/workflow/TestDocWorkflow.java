package dev.axt.fsmw.test.testdoc.workflow;

import dev.axt.fsmw.test.testdoc.model.TestDoc;

import dev.axt.fsmw.core.Workflow;
import dev.axt.fsmw.core.WorkflowConfig;
import dev.axt.fsmw.exception.InvalidStateException;
import static dev.axt.fsmw.test.testdoc.workflow.action.TestDocWorkflowActions.adminNotification;
import static dev.axt.fsmw.test.testdoc.workflow.action.TestDocWorkflowActions.changeStatus;
import static dev.axt.fsmw.test.testdoc.workflow.action.TestDocWorkflowActions.copyOnReclamation;
import static dev.axt.fsmw.test.testdoc.workflow.action.TestDocWorkflowActions.recovery;
import static dev.axt.fsmw.test.testdoc.workflow.action.TestDocWorkflowActions.save;
import static dev.axt.fsmw.test.testdoc.workflow.action.TestDocWorkflowActions.transactionalAction;
import static dev.axt.fsmw.test.testdoc.workflow.action.TestDocWorkflowActions.userNotification;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocActor.ROLE;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocActor.ROLE.ADMIN;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocActor.ROLE.USER;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.EVENT;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.EVENT.CLAIM;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.EVENT.DENY;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.EVENT.FORWARD;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.EVENT.SAVE;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.EVENT.VIEW;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus.STATUS;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus.STATUS.ACCEPTED;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus.STATUS.DENIED;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus.STATUS.DRAFT;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus.STATUS.NULL;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus.STATUS.REVIEW;

/**
 * TestDoc Workflow definition
 *
 * @author alextremp
 */
public class TestDocWorkflow {

	/**
	 * Creates a worflow instance for a TestDoc
	 *
	 * @param testDoc
	 * @return
	 * @throws InvalidStateException
	 */
	public static Workflow<STATUS, EVENT, ROLE> WORKFLOW(TestDoc testDoc) throws InvalidStateException {
		return new Workflow<>(FSMW, STATUS.string2status(testDoc.getStatus()));
	}

	/**
	 * TestDoc Workflow definition
	 */
	public static final WorkflowConfig<STATUS, EVENT, ROLE> FSMW = FSMW();

	/**
	 * TestDoc Workflow specification
	 *
	 * @return
	 */
	private static WorkflowConfig<STATUS, EVENT, ROLE> FSMW() {

		WorkflowConfig<STATUS, EVENT, ROLE> fsmw = new WorkflowConfig<>("TestDocWorkflowFSM");

		// Only the USER can create a TestDoc
		fsmw.state(NULL).actors(USER)
				// when the TestDoc is saved goes to draft status
				.transition(SAVE, DRAFT).action(save());

		// At DRAFT status, only the USER can trigger actions
		fsmw.state(DRAFT).actors(USER)
				// he can VIEW the testDoc (as sample, a recovery action can be executed at view self transition)
				.self(VIEW).action(recovery()).state()
				// he can save (so, he can edit), and if done, testDoc is saved
				.self(SAVE).action(save()).state()
				// if he FORWARD the testDoc, a transaction implying save, change status and admin notification will be triggered
				.transition(FORWARD, REVIEW).action(transactionalAction(save(), changeStatus(), adminNotification())).state()
				// he can deny the testDoc without sending it to revision
				.transition(DENY, DENIED).action(transactionalAction(save(), changeStatus()));

		// At REVISION status
		fsmw.state(REVIEW)
				// all can see the doc
				.self(VIEW).action(recovery()).state()
				// but only the ADMIN can edit
				.self(SAVE).actors(ADMIN).action(save()).state()
				// and only the ADMIN can FORWARD it, marking it as ACCEPTED
				.transition(FORWARD, ACCEPTED).actors(ADMIN).action(transactionalAction(save(), changeStatus())).state()
				// and only the ADMIN can DENY it, marking it as DENIED, and notifying user
				.transition(DENY, DENIED).actors(ADMIN).action(transactionalAction(save(), changeStatus(), userNotification())).state();

		// At ACCEPTED status
		fsmw.state(ACCEPTED)
				// only can be viewd
				.self(VIEW).action(recovery());

		// At DENIED status only the user
		fsmw.state(DENIED).actors(USER)
				// can view the testDoc
				.self(VIEW).action(recovery()).state()
				// and can do a CLAIM, copying the testDoc on a new instance that will start the workflow again
				.self(CLAIM).action(copyOnReclamation());
		
		return fsmw;
	}
}
