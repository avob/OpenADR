package com.avob.openadr.dummy;

import java.util.Arrays;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.server.oadr20b.ven.VEN20bApplicationConfig;
import com.avob.openadr.server.oadr20b.ven.VenConfig;

@SpringBootApplication
@Import({ VEN20bApplicationConfig.class })
public class DummyVEN20bApplication {

	@Resource
	private VenConfig venConfig;

	@Bean
	public OadrRegisterReportType venRegisterReport() {

		String requestId = UUID.randomUUID().toString();
		String reportRequestId = UUID.randomUUID().toString();
		String reportSpecifiedId = "reportSpecifiedId";
		String duration = "PT15M";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		String rid = "rid";
		ReportEnumeratedType reportType = ReportEnumeratedType.PERCENT_USAGE;
		ReadingTypeEnumeratedType readingType = ReadingTypeEnumeratedType.DIRECT_READ;

		return Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId, venConfig.getVenId(), reportRequestId)
				.addOadrReport(Arrays.asList(Oadr20bEiReportBuilders
						.newOadr20bRegisterReportOadrReportBuilder(
								reportSpecifiedId, reportRequestId, reportName, System.currentTimeMillis())
						.withDuration(duration)
						.addReportDescription(Oadr20bEiReportBuilders
								.newOadr20bOadrReportDescriptionBuilder(rid, reportType, readingType).build())
						.build()))
				.build();

	}

	public static void main(String[] args) {
		SpringApplication.run(DummyVEN20bApplication.class, args);
	}

}
