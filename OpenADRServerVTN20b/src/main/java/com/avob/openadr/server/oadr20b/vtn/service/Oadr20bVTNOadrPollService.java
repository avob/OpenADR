package com.avob.openadr.server.oadr20b.vtn.service;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;

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

	private String marshall(Object payload, boolean signed) {
		try {
			if (signed) {
				return xmlSignatureService.sign(payload);

			} else {
				return jaxbContext.marshalRoot(payload);
			}
		} catch (Oadr20bXMLSignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Oadr20bMarshalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String oadrPoll(String venID, OadrPollType event, boolean signed) {
		String requestID = "";
		if (!event.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, event.getVenID(), venID).build();
			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
			return marshall(build, signed);
		}
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build();
			return marshall(build, signed);
		}

		String responseStr = venPollService.retrievePollForVenUsername(venID);
		if (responseStr == null) {
			OadrResponseType build = Oadr20bResponseBuilders.newOadr20bResponseBuilder("", HttpStatus.OK_200, venID)
					.build();
			return marshall(build, signed);
		}

		Object unmarshal;
		try {
			unmarshal = jaxbContext.unmarshal(responseStr);
			return marshall(unmarshal, signed);
		} catch (Oadr20bUnmarshalException e) {
			OadrResponseType build = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder("", HttpStatus.INTERNAL_SERVER_ERROR_500, venID).build();
			return marshall(build, signed);
		}

	}

	public String request(String username, String payload) {
		Object unmarshal;
		try {
			unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());
		} catch (Oadr20bUnmarshalException e) {
			Ven findOneByUsername = venService.findOneByUsername(username);
			boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
					? findOneByUsername.getXmlSignature()
					: false;
			OadrResponseType response = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, username)
					.withDescription("Can't unmarshall payload").build();
			return marshall(response, signed);
		}

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			try {
				xmlSignatureService.validate(payload, oadrPayload);
			} catch (Oadr20bXMLSignatureValidationException e) {
				Ven findOneByUsername = venService.findOneByUsername(username);
				boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
						? findOneByUsername.getXmlSignature()
						: false;
				OadrResponseType response = Oadr20bResponseBuilders
						.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, username)
						.withDescription("Can't validate payload xml signature").build();
				return marshall(response, signed);
			}

			if (oadrPayload.getOadrSignedObject().getOadrPoll() != null) {

				LOGGER.info(username + " - OadrPoll signed");

				return this.oadrPoll(username, oadrPayload.getOadrSignedObject().getOadrPoll(), true);
			}

			Ven findOneByUsername = venService.findOneByUsername(username);
			boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
					? findOneByUsername.getXmlSignature()
					: false;
			OadrResponseType response = Oadr20bResponseBuilders
					.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, username)
					.withDescription("Unknown payload type for service: OadrPoll").build();
			return marshall(response, signed);

		} else if (unmarshal instanceof OadrPollType) {

			LOGGER.info(username + " - OadrPoll");

			LOGGER.debug(payload);

			OadrPollType oadrPollType = (OadrPollType) unmarshal;

			return this.oadrPoll(username, oadrPollType, false);

		}

		Ven findOneByUsername = venService.findOneByUsername(username);
		boolean signed = (findOneByUsername != null && findOneByUsername.getXmlSignature() != null)
				? findOneByUsername.getXmlSignature()
				: false;
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, username)
				.withDescription("Unknown payload type for service: OadrPoll").build();
		return marshall(response, signed);
	}

}
