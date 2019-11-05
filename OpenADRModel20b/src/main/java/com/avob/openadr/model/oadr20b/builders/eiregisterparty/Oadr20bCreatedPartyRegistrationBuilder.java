package com.avob.openadr.model.oadr20b.builders.eiregisterparty;

import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType.OadrExtensions;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles.OadrProfile;
import com.avob.openadr.model.oadr20b.oadr.OadrServiceSpecificInfo;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;

public class Oadr20bCreatedPartyRegistrationBuilder {

	private OadrCreatedPartyRegistrationType oadrCreatedPartyRegistrationType;

	public Oadr20bCreatedPartyRegistrationBuilder(EiResponseType eiresponse, String venId, String vtnId) {
		oadrCreatedPartyRegistrationType = Oadr20bFactory.createOadrCreatedPartyRegistrationType(eiresponse, venId,
				vtnId);
	}

	public Oadr20bCreatedPartyRegistrationBuilder withResponseDescription(String description) {
		oadrCreatedPartyRegistrationType.getEiResponse().setResponseDescription(description);
		return this;
	}

	public Oadr20bCreatedPartyRegistrationBuilder withOadrServiceSpecificInfo(
			OadrServiceSpecificInfo oadrServiceSpecificInfo) {
		oadrCreatedPartyRegistrationType.setOadrServiceSpecificInfo(oadrServiceSpecificInfo);
		return this;
	}

	public Oadr20bCreatedPartyRegistrationBuilder withOadrRequestedOadrPollFreq(String duration) {
		DurationPropType value = Oadr20bFactory.createDurationPropType(duration);
		oadrCreatedPartyRegistrationType.setOadrRequestedOadrPollFreq(value);
		return this;
	}

	public Oadr20bCreatedPartyRegistrationBuilder addOadrProfile(OadrProfile oadrProfile) {
		oadrCreatedPartyRegistrationType.getOadrProfiles().getOadrProfile().add(oadrProfile);
		return this;
	}

	public Oadr20bCreatedPartyRegistrationBuilder addOadrProfile(List<OadrProfile> oadrProfile) {
		oadrCreatedPartyRegistrationType.getOadrProfiles().getOadrProfile().addAll(oadrProfile);
		return this;
	}

	public Oadr20bCreatedPartyRegistrationBuilder withOadrExtensions(OadrExtensions oadrExtensions) {
		oadrCreatedPartyRegistrationType.setOadrExtensions(oadrExtensions);
		return this;
	}

	public Oadr20bCreatedPartyRegistrationBuilder withRegistrationId(String registrationId) {
		oadrCreatedPartyRegistrationType.setRegistrationID(registrationId);
		return this;
	}

	public Oadr20bCreatedPartyRegistrationBuilder withSchemaVersion(String schemaVersion) {
		oadrCreatedPartyRegistrationType.setSchemaVersion(schemaVersion);
		return this;
	}

	public OadrCreatedPartyRegistrationType build() {
		return oadrCreatedPartyRegistrationType;
	}
}
