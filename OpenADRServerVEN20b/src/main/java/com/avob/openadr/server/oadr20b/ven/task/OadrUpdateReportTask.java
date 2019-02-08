package com.avob.openadr.server.oadr20b.ven.task;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;

public class OadrUpdateReportTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OadrRegisterReportTask.class);

    private OadrUpdateReportType payload;

    private OadrHttpVenClient20b client;

    public OadrUpdateReportTask(OadrHttpVenClient20b client, OadrUpdateReportType payload) {
        this.payload = payload;
        this.client = client;
    }

    @Override
    public void run() {

        try {
            OadrUpdatedReportType response = client.oadrUpdateReport(payload);

            String responseCode = response.getEiResponse().getResponseCode();

            if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
                LOGGER.error(
                        "Fail OadrUpdateReport: " + responseCode + response.getEiResponse().getResponseDescription());
            } else {
                LOGGER.info("OadrUpdateReport: " + responseCode);
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
