package org.leolo.util.jobcontrol;


/**
 * The super class for all the job executed by the JobController.
 *
 */
public abstract class Job {
	
	private JobStatus status;
	private JobController controller;
	
	
	/**
	 * An implementation must override this function to perform the
	 * actual job needed.
	 */
	public abstract void run();
	
	/**
	 * Find the current status of this job
	 * 
	 * @return The current status of this job
	 */
	public JobStatus getStatus() {
		return status;
	}
	
	/**
	 * Update the current status of the job
	 * @param status the new status of the job
	 */
	public void setStatus(JobStatus status) {
		this.status = status;
	}
	
	/**
	 * Find the {@link JobController} this job is attached to.
	 * @return the {@link JobController} this job is attached to
	 */
	public JobController getController() {
		return controller;
	}

	protected void setController(JobController controller) {
		this.controller = controller;
	}
}
