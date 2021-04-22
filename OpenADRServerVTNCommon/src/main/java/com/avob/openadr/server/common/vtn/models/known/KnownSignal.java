package com.avob.openadr.server.common.vtn.models.known;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oadr_known_signal")
public class KnownSignal {

	@EmbeddedId
	private KnownSignalId knownSignalId;

	public KnownSignal() {
	}

	public KnownSignal(KnownSignalId knownSignalId) {
		super();
		this.knownSignalId = knownSignalId;
	}

	public KnownSignalId getKnownSignalId() {
		return knownSignalId;
	}

	public void setKnownSignalId(KnownSignalId knownSignalId) {
		this.knownSignalId = knownSignalId;
	}

}
