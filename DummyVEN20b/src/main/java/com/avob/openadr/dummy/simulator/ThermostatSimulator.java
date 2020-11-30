package com.avob.openadr.dummy.simulator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.avob.openadr.dummy.simulator.DummyVEN20bSimulator.Simulator;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.BaseUnitType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService.BufferValue;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveBaselineSignal;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveSignal;

public class ThermostatSimulator implements Simulator {

	private static final String END_DEVICE_ASSET = "Thermostat";

	private static final Float OPERATING_STATE_DEFAULT_VALUE = 0F;

	private static final Float HUMIDITY_DEFAULT_VALUE = 80F;

	private static final Float TEMPERATURE_SIMPLE_LEVEL_0 = 21F;
	private static final Float TEMPERATURE_SIMPLE_LEVEL_1 = 19F;
	private static final Float TEMPERATURE_SIMPLE_LEVEL_2 = 17F;
	private static final Float TEMPERATURE_SIMPLE_LEVEL_3 = 15F;

	private static final Float NORMAL_OFFSET = 0F;
	private static final Float MIN_OFFSET = 0F;
	private static final Float MAX_OFFSET = 6F;

	private static final int MOVING_AVERAGE_SIZE = 5;

	private final Queue<Float> movingAverage = new LinkedList<Float>();
	private Float sum = 0F;

	@Override
	public BufferValue readReportData(OadrReportType report, OadrReportDescriptionType description,
			List<ActiveSignal> activeSignal, List<ActiveBaselineSignal> activeBaseline) {

		ActiveBaselineSignal toBeAppliedBaseline = null;
		if (activeBaseline != null && !activeBaseline.isEmpty()) {
			toBeAppliedBaseline = activeBaseline.get(0);
		}

		ActiveSignal toBeAppliedSignalOffset = null;
		ActiveSignal toBeAppliedSignalLevel = null;
		if (activeSignal != null && !activeSignal.isEmpty()) {
			Collections.sort(activeSignal);
			for (ActiveSignal signal : activeSignal) {

				if (SignalTypeEnumeratedType.X_LOAD_CONTROL_LEVEL_OFFSET.equals(signal.getSignal().getSignalType())) {
					toBeAppliedSignalOffset = signal;
				} else if (SignalTypeEnumeratedType.LEVEL.equals(signal.getSignal().getSignalType())) {
					toBeAppliedSignalLevel = signal;
				}
			}
		}

		Float appliedTemperature = TEMPERATURE_SIMPLE_LEVEL_0;
		Float consigne = TEMPERATURE_SIMPLE_LEVEL_0;
		Float offset = 0F;
		if (toBeAppliedBaseline != null && toBeAppliedSignalOffset != null) {
			consigne = toBeAppliedBaseline.getValue();
			offset = toBeAppliedSignalOffset.getValue();
			appliedTemperature = consigne + offset;

		} else if (toBeAppliedSignalLevel != null) {
			Float levelValue = toBeAppliedSignalLevel.getValue();
			if (levelValue.equals(0F)) {
				appliedTemperature = TEMPERATURE_SIMPLE_LEVEL_0;
			} else if (levelValue.equals(1F)) {
				appliedTemperature = TEMPERATURE_SIMPLE_LEVEL_1;
			} else if (levelValue.equals(2F)) {
				appliedTemperature = TEMPERATURE_SIMPLE_LEVEL_2;
			} else if (levelValue.equals(3F)) {
				appliedTemperature = TEMPERATURE_SIMPLE_LEVEL_3;
			}
			consigne = appliedTemperature;
		}

		BufferValue value = BufferValue.of(DummyVEN20bSimulator.DEFAULT_VALUE);
		if (ReportEnumeratedType.X_RESOURCE_STATUS.value().equals(description.getReportType())) {

			boolean manualOverride = true;
			boolean online = true;

			OadrPayloadResourceStatusType status = new OadrPayloadResourceStatusType();
			OadrLoadControlStateType loadControlState = new OadrLoadControlStateType();
			OadrLoadControlStateTypeType levelOffset = new OadrLoadControlStateTypeType();
			levelOffset.setOadrCurrent(offset);
			levelOffset.setOadrMin(MIN_OFFSET);
			levelOffset.setOadrMax(MAX_OFFSET);
			levelOffset.setOadrNormal(NORMAL_OFFSET);
			loadControlState.setOadrLevelOffset(levelOffset);

			status.setOadrLoadControlState(loadControlState);

			status.setOadrManualOverride(manualOverride);

			status.setOadrOnline(online);

			value = BufferValue.of(status);

		} else if (ReportEnumeratedType.OPERATING_STATE.value().equals(description.getReportType())) {

			value = BufferValue.of(OPERATING_STATE_DEFAULT_VALUE);
		} else if (ReportEnumeratedType.USAGE.value().equals(description.getReportType())) {

			if (description.getItemBase() != null) {

				if (TemperatureType.class.equals(description.getItemBase().getDeclaredType())) {

					addData(appliedTemperature);

					value = BufferValue.of(getMean());

				} else if (BaseUnitType.class.equals(description.getItemBase().getDeclaredType())) {

					value = BufferValue.of(HUMIDITY_DEFAULT_VALUE);

				}
			}

		} else if (ReportEnumeratedType.SET_POINT.value().equals(description.getReportType())) {

			value = BufferValue.of(appliedTemperature);
		}

		return value;
	}

	@Override
	public String endDeviceAssetCompatibility() {
		return END_DEVICE_ASSET;
	}

	private void addData(Float num) {
		sum += num;
		movingAverage.add(num);

		if (movingAverage.size() > MOVING_AVERAGE_SIZE) {
			sum -= movingAverage.remove();
		}
	}

	// function to calculate mean
	private Float getMean() {
		return Math.round(100F * sum / MOVING_AVERAGE_SIZE) / 100F;
	}

}
