package dev.axt.fsmw.representation;

public class ActorTrigger<TRIGGER, ROLE> extends Trigger<TRIGGER> {

	private final Actor<ROLE> actor;

	public ActorTrigger(Actor<ROLE> actor, TRIGGER trigger) {
		super(trigger);
		this.actor = actor;
	}

	public Actor<ROLE> getActor() {
		return actor;
	}

	@Override
	public String toString() {
		return "ActorTrigger[" + getTrigger() + " by " + actor + "]";
	}

}
