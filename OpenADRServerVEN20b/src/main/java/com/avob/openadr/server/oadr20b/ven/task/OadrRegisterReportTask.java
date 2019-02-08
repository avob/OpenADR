package com.avob.openadr.server.oadr20b.ven.task;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;

public class OadrRegisterReportTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OadrRegisterReportTask.class);

    private OadrRegisterReportType payload;

    private OadrHttpVenClient20b client;

    public OadrRegisterReportTask(OadrHttpVenClient20b client, OadrRegisterReportType payload) {
        this.payload = payload;
        this.client = client;
    }

    @Override
    public void run() {

        try {
            OadrRegisteredReportType response = client.oadrRegisterReport(payload);

            String responseCode = response.getEiResponse().getResponseCode();

            if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
                LOGGER.error(
                        "Fail oadrRegisterReport: " + responseCode + response.getEiResponse().getResponseDescription());
            } else {
                LOGGER.info("oadrRegisterReport: " + responseCode);
            }

        } catch (Oadr20bException e) {
            LOGGER.error("", e);
        } catch (Oadr20bHttpLayerException e) {

            if (e.getErrorCode() == HttpStatus.FORBIDDEN_403) {

            }

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
