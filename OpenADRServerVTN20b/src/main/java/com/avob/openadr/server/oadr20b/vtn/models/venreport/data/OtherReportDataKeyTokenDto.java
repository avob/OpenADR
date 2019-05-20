package com.avob.openadr.server.oadr20b.vtn.models.venreport.data;

import java.util.List;

import com.avob.openadr.model.oadr20b.avob.KeyTokenType;

public class OtherReportDataKeyTokenDto extends ReportDataDto {
	private List<KeyTokenType> tokens;

	public List<KeyTokenType> getTokens() {
		return tokens;
	}

	public void setTokens(List<KeyTokenType> tokens) {
		this.tokens = tokens;
	}

}
