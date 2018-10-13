package org.leolo.util.jobcontrol;

import java.util.Set;

import org.apache.commons.collections4.set.UnmodifiableSet;

public class ImmuteableJobDetails implements JobDetails {
	
	private String jobId;
	private String jobName;
	private long createdTime;
	private int priority;
	private Set<String> dependency;
	
	@Override
	public String getJobId() {
		return jobId;
	}

	@Override
	public String getJobName() {
		return jobName;
	}

	@Override
	public long getCreatedTime() {
		return createdTime;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public Set<String> getDependency() {
		return dependency;
	}
	
	private ImmuteableJobDetails(){
		
	}
	
	public static ImmuteableJobDetails create(JobDetails job){
		ImmuteableJobDetails ijd = new ImmuteableJobDetails();
		ijd.createdTime=job.getCreatedTime();
		ijd.jobId=job.getJobId();
		ijd.jobName=job.getJobName();
		ijd.priority=job.getPriority();
		ijd.dependency=UnmodifiableSet.<String>unmodifiableSet(job.getDependency());
		return ijd;
	}

}
