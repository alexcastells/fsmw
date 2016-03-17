# fsmw
Stateless Finite State Machine and Workflow

A stateless finite state machine definition, with workflow support, to develop a easy to read declaration of a Workflow (or state machine only).

```java

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

```

It also contains some action helpers for workflow definition (conditional and sequence actions) and boolean decisors (and, or, not)


A test specification is also readable

```java

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

```


