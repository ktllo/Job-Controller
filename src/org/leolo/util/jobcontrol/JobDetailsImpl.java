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
	private Job job;
	
	@Override
	public Job getJob(){
		return job;
	}
	
	@Override
	public Set<String> getDependency() {
		return UnmodifiableSet.<String>unmodifiableSet(dependency);
	}

	public String getJobId() {
		return jobId;
	}
	
	/**
	 * Updates the unique ID for this job
	 * @param jobId the unique ID for this job
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	/**
	 * Updates the name for the job
	 * @param jobName the name for the job
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getCreatedTime() {
		return createdTime;
	}
	
	/**
	 * Updates the time that the job is created
	 * @param createdTime the time that the job is created
	 */
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public int getPriority() {
		return priority;
	}
	
	/**
	 * Updates the priority for the job
	 * @param priority priority for the job
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * Get the number of dependency this job has
	 * @return number of dependency this job has
	 */
	public int getDependencySize() {
		return dependency.size();
	}
	
	/**
	 * Add a new dependency to this job
	 * @param e The jobId for the new dependency
	 * @return true of the dependency does not currently exists
	 * @see java.util.Set#add(Object)
	 */
	public boolean addDependency(String e) {
		return dependency.add(e);
	}
	
	/**
	 * Removes an dependency from this job
	 * @param o The jobId for the new dependency
	 * @return true if a dependency is removed
	 * @see java.util.Set#remove(Object)
	 */
	public boolean removeDependency(Object o) {
		return dependency.remove(o);
	}
	
	/**
	 * Add all of the dependency from the provided list 
	 * @param c A list containing the jobID for the new dependency
	 * @return List of dependency is changed
	 * @see java.util.Set#addAll(Collection)
	 */
	public boolean addAllDependency(Collection<? extends String> c) {
		return dependency.addAll(c);
	}
	
	/**
	 * Remove all the dependency listed in the given collection
	 * @param c list containing the jobID to be removed as dependency
	 * @return List of dependency is changed
	 * @see java.util.Set#removeAll(Collection)
	 */
	public boolean removeAllDependency(Collection<?> c) {
		return dependency.removeAll(c);
	}
	
	/**
	 * Remove all the dependency for this job.
	 * @see java.util.Set#clear()
	 */
	public void clearDependency() {
		dependency.clear();
	}
	
	
	/**
	 * Updates the job the information stored in this class is for
	 * @param job the job the information stored in this class is for
	 */
	public void setJob(Job job) {
		this.job = job;
	}

}
