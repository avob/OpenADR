package com.avob.openadr.server.oadr20b.ven.task;

import org.eclipse.jetty.http.HttpStatus;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;

public class OadrCreateReportTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(OadrCreatedEventTask.class);

	private OadrCreateReportType payload;

	private OadrHttpVenClient20b httpClient;

	private OadrXmppVenClient20b xmppClient;

	public OadrCreateReportTask(OadrHttpVenClient20b client, OadrCreateReportType payload) {
		this.payload = payload;
		this.httpClient = client;
	}

	public OadrCreateReportTask(OadrXmppVenClient20b client, OadrCreateReportType payload) {
		this.payload = payload;
		this.xmppClient = client;
	}

	@Override
	public void run() {

		try {

			if (httpClient != null) {
				OadrCreatedReportType oadrCreateReport = httpClient.oadrCreateReport(payload);

				String responseCode = oadrCreateReport.getEiResponse().getResponseCode();

				if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
					LOGGER.error("Fail oadrCreateReport: " + responseCode
							+ oadrCreateReport.getEiResponse().getResponseDescription());
				} else {
					LOGGER.info("oadrCreateReport: " + responseCode);
				}
			} else if (xmppClient != null) {
				xmppClient.oadrCreateReport(payload);
			}

		} catch (Oadr20bException e) {
			LOGGER.error("", e);
		} catch (Oadr20bHttpLayerException e) {
			LOGGER.error("", e);
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error("", e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("", e);
		} catch (XmppStringprepException e) {
			LOGGER.error("", e);
		} catch (NotConnectedException e) {
			LOGGER.error("", e);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("", e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
		}
	}
}
