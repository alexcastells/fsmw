package dev.axt.fsmw.test.testdoc.workflow.representation;

/**
 *
 * @author alextremp
 */
public class TestDocStatus {

	public static enum STATUS {

		NULL,
		INVALID,
		DRAFT,
		REVIEW,
		ACCEPTED,
		DENIED;

		public static STATUS string2status(String s) {
			if (s == null) {
				return NULL;
			}
			try {
				return STATUS.valueOf(s);
			} catch (IllegalArgumentException ignored) {
				return INVALID;
			}
		}

		public static String status2string(STATUS status) {
			if (status == null || NULL.equals(status)) {
				return null;
			}
			if (INVALID.equals(status)) {
				throw new IllegalArgumentException("INVALID status");
			}
			return status.name();
		}
	}

}
