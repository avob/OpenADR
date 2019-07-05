package com.avob.openadr.server.oadr20b.ven.task;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;

public class OadrRegisterReportTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(OadrRegisterReportTask.class);

	private OadrRegisterReportType payload;

	private OadrHttpVenClient20b httpClient;

	private OadrXmppVenClient20b xmppClient;

	public OadrRegisterReportTask(OadrHttpVenClient20b client, OadrRegisterReportType payload) {
		this.payload = payload;
		this.httpClient = client;
	}

	public OadrRegisterReportTask(OadrXmppVenClient20b client, OadrRegisterReportType payload) {
		this.payload = payload;
		this.xmppClient = client;
	}

	@Override
	public void run() {

		try {

			if (httpClient != null) {
				OadrRegisteredReportType response = httpClient.oadrRegisterReport(payload);

				String responseCode = response.getEiResponse().getResponseCode();

				if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
					LOGGER.error("Fail oadrRegisterReport: " + responseCode
							+ response.getEiResponse().getResponseDescription());
				} else {
					LOGGER.info("oadrRegisterReport: " + responseCode);
				}
			} else if (xmppClient != null) {
				xmppClient.oadrRegisterReport(payload);
			}

		} catch (Oadr20bException e) {
			LOGGER.error("", e);
		} catch (Oadr20bHttpLayerException e) {
			LOGGER.error("", e);
		} catch (Oadr20bXMLSignatureException e) {
			LOGGER.error("", e);
		} catch (Oadr20bXMLSignatureValidationException e) {
			LOGGER.error("", e);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

}
