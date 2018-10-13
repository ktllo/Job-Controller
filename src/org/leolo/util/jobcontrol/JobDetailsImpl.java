package org.leolo.util.jobcontrol;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.set.UnmodifiableSet;

public class JobDetailsImpl implements JobDetails {
	
	private String jobId;
	private String jobName;
	private long createdTime;
	private int priority;
	private Set<String> dependency = new HashSet<>();
	@Override
	public Set<String> getDependency() {
		return UnmodifiableSet.<String>unmodifiableSet(dependency);
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

	public int getDependencySize() {
		return dependency.size();
	}

	public boolean addDependency(String e) {
		return dependency.add(e);
	}

	public boolean removeDependency(Object o) {
		return dependency.remove(o);
	}

	public boolean addAllDependency(Collection<? extends String> c) {
		return dependency.addAll(c);
	}

	public boolean removeAllDependency(Collection<?> c) {
		return dependency.removeAll(c);
	}

	public void clearDependency() {
		dependency.clear();
	}

}
