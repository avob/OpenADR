package com.avob.openadr.server.common.vtn.models.known;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSignalTypeEnum;

@Embeddable
public class KnownSignalId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1230009403103963009L;

	@NotNull
	private DemandResponseEventSignalNameEnum signalName;

	@NotNull
	private DemandResponseEventSignalTypeEnum signalType;

	public KnownSignalId() {
	}

	public KnownSignalId(@NotNull DemandResponseEventSignalNameEnum signalName,
			@NotNull DemandResponseEventSignalTypeEnum signalType) {
		super();
		this.setSignalName(signalName);
		this.setSignalType(signalType);
	}

	public DemandResponseEventSignalNameEnum getSignalName() {
		return signalName;
	}

	public void setSignalName(DemandResponseEventSignalNameEnum signalName) {
		this.signalName = signalName;
	}

	public DemandResponseEventSignalTypeEnum getSignalType() {
		return signalType;
	}

	public void setSignalType(DemandResponseEventSignalTypeEnum signalType) {
		this.signalType = signalType;
	}

}
