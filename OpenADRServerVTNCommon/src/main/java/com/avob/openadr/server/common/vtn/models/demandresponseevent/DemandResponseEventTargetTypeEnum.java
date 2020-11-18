package com.avob.openadr.server.common.vtn.models.demandresponseevent;

public enum DemandResponseEventTargetTypeEnum {
	
	VEN("ven"), GROUP("group");
	
	private String label;
	
	private DemandResponseEventTargetTypeEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	

}
