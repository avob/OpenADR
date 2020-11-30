package com.avob.openadr.dummy.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;
import com.avob.openadr.server.oadr20b.ven.service.Oadr20bVENEiEventService;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService.BufferValue;
import com.avob.openadr.server.oadr20b.ven.service.UpdateReportOrchestratorService.UpdateReportOrchestratorListener;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveBaselineSignal;
import com.avob.openadr.server.oadr20b.ven.timeline.Timeline.ActiveSignal;

@Service
public class DummyVEN20bSimulator {

//	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVEN20bEiReportListener.class);

	protected static final Float DEFAULT_VALUE = -1F;

	protected static final String DEFAULT_ENDDEVICE_ASSET = "DEFAULT_ENDDEVICE_ASSET";

	@Resource
	private UpdateReportOrchestratorService updateReportOrchestratorService;

	@Resource
	private Oadr20bVENEiEventService oadr20bVENEiEventService;

	private List<Simulator> simulators = Arrays.asList(new ThermostatSimulator(), new SmartEnergyModuleSimulator());

	public interface Simulator {
		BufferValue readReportData(OadrReportType report, OadrReportDescriptionType description,
				List<ActiveSignal> activeSignal, List<ActiveBaselineSignal> activeBaseline);

		String endDeviceAssetCompatibility();
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
					res = simulator.readReportData(report, description,
							new ArrayList<>(oadr20bVENEiEventService.getActiveSignals(vtnConfig)),
							new ArrayList<>(oadr20bVENEiEventService.getActiveBaselineSignals(vtnConfig)));
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

	}

	public void onIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			EiEventSignalType eiEventSignalType, IntervalType intervalType) {

	}

	public void onBaselineIntervalStart(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			IntervalType intervalType) {

	}

	public void onBaselineIntervalEnd(VtnSessionConfiguration vtnConfiguration, OadrEvent event,
			IntervalType intervalType) {

	}

}
