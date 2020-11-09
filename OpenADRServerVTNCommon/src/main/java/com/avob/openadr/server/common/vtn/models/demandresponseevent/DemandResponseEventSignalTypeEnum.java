package com.avob.openadr.server.common.vtn.models.demandresponseevent;

public enum DemandResponseEventSignalTypeEnum {

	/**
	 * Signal indicates the amount to change from what one would have used without
	 * the signal.
	 * 
	 */
	DELTA("delta", false),

	/**
	 * Signal indicates a program level.
	 * 
	 */
	LEVEL("level", false),

	/**
	 * Signal indicates a multiplier applied to the current rate of delivery or
	 * usage from what one would have used without the signal.
	 * 
	 */
	MULTIPLIER("multiplier", false),

	/**
	 * Signal indicates the price.
	 * 
	 */
	PRICE("price", false),

	/**
	 * Signal indicates the price multiplier. Extended price is the computed price
	 * value multiplied by the number of units.
	 * 
	 */
	PRICE_MULTIPLIER("priceMultiplier", false),

	/**
	 * Signal indicates the relative price.
	 * 
	 */
	PRICE_RELATIVE("priceRelative", false),

	/**
	 * Signal indicates a target amount of units.
	 * 
	 */
	SETPOINT("setpoint", false),

	/**
	 * This is an instruction for the load controller to operate at a level that is
	 * some percentage of its maximum load consumption capacity. This can be mapped
	 * to specific load controllers to do things like duty cycling. Note that 1.0
	 * refers to 100% consumption. In the case of simple ON/OFF type devices then 0
	 * = OFF and 1 = ON.
	 * 
	 */
	X_LOAD_CONTROL_CAPACITY("x-loadControlCapacity", true),

	/**
	 * Discrete integer levels that are relative to normal operations where 0 is
	 * normal operations.
	 * 
	 */
	X_LOAD_CONTROL_LEVEL_OFFSET("x-loadControlLevelOffset", true),

	/**
	 * Percentage change from normal load control operations.
	 * 
	 */
	X_LOAD_CONTROL_PERCENT_OFFSET("x-loadControlPercentOffset", true),

	/**
	 * Load controller set points.
	 * 
	 */
	X_LOAD_CONTROL_SETPOINT("x-loadControlSetpoint", true);

	private String label;
	private Boolean oadr20bCompatible;

	private DemandResponseEventSignalTypeEnum(String label, boolean oadr20bCompatible) {
		this.label = label;
		this.oadr20bCompatible = oadr20bCompatible;
	}

	public String getLabel() {
		return label;
	}

	public Boolean getOadr20bCompatible() {
		return oadr20bCompatible;
	}

}
