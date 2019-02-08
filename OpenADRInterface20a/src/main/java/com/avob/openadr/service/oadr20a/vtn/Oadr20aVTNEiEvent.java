package com.avob.openadr.service.oadr20a.vtn;

import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

/**
 * The OpenADR 2.0 A profile uses EiEvent service to exchange demand-response
 * events between VTN and VEN.
 * 
 * @author bertrand
 *
 */
public interface Oadr20aVTNEiEvent {

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
    public OadrResponse oadrCreatedEvent(OadrCreatedEvent event) throws Oadr20aException;

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
    public OadrDistributeEvent oadrRequestEvent(OadrRequestEvent event) throws Oadr20aException;

}
