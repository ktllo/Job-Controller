package org.leolo.util.jobcontrol;

import java.util.Set;

/**
 * A class storing the key details an=bout a job to allow it to be used by {@link JobController}
 */
public interface JobDetails {
	
	/**
	 * Retrieve the unique ID for the job
	 * @return the unique ID for the job
	 */
	public String getJobId();
	
	/**
	 * Retrieve the name for this job
	 * @return the name for this job
	 */
	public String getJobName();
	
	/**
	 * Find the time when this job is created 
	 * @return The number of milliseconds from 1 January 1970 00:00 UTC to the time this job
	 * is created
	 * @see java.lang.System#currentTimeMillis()
	 */
	public long getCreatedTime();
	
	/**
	 * Retrieve the priority for this job
	 * @return the priority for this job
	 */
	public int getPriority();
	
	/**
	 * Retrieve the set of jobId that must be finished before the start of this job
	 * @return the set of jobId that must be finished before the start of this job
	 */
	public Set<String> getDependency();
	
	/**
	 * Retrieve the job the information stored in this class is for
	 * @return the job the information stored in this class is for
	 */
	public Job getJob();
	
	/**
	 * Calculate the number of points this job has when deciding which job to be executed.
	 * @param referenceTime The current time when the points is calculated. 
	 * @return the number of points this job has when deciding which job to be executed
	 */
	public default long getPoints(long referenceTime){
		return (referenceTime-this.getCreatedTime())*this.getPriority();
	}
	
}
