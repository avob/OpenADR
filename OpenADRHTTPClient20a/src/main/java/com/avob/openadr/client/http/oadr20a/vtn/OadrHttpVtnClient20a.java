package com.avob.openadr.client.http.oadr20a.vtn;

import com.avob.openadr.client.http.oadr20a.OadrHttpClient20a;
import com.avob.openadr.model.oadr20a.Oadr20aUrlPath;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

/**
 * Oadr 2.0a VTN simple http client
 * 
 * use TLSv1, TLSv1.1, TLSv1.2 with a given PEM key
 * 
 * @author bertrand
 *
 */
public class OadrHttpVtnClient20a {

    private OadrHttpClient20a client;

    public OadrHttpVtnClient20a(OadrHttpClient20a client) {
        this.client = client;
    }

    /**
     * PUSH API
     * 
     * from VTN to VEN
     * 
     * OadrDistributeEvent may be pushed from VTN to VEN when events are created
     * or modified
     * 
     * @param event
     * @return
     * @throws Oadr20aException
     */
    public OadrResponse oadrDistributeEvent(OadrDistributeEvent event)
            throws Oadr20aException, Oadr20aHttpLayerException {
        return client.post(event, Oadr20aUrlPath.EI_EVENT_SERVICE, OadrResponse.class);
    }

    public OadrResponse oadrDistributeEvent(String url, OadrDistributeEvent event)
            throws Oadr20aException, Oadr20aHttpLayerException {
        return client.post(url, Oadr20aUrlPath.EI_EVENT_SERVICE, null, event, OadrResponse.class);
    }
}
