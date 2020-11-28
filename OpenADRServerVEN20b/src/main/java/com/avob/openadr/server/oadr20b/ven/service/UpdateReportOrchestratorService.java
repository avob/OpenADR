package com.avob.openadr.server.oadr20b.ven.service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bUpdateReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bUpdateReportOadrReportBuilder;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class UpdateReportOrchestratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateReportOrchestratorService.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Resource
	public ScheduledExecutorService scheduledExecutorService;

	private Map<String, ReportRequestOrchestration> reportRequestOrchestration = new ConcurrentHashMap<>();

	public interface UpdateReportOrchestratorListener {
		BufferValue readReportData(OadrReportType report, OadrReportDescriptionType description);

	}

	public void create(VtnSessionConfiguration vtnConfig, OadrCreateReportType oadrCreateReportType,
			UpdateReportOrchestratorListener listener) {
		for (OadrReportRequestType oadrReportRequestType : oadrCreateReportType.getOadrReportRequest()) {
			String reportSpecifierID = oadrReportRequestType.getReportSpecifier().getReportSpecifierID();
			String oadrReportRequestID = oadrReportRequestType.getReportRequestID();

			if (!Oadr20bVENEiReportService.METADATA_REPORT_SPECIFIER_ID.equals(reportSpecifierID)) {

				String orchestrationUUID = getOrchestrationUUID(vtnConfig, oadrReportRequestID);
				ReportRequestOrchestration orchestration = reportRequestOrchestration.get(orchestrationUUID);
				if (orchestration != null) {

					orchestration.cancel(false);

				}

				orchestration = new ReportRequestOrchestration(vtnConfig, oadrReportRequestType, listener);
				orchestration.start();
			}
		}

	}

	public void cancel(VtnSessionConfiguration vtnConfig, OadrCancelReportType oadrCancelReportType) {
		oadrCancelReportType.getReportRequestID().forEach(reportRequestId -> {
			String orchestrationUUID = getOrchestrationUUID(vtnConfig, reportRequestId);
			ReportRequestOrchestration orchestration = reportRequestOrchestration.get(orchestrationUUID);
			if (orchestration != null) {
				orchestration.cancel(false);
				reportRequestOrchestration.remove(orchestrationUUID);
			}

		});

	}

	private class ReportBackTask implements Runnable {

		private OffsetDateTime start;
		private ReportRequestOrchestration orchestration;

		public ReportBackTask(OffsetDateTime start, ReportRequestOrchestration orchestration) {
			this.orchestration = orchestration;
			this.start = start;
		}

		@Override
		public void run() {

			String reportRequestId = orchestration.reportRequest.getReportRequestID();
			String reportSpecifierID = orchestration.reportRequest.getReportSpecifier().getReportSpecifierID();
			DurationPropType granularity = orchestration.reportRequest.getReportSpecifier().getGranularity();
			DurationPropType reportBackDuration = orchestration.reportRequest.getReportSpecifier()
					.getReportBackDuration();
			ScheduledFuture<?> scheduledFuture = orchestration.reportBackTask;

			if (scheduledFuture.isCancelled()) {
				LOGGER.info(String.format("Cancel report back: %s %s", orchestration.vtnConfig.getSessionId(),
						reportRequestId));
				return;
			}

			LOGGER.info(String.format("Report back %s %s", orchestration.vtnConfig.getSessionId(), reportRequestId));

			String reportId = "0";
			String requestId = "0";
			Long confidence = 1L;
			Float accuracy = 1F;

			ReportNameEnumeratedType reportName = null;
			long createdTimestamp = System.currentTimeMillis();
			Long startTimestamp = null;
			String duration = null;

			Oadr20bUpdateReportBuilder newOadr20bUpdateReportBuilder = Oadr20bEiReportBuilders
					.newOadr20bUpdateReportBuilder(requestId, orchestration.vtnConfig.getVenId());

			orchestration.simulateReadingBuffer.forEach((rid, ridMap) -> {

				Oadr20bUpdateReportOadrReportBuilder newOadr20bUpdateReportOadrReportBuilder = Oadr20bEiReportBuilders
						.newOadr20bUpdateReportOadrReportBuilder(reportId, reportSpecifierID, reportRequestId,
								reportName, createdTimestamp, startTimestamp, duration);

				int i = 0;
				for (Entry<Long, BufferValue> entry : ridMap.entrySet()) {
					String intervalId = String.valueOf(i++);
					IntervalType interval = entry.getValue().toInterval(intervalId, entry.getKey(),
							granularity.getDuration(), rid, confidence, accuracy);

					newOadr20bUpdateReportOadrReportBuilder.addInterval(interval);
				}

				newOadr20bUpdateReportBuilder.addReport(newOadr20bUpdateReportOadrReportBuilder.build());

			});

			orchestration.simulateReadingBuffer.clear();

			OadrUpdateReportType build = newOadr20bUpdateReportBuilder.build();

			oadr20bVENEiReportService.updateReport(orchestration.vtnConfig, build);

			Long reportBackDurationToMillisecond = Oadr20bFactory
					.xmlDurationToMillisecond(reportBackDuration.getDuration());
			OffsetDateTime plus = start.plus(java.time.Duration.ofMillis(reportBackDurationToMillisecond));

			ReportBackTask reportBackTask = new ReportBackTask(plus, orchestration);
			ScheduledFuture<?> scheduleReportBack = scheduledExecutorService.schedule(reportBackTask,
					reportBackDurationToMillisecond, TimeUnit.MILLISECONDS);

			orchestration.reportBackTask = scheduleReportBack;

		}

	}

	private class GranularityTask implements Runnable {

		private OffsetDateTime start;
		private ReportRequestOrchestration orchestration;
		private SpecifierPayloadType specifier;

		public GranularityTask(OffsetDateTime start, ReportRequestOrchestration orchestration,
				SpecifierPayloadType specifier) {
			this.start = start;
			this.orchestration = orchestration;
			this.specifier = specifier;
		}

		@Override
		public void run() {

			VtnSessionConfiguration multiConfig = orchestration.vtnConfig;

			String reportRequestId = orchestration.reportRequest.getReportRequestID();
			String reportSpecifierId = orchestration.reportRequest.getReportSpecifier().getReportSpecifierID();
			String rid = specifier.getRID();
			DurationPropType granularity = orchestration.reportRequest.getReportSpecifier().getGranularity();

			ScheduledFuture<?> scheduledFuture = orchestration.simulateReadingTask.get(rid);

			if (scheduledFuture.isCancelled()) {
				LOGGER.info(String.format("Cancel reading: %s %s %s %s", multiConfig.getVenName(), reportRequestId,
						reportSpecifierId, rid));
				return;
			}

			TreeMap<Long, BufferValue> ridMap = orchestration.simulateReadingBuffer.get(rid);
			if (ridMap == null) {
				ridMap = new TreeMap<>();
			}

			Optional<OadrReportType> report = orchestration.vtnConfig.getReport(reportSpecifierId);

			Optional<OadrReportDescriptionType> reportDescription = orchestration.vtnConfig
					.getReportDescription(reportSpecifierId, rid);

			if (report.isPresent() && reportDescription.isPresent()) {

				BufferValue value = orchestration.listener.readReportData(report.get(), reportDescription.get());

				ridMap.put(start.toInstant().toEpochMilli(), value);

				orchestration.simulateReadingBuffer.put(rid, ridMap);

				Long granularityToMillisecond = Oadr20bFactory.xmlDurationToMillisecond(granularity.getDuration());
				OffsetDateTime plus = start.plus(java.time.Duration.ofMillis(granularityToMillisecond));

				GranularityTask simulateRidReadingTask = new GranularityTask(plus, orchestration, specifier);
				ScheduledFuture<?> schedule = scheduledExecutorService.schedule(simulateRidReadingTask,
						granularityToMillisecond, TimeUnit.MILLISECONDS);

				orchestration.simulateReadingTask.put(rid, schedule);

				LOGGER.info(String.format("Reading: %s %s %s", reportSpecifierId, rid, value.toString()));

			} else {
				LOGGER.warn(String.format("Unknown report reportSpecifierID: %s rID: %s", reportSpecifierId, rid));
			}

		}

	}

	static public class BufferValue {

		private Float floatValue;
		private OadrPayloadResourceStatusType statusValue;

		private BufferValue(Float floatValue) {
			this.floatValue = floatValue;
		}

		private BufferValue(OadrPayloadResourceStatusType statusValue) {
			this.statusValue = statusValue;
		}

		public IntervalType toInterval(String intervalId, Long start, String duration, String rid, Long confidence,
				Float accuracy) {
			if (floatValue != null) {
				return Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, duration, rid,
						confidence, accuracy, floatValue).build();
			} else {
				return Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, duration, rid,
						confidence, accuracy, statusValue).build();
			}

		}

		public String toString() {
			if (floatValue != null) {
				return String.valueOf(floatValue);
			} else {
				return "<oadrPayloadResourceStatus>";
			}
		}

		public static BufferValue of(Float value) {
			return new BufferValue(value);
		}
		
		public static BufferValue of(OadrPayloadResourceStatusType value) {
			return new BufferValue(value);
		}

	}

	private class ReportRequestOrchestration {

		private ScheduledFuture<?> reportBackTask;
		private Map<String, ScheduledFuture<?>> simulateReadingTask = new ConcurrentHashMap<>();
		private Map<String, TreeMap<Long, BufferValue>> simulateReadingBuffer = new ConcurrentHashMap<>();
		private OadrReportRequestType reportRequest;
		private UpdateReportOrchestratorListener listener;
		private VtnSessionConfiguration vtnConfig;

		public ReportRequestOrchestration(VtnSessionConfiguration vtnConfig, OadrReportRequestType reportRequest,
				UpdateReportOrchestratorListener listener) {
			this.vtnConfig = vtnConfig;
			this.reportRequest = reportRequest;
			this.listener = listener;
		}

		public void start() {

			OffsetDateTime start = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusMinutes(1);

			DurationPropType reportBackDuration = reportRequest.getReportSpecifier().getReportBackDuration();
			DurationPropType granularity = reportRequest.getReportSpecifier().getGranularity();
			Long reportBackDurationToMillisecond = Oadr20bFactory
					.xmlDurationToMillisecond(reportBackDuration.getDuration());

			OffsetDateTime reportBackStart = start.plus(reportBackDurationToMillisecond, ChronoUnit.MILLIS);
			ReportBackTask reportBackTask = new ReportBackTask(reportBackStart, this);
			this.reportBackTask = scheduledExecutorService.schedule(reportBackTask, reportBackDurationToMillisecond,
					TimeUnit.MILLISECONDS);

			reportRequest.getReportSpecifier().getSpecifierPayload().forEach(specifier -> {

				String rid = specifier.getRID();

				Long granularityToMillisecond = Oadr20bFactory.xmlDurationToMillisecond(granularity.getDuration());

				GranularityTask simulateRidReadingTask = new GranularityTask(start, this, specifier);

				ScheduledFuture<?> task = simulateReadingTask.get(rid);
				if (task == null) {
					task = scheduledExecutorService.schedule(simulateRidReadingTask, granularityToMillisecond,
							TimeUnit.MILLISECONDS);
					simulateReadingTask.put(rid, task);
				}

			});
		}

		public void cancel(boolean interrupt) {

		}

	}

	public void addReportRequestOrchestration(VtnSessionConfiguration vtnConfig, OadrReportRequestType reportRequest,
			UpdateReportOrchestratorListener listener) {

	}

	public String getOrchestrationUUID(VtnSessionConfiguration vtnConfig, String reportRequestId) {

		return new StringBuilder().append(vtnConfig.getSessionKey()).append(reportRequestId).toString();

	}

}
