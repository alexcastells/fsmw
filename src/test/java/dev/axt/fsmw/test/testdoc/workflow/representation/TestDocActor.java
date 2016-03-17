package dev.axt.fsmw.test.testdoc.workflow.representation;

import dev.axt.fsmw.representation.Actor;
import dev.axt.fsmw.test.testdoc.workflow.representation.TestDocActor.ROLE;

/**
 * TestDoc workflow actor
 *
 * @author alextremp
 */
public class TestDocActor extends Actor<ROLE> {

	public static enum ROLE {

		USER,
		ADMIN;
	}

	public TestDocActor(ROLE role) {
		super(role);
	}

}
