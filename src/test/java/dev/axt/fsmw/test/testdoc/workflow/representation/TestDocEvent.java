package dev.axt.fsmw.test.testdoc.workflow.representation;

import dev.axt.fsmw.representation.ActorTrigger;
import dev.axt.fsmw.test.testdoc.model.TestDoc;

/**
 * TestDoc Event
 *
 * @author alextremp
 */
public class TestDocEvent extends ActorTrigger<TestDocEvent.EVENT, TestDocActor.ROLE> {

	public static enum EVENT {

		VIEW,
		SAVE,
		FORWARD,
		DENY,
		CLAIM;
	}

	private String idRecovery;

	private TestDoc testDoc;

	private TestDocEvent(TestDocActor.ROLE rol, EVENT trigger) {
		super(new TestDocActor(rol), trigger);
	}

	public static TestDocEvent create(TestDocActor.ROLE rol, EVENT trigger, TestDoc testDoc) {
		TestDocEvent e = new TestDocEvent(rol, trigger);
		e.testDoc = testDoc;
		return e;
	}

	public TestDoc getTestDoc() {
		return testDoc;
	}

	public void setTestDoc(TestDoc testDoc) {
		this.testDoc = testDoc;
	}

	public String getIdRecovery() {
		return idRecovery;
	}

	public void setIdRecovery(String idRecovery) {
		this.idRecovery = idRecovery;
	}

}
