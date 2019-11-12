package com.avob.openadr.server.common.vtn.broker.rabbitmq;

public class TopicCheck extends ResourceCheck {

	private String routing_key;

	public String getRouting_key() {
		return routing_key;
	}

	public void setRouting_key(String routing_key) {
		this.routing_key = routing_key;
	}

}
