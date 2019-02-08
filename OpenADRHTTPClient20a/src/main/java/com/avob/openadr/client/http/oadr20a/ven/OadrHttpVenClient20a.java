package com.avob.openadr.client.http.oadr20a.ven;

import com.avob.openadr.client.http.oadr20a.OadrHttpClient20a;
import com.avob.openadr.model.oadr20a.Oadr20aUrlPath;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

/**
 * Oadr 2.0a VEN simple http client
 * 
 * use TLSv1, TLSv1.1, TLSv1.2 with a given PEM key
 * 
 * @author bertrand
 *
 */
public class OadrHttpVenClient20a {

    private OadrHttpClient20a client;

    public OadrHttpVenClient20a(OadrHttpClient20a client) {
        this.client = client;
    }

    /**
     * PUSH API
     * 
     * from VEN to VTN
     * 
     * oadrCreatedEvent is called according to the same rules as pull scenario
     * after reveiving an oadrDistributeEvent from the VTN
     * 
     * @param event
     * @return
     */
    public OadrResponse oadrCreatedEvent(OadrCreatedEvent event) throws Oadr20aException, Oadr20aHttpLayerException {
        return client.post(event, Oadr20aUrlPath.EI_EVENT_SERVICE, OadrResponse.class);
    }

    /**
     * PULL API
     * 
     * from VEN to VTN
     * 
     * oadrRequestEvent may be periodically called one or more times until the
     * VEN receives new or modified events
     * 
     * @param event
     * @return
     */
    public OadrDistributeEvent oadrRequestEvent(OadrRequestEvent event)
            throws Oadr20aException, Oadr20aHttpLayerException {
        return client.post(event, Oadr20aUrlPath.EI_EVENT_SERVICE, OadrDistributeEvent.class);
    }

}
