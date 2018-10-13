package org.leolo.util.jobcontrol;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobController {
	
	public final int MAX_THREAD_COUNT;
	private JobControllerThread thread;
	
	private Logger log = LoggerFactory.getLogger(JobController.class);
	
	private String synchronizeToken = new String();
	
	private List<Job> jobList;
	
	public JobController(int maxThreadCount){
		MAX_THREAD_COUNT = maxThreadCount;
		jobList = new Vector<>();
		thread = new JobControllerThread();
	}
	
	public JobController(){
		this(5);
	}
	
	public String getSynchronizeToken() {
		return synchronizeToken;
	}

	private class JobControllerThread extends Thread{
		
		public JobControllerThread(){
			this.setName("Controller");
		}
		
		public void run(){
			
		}
	}
	
	public void start(){
		thread.start();
outer:	while(true){
			synchronized(this.getSynchronizeToken()){
				try {
					this.getSynchronizeToken().wait();
				} catch (InterruptedException e) {
					log.error(e.getLocalizedMessage(), e);
				}
			}
			for(Job j:jobList){
				if(j.getStatus()!=JobStatus.FINISHED){
					continue outer;
				}
			}
			break outer;
		}
	}
}
