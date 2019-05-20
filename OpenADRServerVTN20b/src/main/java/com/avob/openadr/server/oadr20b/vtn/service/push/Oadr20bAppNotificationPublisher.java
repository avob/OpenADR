package com.avob.openadr.server.oadr20b.vtn.service.push;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.service.push.OadrAppNotificationPublisher;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.VenReportDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloatDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyTokenDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatusDto;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;

@Service
public class Oadr20bAppNotificationPublisher {

	private static final String REGISTER_REPORT_TOPIC = "registerReport";

	private static final String UPDATE_REPORT_TOPIC = "updateReport";

	@Resource
	private OadrAppNotificationPublisher oadrAppNotificationPublisher;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

	public void notifyRegisterReport(VenReportDto dto, String venId) {
		oadrAppNotificationPublisher.notify(dto, REGISTER_REPORT_TOPIC, venId);
	}

	public void notifyUpdateReportFloat(List<OtherReportDataFloatDto> dto, String venId) {
		oadrAppNotificationPublisher.notify(dto, UPDATE_REPORT_TOPIC + ".float", venId);
	}

	public void notifyUpdateReportResourceStatus(List<OtherReportDataPayloadResourceStatusDto> dto, String venId) {
		oadrAppNotificationPublisher.notify(dto, UPDATE_REPORT_TOPIC + ".resourcestatus", venId);
	}

	public void notifyUpdateReportKeyToken(List<OtherReportDataKeyTokenDto> dto, String venId) {
		oadrAppNotificationPublisher.notify(dto, UPDATE_REPORT_TOPIC + ".keytoken", venId);
	}
}
