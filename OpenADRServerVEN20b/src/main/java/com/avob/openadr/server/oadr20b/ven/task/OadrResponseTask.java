package com.avob.openadr.server.oadr20b.ven.task;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

public class OadrResponseTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OadrResponseTask.class);

    private OadrResponseType payload;

    private OadrHttpVenClient20b client;

    public OadrResponseTask(OadrHttpVenClient20b client, OadrResponseType payload) {
        this.payload = payload;
    }

    @Override
    public void run() {

        try {
            OadrResponseType response = client.oadrResponseReregisterParty(payload);

            String responseCode = response.getEiResponse().getResponseCode();

            if (HttpStatus.OK_200 != Integer.valueOf(responseCode)) {
                LOGGER.error(
                        "Fail oadrResponseReregisterParty: " + responseCode + response.getEiResponse().getResponseDescription());
            } else {
                LOGGER.info("oadrResponseReregisterParty: " + responseCode);
            }

        } catch (Oadr20bException e) {
            LOGGER.error("", e);
        } catch (Oadr20bHttpLayerException e) {
            LOGGER.error("", e);
        } catch (Oadr20bXMLSignatureException e) {
            LOGGER.error("", e);
        } catch (Oadr20bXMLSignatureValidationException e) {
            LOGGER.error("", e);
        }
    }
}