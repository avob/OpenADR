package com.avob.openadr.server.common.vtn.models.known;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "oadr_known_unit")
public class KnownUnit {

	@EmbeddedId
	private KnownUnitId knownUnitId;

	@Lob
	private String attributes;

	public KnownUnitId getKnownUnitId() {
		return knownUnitId;
	}

	public void setKnownUnitId(KnownUnitId knownUnitId) {
		this.knownUnitId = knownUnitId;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
}
