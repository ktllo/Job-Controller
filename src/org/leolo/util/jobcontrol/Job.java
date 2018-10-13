package org.leolo.util.jobcontrol;

public abstract class Job {
	
	private JobStatus status;
	private JobController controller;
	private JobDetails jobDetails;
	
	
	/**
	 * The implementation must override this function to perform the
	 * actual job needed.
	 */
	public abstract void run();
	
	public Job(){
		status = JobStatus.PENDING;
	}
	
	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public JobController getController() {
		return controller;
	}

	protected void setController(JobController controller) {
		this.controller = controller;
	}

	class JobThread implements Runnable{
		
		public void run(){
			if(controller != null){
				status = JobStatus.RUNNING;
				Job.this.run();
				status = JobStatus.FINISHED;
			}
		}
	}
}
