package com.avob.openadr.server.common.vtn.models.known;

import java.io.Serializable;

import javax.persistence.Embeddable;
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

	private KnownUnitId itemBase;

	public KnownSignalId() {
	}

	public KnownSignalId(@NotNull String signalName, @NotNull DemandResponseEventSignalTypeEnum signalType,
			KnownUnitId itemBase) {
		super();
		this.setSignalName(signalName);
		this.setSignalType(signalType);
		this.setItemBase(itemBase);
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

	public KnownUnitId getItemBase() {
		return itemBase;
	}

	public void setItemBase(KnownUnitId itemBase) {
		this.itemBase = itemBase;
	}

}
