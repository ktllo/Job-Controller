package org.leolo.util.jobcontrol;

public enum JobStatus {
	/**
	 * Indicating the job is being blocked because the prerequisite of the job has not been
	 * fulfilled yet.
	 */
	BLOCKED,
	/**
	 * Indicate the job is ready to be run.
	 */
	PENDING,
	/**
	 * Indicate the job is being executed
	 */
	RUNNING,
	/**
	 * Indicate the job has finished it's execution
	 */
	FINISHED;
}
