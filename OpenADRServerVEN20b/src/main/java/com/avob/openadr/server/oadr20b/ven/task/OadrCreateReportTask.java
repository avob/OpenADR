package com.avob.openadr.server.oadr20b.ven.task;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;

public class OadrCreateReportTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(OadrRegisterReportTask.class);

    private OadrCreateReportType payload;

    private OadrHttpVenClient20b client;

    public OadrCreateReportTask(OadrHttpVenClient20b client, OadrCreateReportType payload) {
        this.payload = payload;
        this.client = client;
    }

    @Override
    public void run() {

        try {
            OadrCreatedReportType response = client.oadrCreateReport(payload);

            String responseCode = response.getEiResponse().getResponseCode();

            if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
                LOGGER.error(
                        "Fail oadrCreateReport: " + responseCode + response.getEiResponse().getResponseDescription());
            } else {
                LOGGER.info("oadrCreateReport: " + responseCode);
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
