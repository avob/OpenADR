package com.avob.openadr.server.common.vtn.models.known;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class KnownUnitId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2166728620931735270L;

	private String itemDescription = "No Unit";

	private String itemUnits = "No Unit";

	private String xmlType = "No Unit";

	private String siScaleCode = "none";

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

	public String getXmlType() {
		return xmlType;
	}

	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}

	public String getSiScaleCode() {
		return siScaleCode;
	}

	public void setSiScaleCode(String siScaleCode) {
		this.siScaleCode = siScaleCode;
	}

}
