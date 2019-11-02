package com.avob.openadr.server.oadr20b.vtn.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.push.VenCommandDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VenDistributeService {

	public static final String OADR20B_QUEUE = "queue.command.oadr20b";

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Autowired
	private JmsTemplate jmsTemplate;

	private ObjectMapper mapper = new ObjectMapper();

	private <T> void publish(Ven ven, String payload, Class<T> klass) throws JsonProcessingException {
		VenCommandDto<T> command = new VenCommandDto<T>(ven, payload, klass);
		this.send(mapper.writeValueAsString(command));

	}

	protected void send(String payload) {
		jmsTemplate.convertAndSend(OADR20B_QUEUE, payload);
	}

	public void distribute(Ven ven, Object payload) throws Oadr20bApplicationLayerException {
		try {
			String marshalRoot = jaxbContext.marshalRoot(payload);
			if (payload instanceof OadrDistributeEventType) {

				this.publish(ven, marshalRoot, OadrDistributeEventType.class);

			} else if (payload instanceof OadrCancelReportType) {

				this.publish(ven, marshalRoot, OadrCancelReportType.class);

			} else if (payload instanceof OadrCreateReportType) {

				this.publish(ven, marshalRoot, OadrCreateReportType.class);

			} else if (payload instanceof OadrRegisterReportType) {

				this.publish(ven, marshalRoot, OadrRegisterReportType.class);

			} else if (payload instanceof OadrUpdateReportType) {

				this.publish(ven, marshalRoot, OadrUpdateReportType.class);

			} else if (payload instanceof OadrCancelPartyRegistrationType) {

				this.publish(ven, marshalRoot, OadrCancelPartyRegistrationType.class);

			} else if (payload instanceof OadrRequestReregistrationType) {

				this.publish(ven, marshalRoot, OadrRequestReregistrationType.class);

			} else {
				throw new Oadr20bApplicationLayerException("Can't distribute an unknown payload type");
			}
		} catch (JsonProcessingException | Oadr20bMarshalException e) {
			throw new Oadr20bApplicationLayerException(e);
		}
	}

}
