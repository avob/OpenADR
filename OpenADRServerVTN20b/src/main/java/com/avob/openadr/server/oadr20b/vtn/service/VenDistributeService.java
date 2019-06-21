package com.avob.openadr.server.oadr20b.vtn.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.push.VenCommandPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class VenDistributeService {

	@Resource
	private VenCommandPublisher venCommandPublisher;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	public void distribute(Ven ven, Object payload) throws Oadr20bMarshalException {
		try {
			String marshalRoot = jaxbContext.marshalRoot(payload);
			if (payload instanceof OadrDistributeEventType) {

				venCommandPublisher.publish(ven, marshalRoot, OadrDistributeEventType.class);

			} else if (payload instanceof OadrCancelReportType) {

				venCommandPublisher.publish(ven, marshalRoot, OadrCancelReportType.class);

			} else if (payload instanceof OadrCreateReportType) {

				venCommandPublisher.publish(ven, marshalRoot, OadrCreateReportType.class);

			} else if (payload instanceof OadrRegisterReportType) {

				venCommandPublisher.publish(ven, marshalRoot, OadrRegisterReportType.class);

			} else if (payload instanceof OadrUpdateReportType) {

				venCommandPublisher.publish(ven, marshalRoot, OadrUpdateReportType.class);

			} else if (payload instanceof OadrCancelPartyRegistrationType) {

				venCommandPublisher.publish(ven, marshalRoot, OadrCancelPartyRegistrationType.class);

			} else if (payload instanceof OadrRequestReregistrationType) {

				venCommandPublisher.publish(ven, marshalRoot, OadrRequestReregistrationType.class);

			} else {
				// TODO bzanni: exception cannot be pushed payload (outside
				// Oadr20b
				// protocol)
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
