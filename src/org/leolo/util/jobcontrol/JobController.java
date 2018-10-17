package org.leolo.util.jobcontrol;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobController {
	
	/**
	 * The maximum number of thread to be started at same time
	 */
	public final int MAX_THREAD_COUNT;
	private JobControllerThread thread;
	
	private Logger log = LoggerFactory.getLogger(JobController.class);
	
	private String synchronizeToken = new String();
	
	private Map<String, ImmuteableJobDetails> jobList;
	
	private List<JobThread> threads;
	
	/**
	 * Create an instance of JobController with the specific maximum number of thread
	 * @param maxThreadCount maximum number of thread to be started
	 */
	public JobController(int maxThreadCount){
		MAX_THREAD_COUNT = maxThreadCount;
		jobList = new ConcurrentHashMap<>();
		thread = new JobControllerThread();
		threads = new Vector<>(MAX_THREAD_COUNT);
	}
	
	/**
	 * Create an instance of JobController with the default maximum number of thread
	 */
	public JobController(){
		this(5);
	}
	
	/**
	 * Get the token used for synchronization 
	 * @return token used for synchronization 
	 */
	public String getSynchronizeToken() {
		return synchronizeToken;
	}
	/**
	 * Add a new job this job controller
	 * @param jd the new job
	 */
	public void addJob(JobDetails jd){
		if(jobList.containsKey(jd.getJobId())){
			throw new RuntimeException("Job ID already exists");
		}
		for(String dep:jd.getDependency()){
			JobDetails depjd = jobList.get(dep);
			if(depjd!=null){
				if(depjd.getDependency().contains(jd.getJobId())){
					throw new RuntimeException("Loop in dependency found!");
				}
			}
		}
		jd.getJob().setStatus(JobStatus.BLOCKED);
		jobList.put(jd.getJobId(), ImmuteableJobDetails.create(jd));
	}
	
	private int threadCount(){
		int threadCount = 0;
		for(JobThread t:threads){
			if(t.getState()!=State.TERMINATED)
				threadCount++;
		}
		return threadCount;
	}
	
	private class JobControllerThread extends Thread{
		
		public JobControllerThread(){
			this.setName("Controller");
		}
		
		public void run(){
mainLoop:	while(true){
				//Checks is there spare threads
				
				if(threadCount() >= MAX_THREAD_COUNT){
					synchronized(getSynchronizeToken()){
						try {
							getSynchronizeToken().wait();
						} catch (InterruptedException e) {
							log.error(e.getLocalizedMessage(), e);
						}
					}
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
					log.debug("Point for {} is {}", ijd.getJobId(), point);
					if(point>maxPoint){
						maxPoint = point;
						targetJob = ijd;
					}
				}
				//Start the thread
				if(targetJob!=null){
					JobThread jt = new JobThread(targetJob);
					targetJob.getJob().setStatus(JobStatus.RUNNING);
					threads.add(jt);
					jt.start();
				}
				
			}
		}
	}
	
	/**
	 * Start running the job in the job controller. This method will return once all the 
	 * job in this controller has been finished.
	 */
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
			try{
				ijd.getJob().run();
				ijd.getJob().setStatus(JobStatus.FINISHED);
			}catch(RuntimeException re){
				log.error(re.getLocalizedMessage(), re);
			}synchronized(getSynchronizeToken()){
				getSynchronizeToken().notifyAll();
			}
		}
	}
}
