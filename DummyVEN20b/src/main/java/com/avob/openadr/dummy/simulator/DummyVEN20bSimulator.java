package com.avob.openadr.dummy.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.ei.EiEventBaselineType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.power.EndDeviceAssetType;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService.BufferValue;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService.UpdateReportOrchestratorListener;

@Service
public class DummyVEN20bSimulator {

//	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiReportListener.class);

	protected static final Float DEFAULT_VALUE = -1F;

	protected static final String DEFAULT_ENDDEVICE_ASSET = "DEFAULT_ENDDEVICE_ASSET";

	@Resource
	private UpdateReportOrchestratorService updateReportOrchestratorService;

	/**
	 * <sessionKey,< endDeviceAsset, <eventId+signalId+intervalId, SignalValue>>>
	 */
	private Map<String, Map<String, Map<String, ActiveSignal>>> activeSignals = new ConcurrentHashMap<>();
	private Map<String, Map<String, ActiveBaseline>> baselineActiveSignal = new ConcurrentHashMap<>();

	private List<Simulator> simulators = Arrays.asList(new ThermostatSimulator(), new SmartEnergyModuleSimulator());

	public interface Simulator {
		BufferValue readReportData(OadrReportType report, OadrReportDescriptionType description,
				List<ActiveSignal> activeSignal, List<ActiveBaseline> activeBaseline);

		String endDeviceAssetCompatibility();
	}

	private List<ActiveSignal> getActiveSignal(VtnSessionConfiguration vtnConfiguration, String endDeviceAsset) {
		List<ActiveSignal> res = null;
		Map<String, Map<String, ActiveSignal>> perSession = activeSignals.get(vtnConfiguration.getSessionKey());
		if (perSession != null) {
			Map<String, ActiveSignal> perEndDeviceAsset = perSession.get(endDeviceAsset);
			if (perEndDeviceAsset != null) {
				res = new ArrayList<>(perEndDeviceAsset.values());
			}
		}
		return res;
	}

	private List<ActiveBaseline> getActiveBaseline(VtnSessionConfiguration vtnConfiguration) {
		List<ActiveBaseline> res = null;
		Map<String, ActiveBaseline> map = baselineActiveSignal.get(vtnConfiguration.getSessionKey());
		if (map != null) {
			res = new ArrayList<>(map.values());
		}
		return res;
	}

	public void create(VtnSessionConfiguration vtnConfig, OadrCreateReportType oadrCreateReportType) {
		updateReportOrchestratorService.create(vtnConfig, oadrCreateReportType, new UpdateReportOrchestratorListener() {

			@Override
			public BufferValue readReportData(OadrReportType report, OadrReportDescriptionType description) {
				String endDeviceAsset = DEFAULT_ENDDEVICE_ASSET;
				if (!description.getReportSubject().getEndDeviceAsset().isEmpty()) {
					endDeviceAsset = description.getReportSubject().getEndDeviceAsset().get(0).getMrid();
				}

				BufferValue res = BufferValue.of(DEFAULT_VALUE);
				final String endDeviceAssetFinal = endDeviceAsset;
				List<Simulator> collect = simulators.stream()
						.filter(simulator -> simulator.endDeviceAssetCompatibility().equals(endDeviceAssetFinal))
						.collect(Collectors.toList());
				if (!collect.isEmpty()) {
					Simulator simulator = collect.get(0);
					res = simulator.readReportData(report, description, getActiveSignal(vtnConfig, endDeviceAsset),
							getActiveBaseline(vtnConfig));
				}
				return res;

			}

		});
	}

	public void cancel(VtnSessionConfiguration vtnConfig, OadrCancelReportType oadrCancelReportType) {
		updateReportOrchestratorService.cancel(vtnConfig, oadrCancelReportType);
	}

	public void onIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {

		Float value = null;
		Long priority = event.getEiEvent().getEventDescriptor().getPriority();
		String eventId = event.getEiEvent().getEventDescriptor().getEventID();
		String signalId = eiEventSignalType.getSignalID();
		String intervalId = intervalType.getUid().getText();
		String activeSignalUUID = String.format("%s-%s-%s", eventId, signalId, intervalId);

		List<String> collect;
		if (eiEventSignalType.getEiTarget() != null && eiEventSignalType.getEiTarget().getEndDeviceAsset() != null
				&& !eiEventSignalType.getEiTarget().getEndDeviceAsset().isEmpty()) {
			collect = eiEventSignalType.getEiTarget().getEndDeviceAsset().stream().map(EndDeviceAssetType::getMrid)
					.collect(Collectors.toList());
		} else {
			collect = new ArrayList<>();
			collect.add(DEFAULT_ENDDEVICE_ASSET);
		}

		for (JAXBElement<? extends StreamPayloadBaseType> payload : intervalType.getStreamPayloadBase()) {
			if (payload.getDeclaredType().equals(SignalPayloadType.class)) {

				SignalPayloadType reportPayload = (SignalPayloadType) payload.getValue();

				JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
				if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {

					PayloadFloatType payloadFloat = (PayloadFloatType) payloadBase.getValue();

					value = payloadFloat.getValue();

					Map<String, Map<String, ActiveSignal>> perEndDevideAsset = activeSignals
							.get(vtnConfiguration.getSessionKey());
					if (perEndDevideAsset == null) {
						perEndDevideAsset = new ConcurrentHashMap<>();
					}
					for (String endDeviceAsset : collect) {
						Map<String, ActiveSignal> listActiveSignal = perEndDevideAsset.get(endDeviceAsset);
						if (listActiveSignal == null) {
							listActiveSignal = new ConcurrentHashMap<>();
						}
						listActiveSignal.put(activeSignalUUID, new ActiveSignal(value, priority, eiEventSignalType));
						perEndDevideAsset.put(endDeviceAsset, listActiveSignal);
					}
					activeSignals.put(vtnConfiguration.getSessionKey(), perEndDevideAsset);

				}
			}
		}

	}

