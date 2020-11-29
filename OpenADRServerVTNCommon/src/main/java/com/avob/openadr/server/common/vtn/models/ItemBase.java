package com.avob.openadr.server.common.vtn.models;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class ItemBase {

	private String itemDescription;

	private String itemUnits;

	private String siScaleCode;

	private String xmlType;

	@Lob
	private String attributes;

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

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

}
