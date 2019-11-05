package com.avob.openadr.server.oadr20b.vtn.service.ei;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

@Service
public class Oadr20bVTNOadrPollService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNOadrPollService.class);

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VenPollService venPollService;

	@Resource
	private VenService venService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private VtnConfig vtnConfig;

	private Object oadrPoll(Ven ven, OadrPollType event) {
		String venID = ven.getUsername();
		String requestID = "";
		if (!event.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, event.getVenID(), venID);
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
		}

		String responseStr = venPollService.retrievePollForVenUsername(venID);
		if (responseStr == null) {
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder("", HttpStatus.OK_200, venID).build();
		}

		try {
			return jaxbContext.unmarshal(responseStr);
		} catch (Oadr20bUnmarshalException e) {
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder("", HttpStatus.INTERNAL_SERVER_ERROR_500, venID)
					.build();
		}

	}

	public Object request(Ven ven, Object payload) {
		if (payload instanceof OadrPollType) {

			LOGGER.info(ven.getUsername() + " - OadrPoll");

			OadrPollType oadrPollType = (OadrPollType) payload;

			return this.oadrPoll(ven, oadrPollType);

		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, ven.getUsername())
				.withDescription("Unknown payload type for service: OadrPoll").build();
	}

}
