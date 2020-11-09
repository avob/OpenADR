package com.avob.openadr.server.common.vtn.models.demandresponseevent;

public enum DemandResponseEventSignalNameEnum {
	
	/**
     * Simple levels (OpenADR 2.0a compliant)
     * 
     */
    SIMPLE("SIMPLE"),

    /**
     * depreciated - for backwards compatibility with A profile
     * 
     */
    DEPRECATED_OADR20A_SIMPLE("simple"),

    /**
     * This is the cost of electricity
     * 
     */
    ELECTRICITY_PRICE("ELECTRICITY_PRICE"),

    /**
     * This is the cost of energy
     * 
     */
    ENERGY_PRICE("ENERGY_PRICE"),

    /**
     * This is the demand charge
     * 
     */
    DEMAND_CHARGE("DEMAND_CHARGE"),

    /**
     * This is the price that was bid by the resource
     * 
     */
    BID_PRICE("BID_PRICE"),

    /**
     * This is the amount of load that was bid by a resource into a program
     * 
     */
    BID_LOAD("BID_LOAD"),

    /**
     * This is the amount of energy from a resource that was bid into a program
     * 
     */
    BID_ENERGY("BID_ENERGY"),

    /**
     * State of energy storage resource
     * 
     */
    CHARGE_STATE("CHARGE_STATE"),

    /**
     * This is used to dispatch load
     * 
     */
    LOAD_DISPATCH("LOAD_DISPATCH"),

    /**
     * Set load output to relative values
     * 
     */
    LOAD_CONTROL("LOAD_CONTROL");
	
	private String label;
	
	private DemandResponseEventSignalNameEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
