package com.avob.openadr.server.oadr20b.vtn.models.venreport.data;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avob.openadr.model.oadr20b.avob.KeyTokenType;

@Entity
@Table(name = "otherreportdata_keytoken")
public class OtherReportDataKeyToken extends ReportData {

	@Convert(converter = OtherReportDataKeyTokenFieldConverter.class)
	private List<KeyTokenType> tokens;

	public List<KeyTokenType> getTokens() {
		return tokens;
	}

	public void setTokens(List<KeyTokenType> tokens) {
		this.tokens = tokens;
	}

}
