package com.avob.openadr.server.oadr20b.vtn.service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

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

//	@Resource
//	private VenPollService venPollService;
//
//	@Resource
//	private Oadr20bPushService oadr20bPushService;

	@Resource
	private VenCommandPublisher venCommandPublisher;
//
	private Oadr20bJAXBContext jaxbContext;

	public VenDistributeService() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	public void distribute(Ven ven, Object payload) throws Oadr20bMarshalException {
		try {
			String marshalRoot = jaxbContext.marshalRoot(payload);
			if (payload instanceof OadrDistributeEventType) {
				
				venCommandPublisher.publish(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature(), marshalRoot,
						OadrDistributeEventType.class);

			} else if (payload instanceof OadrCancelReportType) {


				venCommandPublisher.publish(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature(), marshalRoot,
						OadrCancelReportType.class);

			} else if (payload instanceof OadrCreateReportType) {

				venCommandPublisher.publish(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature(), marshalRoot,
						OadrCreateReportType.class);

			} else if (payload instanceof OadrRegisterReportType) {

				venCommandPublisher.publish(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature(), marshalRoot,
						OadrRegisterReportType.class);

			} else if (payload instanceof OadrUpdateReportType) {

				venCommandPublisher.publish(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature(), marshalRoot,
						OadrUpdateReportType.class);

			} else if (payload instanceof OadrCancelPartyRegistrationType) {

				venCommandPublisher.publish(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature(), marshalRoot,
						OadrCancelPartyRegistrationType.class);

			} else if (payload instanceof OadrRequestReregistrationType) {

				venCommandPublisher.publish(ven.getUsername(), ven.getPushUrl(), ven.getXmlSignature(), marshalRoot,
						OadrRequestReregistrationType.class);

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
