package fr.pierrezemb.beacon.api;

import java.util.ArrayList;

public class FlinkMonitoringJobOverview {
    	private ArrayList<FlinkJobInformation> jobs;

	public ArrayList<FlinkJobInformation> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<FlinkJobInformation> jobs) {
		this.jobs = jobs;
}
}
