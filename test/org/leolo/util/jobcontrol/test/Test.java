package org.leolo.util.jobcontrol.test;

import static org.junit.Assert.*;

import org.leolo.util.jobcontrol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

public class Test {
	
	public static int num = 0;
	public static String com = null;
	Logger log = LoggerFactory.getLogger(Test.class);
	
	
	@org.junit.Test
	public void test1() {
		num = 0;
		JobController controller = new JobController();
		JobDetailsImpl jd1 = new JobDetailsImpl();
		jd1.setCreatedTime(System.currentTimeMillis());
		jd1.setJobId("AA");
		jd1.setJobName("AA");
		jd1.setJob(new Job(){

			@Override
			public void run() {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
				Test.num++;
			}
			
		});
		controller.addJob(jd1);
		controller.start();
	}
	
	@org.junit.Test
	public void test2() {
		num = 0;
		JobController controller = new JobController();
		for(int i=0;i<10;i++){
			JobDetailsImpl jd1 = new JobDetailsImpl();
			jd1.setCreatedTime(System.currentTimeMillis());
			jd1.setJobId("AA"+i);
			jd1.setJobName("AA"+i);
			final String seq = Integer.toString(i);
			jd1.setJob(new Job(){
	
				@Override
				public void run() {
					log.info("start{}", seq);
					try {
						
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						log.error(e.getMessage(), e);
					}
					Test.num++;
					log.info("end{}", seq);
				}
				
			});
			controller.addJob(jd1);
		}
		controller.start();
	}
	
	@org.junit.Test
	public void test3(){
		num = 0;
		com = null;
		JobController controller = new JobController();
		JobDetailsImpl jd1 = new JobDetailsImpl();
		jd1.setCreatedTime(System.currentTimeMillis());
		jd1.setJobId("AA");
		jd1.setJobName("AA");
		jd1.setJob(new Job(){

			@Override
			public void run() {
				try {
					log.info("AA-start");
					Thread.sleep(2500);
					com = "";
					log.info("AA-end");
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
				Test.num++;
			}
			
		});
		
		JobDetailsImpl jd2 = new JobDetailsImpl();
		jd2.setCreatedTime(System.currentTimeMillis());
		jd2.setJobId("AB");
		jd2.setJobName("AB");
		jd2.setJob(new Job(){

			@Override
			public void run() {
				try {
					log.info("AB-start");
					log.info(com);
					Thread.sleep(2500);
					log.info("AB-end");
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
				Test.num++;
			}
			
		});
		jd2.addDependency("AA");
//		jd1.addDependency("AB");
		controller.addJob(jd2);
		controller.addJob(jd1);
		controller.start();
	}

}
