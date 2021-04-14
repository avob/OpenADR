package com.avob.openadr.server.common.vtn.models.demandresponseevent.sort;

import org.springframework.data.domain.Sort;

public class DemandResponseEventSort {

	private String property;
	private Sort.Direction type;
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Sort.Direction getType() {
		return type;
	}
	public void setType(Sort.Direction type) {
		this.type = type;
	}
	
}
