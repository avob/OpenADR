package com.avob.openadr.server.common.vtn.broker.rabbitmq;

public class BaseCheck {

	private String username;

	private String vhost;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + "username='" + username + '\'' + ", vhost='" + vhost + '\'' + '}';
	}
}