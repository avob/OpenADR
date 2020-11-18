package com.avob.openadr.server.common.vtn.models;

public enum TargetTypeEnum {
	
	VEN("ven"), GROUP("group"), ENDDEVICE_ASSET("endDeviceAsset");
	
	private String label;
	
	private TargetTypeEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	

}
