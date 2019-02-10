package fr.pierrezemb.beacon.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkJobInformation {
    private String jid;
	private String name;
	private String state;

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "FlinkJobInformation{" +
				"jid='" + jid + '\'' +
				", name='" + name + '\'' +
				", state='" + state + '\'' +
				'}';
}
}
