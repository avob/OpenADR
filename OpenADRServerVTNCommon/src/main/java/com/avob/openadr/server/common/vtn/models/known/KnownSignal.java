package com.avob.openadr.server.common.vtn.models.known;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "oadr_known_signal")
public class KnownSignal {
	
	@EmbeddedId
	private KnownSignalId knownSignalId;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<KnownUnit> units;

	public KnownSignal() {}
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
	public List<KnownUnit> getUnits() {
		return units;
	}
	public void setUnits(List<KnownUnit> units) {
		this.units = units;
	}

}
