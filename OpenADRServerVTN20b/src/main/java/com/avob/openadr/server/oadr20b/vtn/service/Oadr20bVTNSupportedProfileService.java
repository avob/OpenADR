package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles.OadrProfile;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;

/**
 * Define VTN Supported VEN profiles
 * 
 * @author bzanni
 *
 */
@Service
public class Oadr20bVTNSupportedProfileService {

	public static final OadrProfile profile20a = Oadr20bEiRegisterPartyBuilders
			.newOadr20bOadrProfileBuilder(SchemaVersionEnumeratedType.OADR_20A.value())
			.addTransport(OadrTransportType.SIMPLE_HTTP).build();

	public static final OadrProfile profile20b = Oadr20bEiRegisterPartyBuilders
			.newOadr20bOadrProfileBuilder(SchemaVersionEnumeratedType.OADR_20B.value())
			.addTransport(OadrTransportType.SIMPLE_HTTP).addTransport(OadrTransportType.XMPP).build();

	private List<OadrProfile> supportedProfiles;

	public Oadr20bVTNSupportedProfileService() {
		supportedProfiles = new ArrayList<>();
		supportedProfiles.add(profile20a);
		supportedProfiles.add(profile20b);
	}

	public List<OadrProfile> getSupportedProfiles() {
		return supportedProfiles;
	}

	public OadrProfile getProfileA() {
		return profile20a;
	}

	public OadrProfile getProfileB() {
		return profile20b;
	}
}
