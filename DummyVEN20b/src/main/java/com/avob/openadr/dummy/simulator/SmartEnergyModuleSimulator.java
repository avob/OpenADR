package com.avob.openadr.dummy.simulator;

import java.util.Collections;
import java.util.List;

import com.avob.openadr.dummy.simulator.DummyVEN20bSimulator.Simulator;
import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.power.EnergyRealType;
import com.avob.openadr.model.oadr20b.power.PowerRealType;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService.BufferValue;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveBaselineSignal;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveSignal;

public class SmartEnergyModuleSimulator implements Simulator {

	private static final String END_DEVICE_ASSET = "Smart_Energy_Module";

	private static final Float DEFAULT_VOLTAGE = 1000F;

	private static final Float VOLTAGE_SIMPLE_LEVEL_0 = DEFAULT_VOLTAGE;
	private static final Float VOLTAGE_SIMPLE_LEVEL_1 = DEFAULT_VOLTAGE + 100;
	private static final Float VOLTAGE_SIMPLE_LEVEL_2 = DEFAULT_VOLTAGE + 200;
	private static final Float VOLTAGE_SIMPLE_LEVEL_3 = DEFAULT_VOLTAGE + 300;

	private Float lastObservedVoltage = DEFAULT_VOLTAGE;
	private Long lastObservedDate = System.currentTimeMillis();

	@Override
	public BufferValue readReportData(OadrReportType report, OadrReportDescriptionType description,
			List<ActiveSignal> activeSignal, List<ActiveBaselineSignal> activeBaseline) {

		ActiveSignal toBeAppliedSignalSetPoint = null;
		ActiveSignal toBeAppliedSignalLevel = null;
		if (activeSignal != null && !activeSignal.isEmpty()) {
			Collections.sort(activeSignal);
			for (ActiveSignal signal : activeSignal) {

				if (SignalNameEnumeratedType.LOAD_DISPATCH.value().equals(signal.getSignal().getSignalName())
						&& SignalTypeEnumeratedType.SETPOINT.equals(signal.getSignal().getSignalType())) {
					toBeAppliedSignalSetPoint = signal;
				} else if (SignalTypeEnumeratedType.LEVEL.equals(signal.getSignal().getSignalType())) {
					toBeAppliedSignalLevel = signal;
				}
			}
		}

		Float appliedVoltage = DEFAULT_VOLTAGE;

		if (toBeAppliedSignalSetPoint != null) {
			appliedVoltage = toBeAppliedSignalSetPoint.getValue();

		} else if (toBeAppliedSignalLevel != null) {
			Float levelValue = toBeAppliedSignalLevel.getValue();
			if (levelValue.equals(0F)) {
				appliedVoltage = VOLTAGE_SIMPLE_LEVEL_0;
			} else if (levelValue.equals(1F)) {
				appliedVoltage = VOLTAGE_SIMPLE_LEVEL_1;
			} else if (levelValue.equals(2F)) {
				appliedVoltage = VOLTAGE_SIMPLE_LEVEL_2;
			} else if (levelValue.equals(3F)) {
				appliedVoltage = VOLTAGE_SIMPLE_LEVEL_3;
			}
		}

		BufferValue value = BufferValue.of(DummyVEN20bSimulator.DEFAULT_VALUE);
		if (description.getItemBase() != null) {
			if (description.getItemBase().getDeclaredType().equals(PowerRealType.class)) {

				value = BufferValue.of(appliedVoltage);

			} else if (description.getItemBase().getDeclaredType().equals(EnergyRealType.class)) {

				Long durationMillis = System.currentTimeMillis() - lastObservedDate;

				Float observedEnergy = Math
						.round(100 * (Float.valueOf(durationMillis) * lastObservedVoltage / 1000 / 3600)) / 100F;

				value = BufferValue.of(observedEnergy);

				lastObservedDate = System.currentTimeMillis();

				lastObservedVoltage = appliedVoltage;

			}
		}

		return value;
	}

	@Override
	public String endDeviceAssetCompatibility() {
		return END_DEVICE_ASSET;
	}

}
