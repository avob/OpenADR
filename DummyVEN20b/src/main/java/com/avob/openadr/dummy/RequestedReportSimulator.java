package com.avob.openadr.dummy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.power.EndDeviceAssetType;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService.UpdateReportOrchestratorListener;

@Service
public class RequestedReportSimulator {

	private static final String DEFAULT_ENDDEVICE_ASSET = "DEFAULT_ENDDEVICE_ASSET";

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiReportListener.class);

	private static final Float DEFAULT_VALUE = -1F;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private UpdateReportOrchestratorService updateReportOrchestratorService;

	private Map<String, Map<String, Map<String, OadrReportRequestType>>> requestedReport = new ConcurrentHashMap<>();
	private Map<String, Map<String, Map<String, ActiveSignal>>> activeSignals = new ConcurrentHashMap<>();

	public List<String> getPendingRequestReport() {
		return new ArrayList<>(requestedReport.keySet());
	}

	public Float getCurrentValue(VtnSessionConfiguration vtnConfiguration, String endDeviceAsset) {
		Float currentValue = DEFAULT_VALUE;
		Map<String, Map<String, ActiveSignal>> perSession = activeSignals.get(vtnConfiguration.getSessionKey());
		if (perSession != null) {
			Map<String, ActiveSignal> perEndDeviceAsset = perSession.get(endDeviceAsset);
			if (perEndDeviceAsset != null) {
				List<ActiveSignal> keySet = new ArrayList<>(perEndDeviceAsset.values());
				if (!keySet.isEmpty()) {
					Collections.sort(keySet);
					ActiveSignal activeSignal = keySet.get(0);
					currentValue = activeSignal.getValue();
				}

			}
		}
		return currentValue;
	}

	public void create(VtnSessionConfiguration vtnConfig, OadrCreateReportType oadrCreateReportType) {
		updateReportOrchestratorService.create(vtnConfig, oadrCreateReportType, new UpdateReportOrchestratorListener() {

			@Override
			public Float onMetricReportDataRead(OadrReportRequestType reportRequest,
					OadrReportDescriptionType description) {
				String endDeviceAsset = DEFAULT_ENDDEVICE_ASSET;
				if (!description.getReportSubject().getEndDeviceAsset().isEmpty()) {
					endDeviceAsset = description.getReportSubject().getEndDeviceAsset().get(0).getMrid();
				}
				return getCurrentValue(vtnConfig, endDeviceAsset);
			}

			@Override
			public OadrPayloadResourceStatusType onStatusReportDataRead(OadrReportRequestType reportRequest,
					OadrReportDescriptionType description) {
				// TODO Auto-generated method stub
				return null;
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
						listActiveSignal.put(activeSignalUUID, new ActiveSignal(value, priority));
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

		Map<String, Map<String, ActiveSignal>> perEndDevideAsset = activeSignals.get(vtnConfiguration.getSessionKey());

		if (perEndDevideAsset != null) {
			perEndDevideAsset.remove(activeSignalUUID);
		}
	}

	private class ActiveSignal implements Comparable<ActiveSignal> {
		private AtomicInteger value;
		private Long priority;

		public ActiveSignal(Float value, Long priority) {
			this.value = new AtomicInteger(Float.floatToIntBits(value));
			this.priority = priority;
		}

		@Override
		public int compareTo(ActiveSignal o) {
			return this.priority.compareTo(o.priority);
		}

		public Float getValue() {
			return Float.intBitsToFloat(value.get());
		}
	}

}
