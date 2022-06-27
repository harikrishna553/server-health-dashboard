package com.sample.app.dto;

import java.io.Serializable;
import java.util.List;

public class SystemsToMonitorRootModel implements Serializable {

	private String id;

	private List<SystemsToMonitor> systemsToMonitor;

	public List<SystemsToMonitor> getSystemsToMonitor() {
		return systemsToMonitor;
	}

	public void setSystemsToMonitor(List<SystemsToMonitor> systemsToMonitor) {
		this.systemsToMonitor = systemsToMonitor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
