package com.sample.app.dto;

public class SystemDetails {

	private String serviceName;
	private String healthEndpoint;
	private boolean health;
	private int responseCode;
	private long responseTimeInMillis;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHealthEndpoint() {
		return healthEndpoint;
	}

	public void setHealthEndpoint(String healthEndpoint) {
		this.healthEndpoint = healthEndpoint;
	}

	public boolean isHealth() {
		return health;
	}

	public void setHealth(boolean health) {
		this.health = health;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public long getResponseTimeInMillis() {
		return responseTimeInMillis;
	}

	public void setResponseTimeInMillis(long responseTimeInMillis) {
		this.responseTimeInMillis = responseTimeInMillis;
	}

}
