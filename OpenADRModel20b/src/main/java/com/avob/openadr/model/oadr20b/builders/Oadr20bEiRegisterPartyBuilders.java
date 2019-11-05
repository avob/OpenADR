package com.avob.openadr.model.oadr20b.builders;

import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCancelPartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCanceledPartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatePartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatedPartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bOadrProfileBuilder;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bQueryRegistrationBuilder;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bRequestReregistrationBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;

public class Oadr20bEiRegisterPartyBuilders {

	private Oadr20bEiRegisterPartyBuilders() {
	}

	public static Oadr20bCanceledPartyRegistrationBuilder newOadr20bCanceledPartyRegistrationBuilder(
			EiResponseType eiresponse, String registrationId, String venId) {
		return new Oadr20bCanceledPartyRegistrationBuilder(eiresponse, registrationId, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bCancelPartyRegistrationBuilder newOadr20bCancelPartyRegistrationBuilder(String requestId,
			String registrationId, String venId) {
		return new Oadr20bCancelPartyRegistrationBuilder(requestId, registrationId, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bCreatedPartyRegistrationBuilder newOadr20bCreatedPartyRegistrationBuilder(
			EiResponseType eiresponse, String venId, String vtnId) {
		return new Oadr20bCreatedPartyRegistrationBuilder(eiresponse, venId, vtnId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bCreatePartyRegistrationBuilder newOadr20bCreatePartyRegistrationBuilder(String requestId,
			String venId, String profilName) {
		return new Oadr20bCreatePartyRegistrationBuilder(requestId, venId, profilName)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bQueryRegistrationBuilder newOadr20bQueryRegistrationBuilder(String requestId) {
		return new Oadr20bQueryRegistrationBuilder(requestId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bRequestReregistrationBuilder newOadr20bRequestReregistrationBuilder(String venId) {
		return new Oadr20bRequestReregistrationBuilder(venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bOadrProfileBuilder newOadr20bOadrProfileBuilder(String profileName) {
		return new Oadr20bOadrProfileBuilder(profileName);
	}
}
