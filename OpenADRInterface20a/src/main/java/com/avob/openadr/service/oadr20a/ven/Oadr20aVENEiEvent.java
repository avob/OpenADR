package com.avob.openadr.service.oadr20a.ven;

import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

/**
 * The OpenADR 2.0 A profile uses EiEvent service to exchange demand-response
 * events between VTN and VEN.
 * 
 * @author bertrand
 *
 */
public interface Oadr20aVENEiEvent {

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
    public OadrResponse oadrDistributeEvent(OadrDistributeEvent event) throws Oadr20aException;

}
