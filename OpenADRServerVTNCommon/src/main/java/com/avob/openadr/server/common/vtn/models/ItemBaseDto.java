package com.avob.openadr.server.common.vtn.models;

public class ItemBaseDto {

	private String itemDescription;

	private String itemUnits;

	private String siScaleCode;
	
	private String xmlType;

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemUnits() {
		return itemUnits;
	}

	public void setItemUnits(String itemUnits) {
		this.itemUnits = itemUnits;
	}

	public String getSiScaleCode() {
		return siScaleCode;
	}

	public void setSiScaleCode(String siScaleCode) {
		this.siScaleCode = siScaleCode;
	}

	public String getXmlType() {
		return xmlType;
	}

	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}
}
