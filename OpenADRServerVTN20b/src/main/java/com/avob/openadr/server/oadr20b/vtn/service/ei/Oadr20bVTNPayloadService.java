package com.avob.openadr.server.oadr20b.vtn.service.ei;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

@Service
public class Oadr20bVTNPayloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNPayloadService.class);

	@Resource
	private VenService venService;

	@Resource
	private Oadr20bJAXBContext oadr20bJAXBContext;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

	@Resource
	private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

	@Resource
	private Oadr20bVTNOadrPollService oadr20bVTNOadrPollService;

	private class UnmarshalledPayload {
		private Object payload;
		private boolean signed;

		public UnmarshalledPayload(Object payload, boolean signed) {
			this.payload = payload;
			this.signed = signed;
		}

		public Object getPayload() {
			return payload;
		}

		public boolean isSigned() {
			return signed;
		}

	}

	private UnmarshalledPayload unmarshall(Ven ven, String payload)
			throws Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException, MarshallException {

		Object unmarshal = oadr20bJAXBContext.unmarshal(payload);
		Object unsignedPayload = null;
		boolean signed = false;
		if (unmarshal instanceof OadrPayload) {
			OadrPayload oadrPayload = (OadrPayload) unmarshal;
			xmlSignatureService.validate(payload, oadrPayload);
			unsignedPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(oadrPayload);
			signed = true;
		} else {
			unsignedPayload = unmarshal;
		}
		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			throw new MarshallException(expectedSignatureError(ven));
		}

		return new UnmarshalledPayload(unsignedPayload, signed);
	}

	private String marshall(Object payload, boolean signed) {
		try {
			if (signed) {
				return xmlSignatureService.sign(payload);

			} else {
				return oadr20bJAXBContext.marshalRoot(payload);
			}
		} catch (Oadr20bXMLSignatureException | Oadr20bMarshalException e) {
			LOGGER.error("Internal error happened when marhsalling VTN message", e);
			return null;
		}
	}
	
	private String expectedSignatureError(Ven ven) {
		boolean signed = (ven.getXmlSignature() != null) ? ven.getXmlSignature() : false;
		EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
				.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder("", ven.getUsername());
		OadrResponseType build = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, ven.getUsername()).build();
		return marshall(build, signed);
	}

	private String signatureError(Ven ven) {
		boolean signed = (ven.getXmlSignature() != null) ? ven.getXmlSignature() : false;
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.INVALID_DATA_454, ven.getUsername())
				.withDescription("Can't validate payload xml signature").build();
		return marshall(response, signed);
	}

	private String unmarshallError(Ven ven) {
		boolean signed = (ven.getXmlSignature() != null) ? ven.getXmlSignature() : false;
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, ven.getUsername())
				.withDescription("Can't unmarshall payload").build();
		return marshall(response, signed);
	}

	private String venNotfoundError(String username) {
		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, username)
				.withDescription("Can't found ven wth venID: " + username).build();
		return marshall(response, false);
	}

	private class MarshallException extends Exception {
		private static final long serialVersionUID = 6536341286113107628L;
		private String response = null;

		public MarshallException(String response) {
			this.response = response;
		}

		public String getResponse() {
			return response;
		}
	}

	private UnmarshalledPayload marshall(Ven ven, String payload) throws MarshallException {
		try {
			return unmarshall(ven, payload);
		} catch (Oadr20bUnmarshalException e) {
			throw new MarshallException(unmarshallError(ven));
		} catch (Oadr20bXMLSignatureValidationException e) {
			throw new MarshallException(signatureError(ven));
		}
	}

	public String event(String username, String payload) {
		Ven ven = venService.findOneByUsername(username);
		if (ven == null) {
			return venNotfoundError(username);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(ven, payload);
			Object request = oadr20bVTNEiEventService.request(ven, unsignedPayload.getPayload());
			return marshall(request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}

	}

	public String registerParty(String username, String payload) {
		Ven ven = venService.findOneByUsername(username);
		if (ven == null) {
			return venNotfoundError(username);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(ven, payload);
			Object request = oadr20bVTNEiRegisterPartyService.request(ven, unsignedPayload.getPayload());
			return marshall(request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}

	public String opt(String username, String payload) {
		Ven ven = venService.findOneByUsername(username);
		if (ven == null) {
			return venNotfoundError(username);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(ven, payload);
			Object request = oadr20bVTNEiOptService.request(ven, unsignedPayload.getPayload());
			return marshall(request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}

	public String report(String username, String payload) {
		Ven ven = venService.findOneByUsername(username);
		if (ven == null) {
			return venNotfoundError(username);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(ven, payload);
			Object request = oadr20bVTNEiReportService.request(ven, unsignedPayload.getPayload());
			return marshall(request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}

	public String poll(String username, String payload) {
		Ven ven = venService.findOneByUsername(username);
		if (ven == null) {
			return venNotfoundError(username);
		}
		try {
			UnmarshalledPayload unsignedPayload = marshall(ven, payload);
			Object request = oadr20bVTNOadrPollService.request(ven, unsignedPayload.getPayload());
			return marshall(request, unsignedPayload.isSigned());
		} catch (MarshallException e) {
			return e.getResponse();
		}
	}
	
}
