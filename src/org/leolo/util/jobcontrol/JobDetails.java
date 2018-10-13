package org.leolo.util.jobcontrol;

import java.util.Set;

interface JobDetails {
	
	public String getJobId();
	public String getJobName();
	public long getCreatedTime();
	public int getPriority();
	public Set<String> getDependency();
	
}
