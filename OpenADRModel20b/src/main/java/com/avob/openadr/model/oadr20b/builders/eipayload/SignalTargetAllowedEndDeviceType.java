package com.avob.openadr.model.oadr20b.builders.eipayload;

public enum SignalTargetAllowedEndDeviceType {
	THERMOSTAT("Thermostat"), BASEBOARD_HEATER("Baseboard_Heater"), STRIP_HEATER("Strip_Heater"),
	WATER_HEATER("Water_Heater"), POOL_PUMP("Pool_Pump"), SAUNA("Sauna"), HOT_TUB("Hot_tub"),
	SMART_APPLIANCE("Smart_Appliance"), IRRIGATION_PUMP("Irrigation_Pump"),
	MANAGED_COMMERCIAL_AND_INDUSTRIAL_LOADS("Managed_Commercial_and_Industrial_Loads"),
	SIMPLE_RESIDENTIAL_ON_OFF_LOADS("Simple_Residential_On_Off_Loads"), EXTERIOR_LIGHTING("Exterior_Lighting"),
	INTERIOR_LIGHTING("Interior_Lighting"), ELECTRIC_VEHICLE("Electric_Vehicle"),
	GENERATION_SYSTEMS("Generation_Systems"), LOAD_CONTROL_SWITCH("Load_Control_Switch"),
	SMART_INVERTER("Smart_Inverter"), EVSE("EVSE"), RESU("RESU"), ENERGY_MANAGEMENT_SYSTEM("Energy_Management_System"),
	SMART_ENERGY_MODULE("Smart_Energy_Module"), STORAGE("Storage");

	private String label;

	private SignalTargetAllowedEndDeviceType(String label) {
		this.label = label;

	}

	public String label() {
		return this.label;
	}
}