	public void onIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {
		String eventId = event.getEiEvent().getEventDescriptor().getEventID();
		String signalId = eiEventSignalType.getSignalID();
		String intervalId = intervalType.getUid().getText();
		String activeSignalUUID = String.format("%s-%s-%s", eventId, signalId, intervalId);

		List<String> collect;
		if (eiEventSignalType.getEiTarget() != null && eiEventSignalType.getEiTarget().getEndDeviceAsset() != null
				&& !eiEventSignalType.getEiTarget().getEndDeviceAsset().isEmpty()) {
			collect = eiEventSignalType.getEiTarget().getEndDeviceAsset().stream().map(EndDeviceAssetType::getMrid)
					.collect(Collectors.toList());
		} else {
			collect = new ArrayList<>();
			collect.add(DEFAULT_ENDDEVICE_ASSET);
		}
		Map<String, Map<String, ActiveSignal>> perEndDevideAsset = activeSignals.get(vtnConfiguration.getSessionKey());

		if (perEndDevideAsset != null) {
			for (String endDeviceAsset : collect) {
				Map<String, ActiveSignal> map = perEndDevideAsset.get(endDeviceAsset);
				if (map != null) {
					map.remove(activeSignalUUID);
				}
				perEndDevideAsset.put(endDeviceAsset, map);
			}
			activeSignals.put(vtnConfiguration.getSessionKey(), perEndDevideAsset);
		}
	}

	public void onBaselineIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			IntervalType intervalType) {
		String eventId = event.getEiEvent().getEventDescriptor().getEventID();
		String intervalId = intervalType.getUid().getText();
		String activeSignalUUID = String.format("%s-%s", eventId, intervalId);

		EiEventBaselineType baseline = event.getEiEvent().getEiEventSignals().getEiEventBaseline();

		for (JAXBElement<? extends StreamPayloadBaseType> payload : intervalType.getStreamPayloadBase()) {
			if (payload.getDeclaredType().equals(SignalPayloadType.class)) {

				SignalPayloadType reportPayload = (SignalPayloadType) payload.getValue();

				JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
				if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {

					PayloadFloatType payloadFloat = (PayloadFloatType) payloadBase.getValue();

					Float value = payloadFloat.getValue();

					Map<String, ActiveBaseline> map = baselineActiveSignal.get(vtnConfiguration.getSessionKey());
					if (map == null) {
						map = new ConcurrentHashMap<>();
					}
					ActiveBaseline activeBaseline = new ActiveBaseline(value, baseline);
					map.put(activeSignalUUID, activeBaseline);
					baselineActiveSignal.put(vtnConfiguration.getSessionKey(), map);
				}
			}
		}

	}

	public void onBaselineIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			IntervalType intervalType) {
		String eventId = event.getEiEvent().getEventDescriptor().getEventID();
		String intervalId = intervalType.getUid().getText();
		String activeSignalUUID = String.format("%s-%s", eventId, intervalId);
		Map<String, ActiveBaseline> map = baselineActiveSignal.get(vtnConfiguration.getSessionKey());
		if (map != null) {
			map.remove(activeSignalUUID);
			baselineActiveSignal.put(vtnConfiguration.getSessionKey(), map);
		}

	}

	protected class ActiveSignal implements Comparable<ActiveSignal> {
		private AtomicInteger value;
		private Long priority;
		private EiEventSignalType signal;

		public ActiveSignal(Float value, Long priority, EiEventSignalType signal) {
			this.value = new AtomicInteger(Float.floatToIntBits(value));
			this.priority = priority;
			this.signal = signal;
		}

		@Override
		public int compareTo(ActiveSignal o) {
			return this.priority.compareTo(o.priority);
		}

		public Float getValue() {
			return Float.intBitsToFloat(value.get());
		}

		public EiEventSignalType getSignal() {
			return signal;
		}
	}

	protected class ActiveBaseline {
		private EiEventBaselineType baseline;
		private AtomicInteger value;

		public ActiveBaseline(Float value, EiEventBaselineType baseline) {
			this.value = new AtomicInteger(Float.floatToIntBits(value));
			this.baseline = baseline;
		}

		public Float getValue() {
			return Float.intBitsToFloat(value.get());
		}

		public EiEventBaselineType getBaseline() {
			return baseline;
		}
	}

}
