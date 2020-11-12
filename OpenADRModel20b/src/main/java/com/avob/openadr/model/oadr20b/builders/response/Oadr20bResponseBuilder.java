package com.avob.openadr.model.oadr20b.builders.response;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

public class Oadr20bResponseBuilder {

	private OadrResponseType response;

	public Oadr20bResponseBuilder(String requestId, int responseCode) {
		response = Oadr20bFactory.createOadrResponseType(requestId, responseCode);
		response.setSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}
	
	public Oadr20bResponseBuilder(String requestId, int responseCode, String venId) {
		response = Oadr20bFactory.createOadrResponseType(requestId, responseCode, venId);
		response.setSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public Oadr20bResponseBuilder(EiResponseType res, String venId) {
		response = Oadr20bFactory.createOadrResponseType(res.getRequestID(), Integer.valueOf(res.getResponseCode()),
				venId);
		response.setSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public Oadr20bResponseBuilder withDescription(String description) {
		response.getEiResponse().setResponseDescription(description);
		return this;
	}

	public OadrResponseType build() {
		return response;
	}
}
