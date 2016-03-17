package dev.axt.fsmw.test.testdoc.workflow;

import dev.axt.fsmw.core.Workflow;
import dev.axt.fsmw.test.testdoc.model.TestDoc;
import dev.axt.fsmw.test.testdoc.workflow.TestDocWorkflow;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocActor.ROLE;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.EVENT;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocStatus.STATUS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static dev.axt.fsmw.test.testdoc.workflow.representation.TestDocEvent.create;

/**
 *
 * @author alextremp
 */
public class TestDocWorkflowTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws Exception {

		TestDoc testDoc = new TestDoc();

		// get a workflow instance for the testDoc
		Workflow<STATUS, EVENT, ROLE> workflow = TestDocWorkflow.WORKFLOW(testDoc);

		// admin cannot save
		// but user can save and status will go to draft
		Assert.assertFalse(workflow.canFire(create(ROLE.ADMIN, EVENT.SAVE, testDoc)));
		Assert.assertTrue(workflow.canFire(create(ROLE.USER, EVENT.SAVE, testDoc)));

		workflow.fire(create(ROLE.USER, EVENT.SAVE, testDoc));
		Assert.assertEquals(STATUS.DRAFT, workflow.getState());

		// at draft, only user can save
		Assert.assertTrue(workflow.canFire(create(ROLE.USER, EVENT.SAVE, testDoc)));
		Assert.assertFalse(workflow.canFire(create(ROLE.ADMIN, EVENT.SAVE, testDoc)));
		workflow.fire(create(ROLE.USER, EVENT.SAVE, testDoc));

		// and user can pass it to review if he forwards the testDoc
		workflow.fire(create(ROLE.USER, EVENT.FORWARD, testDoc));
		Assert.assertEquals(STATUS.REVIEW, workflow.getState());

		// at review, only admin can forward the testDoc
		Assert.assertTrue(workflow.canFire(create(ROLE.ADMIN, EVENT.FORWARD, testDoc)));
		Assert.assertFalse(workflow.canFire(create(ROLE.USER, EVENT.FORWARD, testDoc)));
		Assert.assertTrue(workflow.canFire(create(ROLE.USER, EVENT.VIEW, testDoc)));

		// and after this forward, testDoc goes to accepted
		workflow.fire(create(ROLE.ADMIN, EVENT.FORWARD, testDoc));
		Assert.assertEquals(STATUS.ACCEPTED, workflow.getState());
	}
}
