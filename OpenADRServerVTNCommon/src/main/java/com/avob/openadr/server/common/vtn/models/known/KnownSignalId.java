package com.avob.openadr.server.common.vtn.models.known;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalTypeEnum;

@Embeddable
public class KnownSignalId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1230009403103963009L;

	@NotNull
	private String signalName;

	@NotNull
	private DemandResponseEventSignalTypeEnum signalType;

	@OneToOne
	private KnownUnit unit;

	public KnownSignalId() {
	}

	public KnownSignalId(@NotNull String signalName, @NotNull DemandResponseEventSignalTypeEnum signalType,
			KnownUnit unit) {
		super();
		this.setSignalName(signalName);
		this.setSignalType(signalType);
		this.setUnit(unit);
	}

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public DemandResponseEventSignalTypeEnum getSignalType() {
		return signalType;
	}

	public void setSignalType(DemandResponseEventSignalTypeEnum signalType) {
		this.signalType = signalType;
	}

	public KnownUnit getUnit() {
		return unit;
	}

	public void setUnit(KnownUnit unit) {
		this.unit = unit;
	}

}
