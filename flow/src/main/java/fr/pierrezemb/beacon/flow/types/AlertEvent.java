package fr.pierrezemb.beacon.flow.types;

public class AlertEvent {

	private String team;
	private String selector;
	private boolean isActive;
	private long since;

	public AlertEvent() {
	}

	public AlertEvent(String team, String selector, boolean isActive) {
		this.team = team;
		this.selector = selector;
		this.isActive = isActive;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public long getSince() {
		return since;
	}

	public void setSince(long since) {
		this.since = since;
	}

	@Override
	public String toString() {
		return "AlertEvent{" +
				"team='" + team + '\'' +
				", selector='" + selector + '\'' +
				", isActive=" + isActive +
				", since=" + since +
				'}';
	}
}
