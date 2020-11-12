package com.avob.openadr.model.oadr20b.builders;

import com.avob.openadr.model.oadr20b.builders.response.Oadr20bEiResponseBuilder;
import com.avob.openadr.model.oadr20b.builders.response.Oadr20bResponseBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;

public class Oadr20bResponseBuilders {

	private Oadr20bResponseBuilders() {
	}

	public static Oadr20bResponseBuilder newOadr20bResponseBuilder(String requestId, int responseCode) {
		return new Oadr20bResponseBuilder(requestId, responseCode);
	}

	public static Oadr20bResponseBuilder newOadr20bResponseBuilder(String requestId, int responseCode, String venId) {
		return new Oadr20bResponseBuilder(requestId, responseCode, venId);
	}

	public static Oadr20bResponseBuilder newOadr20bResponseBuilder(EiResponseType response, String venId) {
		return new Oadr20bResponseBuilder(response, venId);
	}

	public static Oadr20bEiResponseBuilder newOadr20bEiResponseBuilder(String requestId, int responseCode) {
		return new Oadr20bEiResponseBuilder(requestId, responseCode);
	}

	public static EiResponseType newOadr20bEiResponseMismatchUsernameVenIdBuilder(String requestId, String username,
			String venId) {
		String description = "Mismatch between authentication username(" + username + ") and requested venId(" + venId
				+ ")";
		int errorCode = Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462;
		return Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, errorCode).withDescription(description)
				.build();
	}

	public static EiResponseType newOadr20bEiResponseMismatchUsernameVtnIdBuilder(String requestId, String username,
			String vtnId) {
		String description = "Mismatch between authentication username(" + username + ") and requested vtnId(" + vtnId
				+ ")";
		int errorCode = Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462;
		return Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, errorCode).withDescription(description)
				.build();
	}

	public static EiResponseType newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(String requestId,
			String venId) {
		String description = "Ven(" + venId + ") require xml signature but not signed request";
		int errorCode = Oadr20bApplicationLayerErrorCode.COMPLIANCE_ERROR_459;
		return Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, errorCode).withDescription(description)
				.build();
	}

	public static EiResponseType newOadr20bEiResponseInvalidRegistrationIdBuilder(String requestId, String venId) {
		String description = "Ven(" + venId + ") invalid registration ID";
		int errorCode = Oadr20bApplicationLayerErrorCode.INVALID_ID_452;
		return Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, errorCode).withDescription(description)
				.build();
	}

	public static EiResponseType newOadr20bEiResponseOK(String requestId) {
		int errorCode = 200;
		return Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, errorCode).build();
	}

}
