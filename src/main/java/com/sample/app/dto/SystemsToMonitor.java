package com.sample.app.dto;

import java.util.List;


public class SystemsToMonitor {
	private List<SystemDetails> systemsDetails;

	private String envType;

	public List<SystemDetails> getSystemsDetails() {
		return systemsDetails;
	}

	public void setSystemsDetails(List<SystemDetails> systemsDetails) {
		this.systemsDetails = systemsDetails;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

}