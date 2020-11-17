package com.avob.openadr.dummy;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.power.EndDeviceAssetType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;

@Configuration
public class DummyVEN20bApplicationConfig {

	@Bean
	public List<OadrReportType> venRegisterReport() {

//		String requestId = UUID.randomUUID().toString();
		String reportRequestId = UUID.randomUUID().toString();
		String reportSpecifiedId = "reportSpecifiedId";
		String rid = "real_energy";
		String endDeviseAssetMrid = "Smart_Energy_Module";

		EiTargetType datasourceEiTarget = null;
		EndDeviceAssetType endDeviceAsset = new EndDeviceAssetType();
		endDeviceAsset.setMrid(endDeviseAssetMrid);
		EiTargetType subjectEiTarget = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
				.addEndDeviceAsset(endDeviceAsset).build();

		String minPeriod = null;
		String maxPeriod = null;
		boolean onChange = false;

		return Arrays.asList(Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(reportSpecifiedId, reportRequestId,
						ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE, System.currentTimeMillis())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid, ReportEnumeratedType.USAGE,
								ReadingTypeEnumeratedType.X_NOT_APPLICABLE)
						.withDataSource(datasourceEiTarget).withSubject(subjectEiTarget)
						.withEnergyRealBase(SiScaleCodeType.KILO).withOadrSamplingRate(minPeriod, maxPeriod, onChange)
						.build())
				.build());

	}

}
