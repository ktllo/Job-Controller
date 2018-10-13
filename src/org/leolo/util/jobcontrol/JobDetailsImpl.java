package org.leolo.util.jobcontrol;

import java.util.Set;

public class JobDetailsImpl implements JobDetails {
	
	private String jobId;
	private String jobName;
	private long createdTime;
	private int priority;

	@Override
	public Set<String> getDependency() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
