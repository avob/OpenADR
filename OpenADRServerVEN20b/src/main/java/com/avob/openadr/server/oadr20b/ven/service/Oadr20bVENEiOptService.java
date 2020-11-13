package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENEiOptService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiReportService.class);

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public void oadrCreatedOpt(VtnSessionConfiguration vtnConfig, OadrCreatedOptType oadrCreatedOpt) {

	}

	public void oadrCanceledOpt(VtnSessionConfiguration vtnConfig, OadrCanceledOptType oadrCanceledOpt) {

	}

	public void createOpt(VtnSessionConfiguration vtnConfig, OadrCreateOptType oadrCreateOptType) {

		try {
			multiVtnConfig.oadrCreateOpt(vtnConfig, oadrCreateOptType);
		} catch (XmppStringprepException | NotConnectedException | Oadr20bException | Oadr20bHttpLayerException
				| Oadr20bXMLSignatureException | Oadr20bXMLSignatureValidationException | Oadr20bMarshalException
				| InterruptedException e) {
			LOGGER.error("Can't create opt", e);
		}
	}

}
