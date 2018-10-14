package org.leolo.util.jobcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobController {
	
	public final int MAX_THREAD_COUNT;
	private JobControllerThread thread;
	
	private Logger log = LoggerFactory.getLogger(JobController.class);
	
	private String synchronizeToken = new String();
	
	private Map<String, ImmuteableJobDetails> jobList;
	
	private List<JobThread> threads;
	
	public JobController(int maxThreadCount){
		MAX_THREAD_COUNT = maxThreadCount;
		jobList = new ConcurrentHashMap<>();
		thread = new JobControllerThread();
		threads = new Vector<>(MAX_THREAD_COUNT);
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
mainLoop:	while(true){
				//Checks is there spare threads
				int threadCount = 0;
				for(JobThread t:threads){
					if(t.isAlive())
						threadCount++;
				}
				if(threadCount >= MAX_THREAD_COUNT){
					continue mainLoop;
				}
				//Update the block status
blkChkLoop:		for(ImmuteableJobDetails j:jobList.values()){
					if(j.getJob().getStatus()==JobStatus.BLOCKED){
						for(String tid:j.getDependency()){
							JobDetails depencency = jobList.get(tid);
							if(depencency==null){
								log.warn("Dependency {} does not exists.", tid);
							}else{
								if(depencency.getJob().getStatus() != JobStatus.FINISHED){
									continue blkChkLoop;
								}
							}
						}
						j.getJob().setStatus(JobStatus.PENDING);
					}
				}
				//Check is there any job left, add them to a temp. list
				List<ImmuteableJobDetails> pending = new ArrayList<>();
				for(ImmuteableJobDetails j:jobList.values()){
					if(j.getJob().getStatus()==JobStatus.PENDING){
						pending.add(j);
					}
				}
				final long REFERENCE_TIME = System.currentTimeMillis();
				long maxPoint = Long.MIN_VALUE;
				ImmuteableJobDetails targetJob = null;
				for(ImmuteableJobDetails ijd:pending){
					long point = ijd.getPoints(REFERENCE_TIME);
					if(point>maxPoint){
						maxPoint = point;
						targetJob = ijd;
					}
				}
				//Start the thread
				new JobThread(targetJob).start();
				synchronized(getSynchronizeToken()){
					try {
						getSynchronizeToken().wait();
					} catch (InterruptedException e) {
						log.error(e.getLocalizedMessage(), e);
					}
				}
			}
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
			for(ImmuteableJobDetails j:jobList.values()){
				if(j.getJob().getStatus()!=JobStatus.FINISHED){
					continue outer;
				}
			}
			break outer;
		}
	}
	
	private class JobThread extends Thread{
		ImmuteableJobDetails ijd;
		JobThread(ImmuteableJobDetails ijd){
			this.ijd = ijd;
		}
		
		public void run(){
			ijd.getJob().setStatus(JobStatus.RUNNING);
			ijd.getJob().run();
			ijd.getJob().setStatus(JobStatus.FINISHED);
			synchronized(getSynchronizeToken()){
				getSynchronizeToken().notifyAll();
			}
		}
	}
}
