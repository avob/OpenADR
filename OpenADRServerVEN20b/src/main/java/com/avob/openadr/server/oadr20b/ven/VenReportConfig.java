package com.avob.openadr.server.oadr20b.ven;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.ReportSpecifierType;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrSamplingRateType;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bInvalidReportRequestException;

@Configuration
public class VenReportConfig {

	@Resource
	private VenConfig venConfig;

	private Map<String, OadrReportType> reports = new HashMap<>();

	@Autowired(required = false)
	private OadrRegisterReportType venRegisterReport;

	@PostConstruct
	public void init() {
		if (venRegisterReport != null) {
			venRegisterReport.getOadrReport().forEach(report -> {
				reports.put(report.getReportSpecifierID(), report);
			});
		}

	}

	public OadrRegisterReportType getVenRegisterReport() {
		if (venRegisterReport == null) {
			String requestId = UUID.randomUUID().toString();
			String reportRequestId = UUID.randomUUID().toString();
			return Oadr20bEiReportBuilders
					.newOadr20bRegisterReportBuilder(requestId, venConfig.getVenId(), reportRequestId).build();
		}
		return venRegisterReport;
	}

	public void checkReportSpecifier(String requestId, String reportRequestId, ReportSpecifierType reportSpecifier)
			throws Oadr20bInvalidReportRequestException {

		String reportSpecifierID = reportSpecifier.getReportSpecifierID();
		if (!reports.containsKey(reportSpecifierID)) {
			throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461,
							venConfig.getVenId())
					.withDescription(
							String.format("Invalid create report request: %s - report specifier %s not suported",
									reportRequestId, reportSpecifierID))
					.build());
		}

		OadrReportType report = reports.get(reportSpecifierID);
		Map<String, OadrReportDescriptionType> reportDescriptions = report.getOadrReportDescription().stream()
				.collect(Collectors.toMap(desc -> {
					return getReportDescriptionUID(desc);
				}, Function.identity()));

		DurationPropType granularity = reportSpecifier.getGranularity();
		DurationPropType reportBackDuration = reportSpecifier.getReportBackDuration();

		Long granularityMillis = Oadr20bFactory.xmlDurationToMillisecond(granularity.getDuration());
		Long reportBackDurationMillis = Oadr20bFactory.xmlDurationToMillisecond(reportBackDuration.getDuration());

		if (reportBackDurationMillis < granularityMillis) {
			throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.INVALID_DATA_454,
							venConfig.getVenId())
					.withDescription(String.format(
							"Invalid create report request: %s - Granularity duration must be less than report back duration",
							reportRequestId))
					.build());
		}

		List<SpecifierPayloadType> specifierPayload = reportSpecifier.getSpecifierPayload();
		for (SpecifierPayloadType specifier : specifierPayload) {
			String reportDescriptionUID = getReportDescriptionUID(specifier);

			if (!reportDescriptions.containsKey(reportDescriptionUID)) {
				throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
						.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461,
								venConfig.getVenId())
						.withDescription(
								String.format("Invalid create report request: %s - report specifier %s not suported",
										reportRequestId, reportSpecifierID))
						.build());
			}

			OadrReportDescriptionType oadrReportDescriptionType = reportDescriptions.get(reportDescriptionUID);
			OadrSamplingRateType oadrSamplingRate = oadrReportDescriptionType.getOadrSamplingRate();

			Long minSamplingRateMillis = null;
			Long maxSamplingRateMillis = null;
			boolean oadrOnChange = false;
			if (oadrSamplingRate != null) {
				if (oadrSamplingRate.getOadrMinPeriod() != null) {
					minSamplingRateMillis = Oadr20bFactory
							.xmlDurationToMillisecond(oadrSamplingRate.getOadrMaxPeriod());
					if (granularityMillis < minSamplingRateMillis) {
						throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
								.newOadr20bResponseBuilder(requestId,
										Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461, venConfig.getVenId())
								.withDescription(String.format(
										"Invalid create report request: %s - granularity must be greater than every report description oadrMinPeriod if defined",
										reportRequestId, reportSpecifierID))
								.build());
					}
				}
				if (oadrSamplingRate.getOadrMinPeriod() != null) {
					maxSamplingRateMillis = Oadr20bFactory
							.xmlDurationToMillisecond(oadrSamplingRate.getOadrMaxPeriod());
					if (reportBackDurationMillis > maxSamplingRateMillis) {
						throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
								.newOadr20bResponseBuilder(requestId,
										Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461, venConfig.getVenId())
								.withDescription(String.format(
										"Invalid create report request: %s - report back duration must be greater than every report description oadrMaxPeriod if defined",
										reportRequestId, reportSpecifierID))
								.build());
					}
				}
				oadrOnChange = oadrSamplingRate.isOadrOnChange();
			}

			if (!oadrOnChange && (granularityMillis == 0 || reportBackDurationMillis == 0)) {
				throw new Oadr20bInvalidReportRequestException(Oadr20bResponseBuilders
						.newOadr20bResponseBuilder(requestId, Oadr20bApplicationLayerErrorCode.REPORT_NOT_SUPPORTED_461,
								venConfig.getVenId())
						.withDescription(String.format(
								"Invalid create report request: %s - granularity and report back duration equals 0 while at least on report description do not support oadrOnChange",
								reportRequestId, reportSpecifierID))
						.build());
			}

		}
	}

	private String getReportDescriptionUID(SpecifierPayloadType description) {
		StringBuilder builder = new StringBuilder().append(description.getRID());
		if (description.getReadingType() != null) {
			builder.append(description.getReadingType());
		}
		if (description.getItemBase() != null) {
			builder.append(description.getItemBase().getValue());
		}
		return builder.toString();
	}

	private String getReportDescriptionUID(OadrReportDescriptionType description) {
		StringBuilder builder = new StringBuilder().append(description.getRID());
		if (description.getReadingType() != null) {
			builder.append(description.getReadingType());
		}
		if (description.getItemBase() != null) {
			builder.append(description.getItemBase().getValue());
		}
		return builder.toString();
	}

}
