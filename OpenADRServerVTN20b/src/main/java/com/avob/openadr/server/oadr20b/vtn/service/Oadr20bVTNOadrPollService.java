package com.avob.openadr.server.oadr20b.vtn.service;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.poll.Oadr20bPollApplicationLayerException;

@Service
public class Oadr20bVTNOadrPollService {

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VenPollService venPollService;

	@Resource
	private VenService venService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	public String oadrPoll(OadrPollType event, boolean signed) throws Oadr20bPollApplicationLayerException,
			Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bMarshalException {
		String requestID = "";
		String venID = event.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bPollApplicationLayerException(xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build());
		}

		String responseStr = venPollService.retrievePollForVenUsername(venID);
		OadrResponseType response = null;
		if (responseStr == null) {
			response = Oadr20bResponseBuilders.newOadr20bResponseBuilder("", HttpStatus.OK_200, venID).build();
		}
		if (signed) {
			if (response != null) {
				return xmlSignatureService.sign(response);
			} else if (responseStr != null) {
				Object unmarshal = jaxbContext.unmarshal(responseStr);
				return xmlSignatureService.sign(unmarshal);
			}

		} else {
			if (response != null) {
				return jaxbContext.marshalRoot(response);
			} else if (responseStr != null) {
				return responseStr;
			}
		}
		return null;
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrPollType oadrPollType)
			throws Oadr20bPollApplicationLayerException {
		String venID = oadrPollType.getVenID();
		String requestID = "";
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bPollApplicationLayerException(mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build());
		}
	}

}
