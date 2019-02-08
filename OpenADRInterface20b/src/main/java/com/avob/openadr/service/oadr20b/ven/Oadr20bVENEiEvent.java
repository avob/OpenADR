package com.avob.openadr.service.oadr20b.ven;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

/**
 * The OpenADR 2.0 B profile uses EiEvent service to exchange demand-response
 * events between VTN and VEN.
 * 
 * @author bertrand
 *
 */
public interface Oadr20bVENEiEvent {

    /**
     * PUSH API
     * 
     * from VTN to VEN
     * 
     * oadrDistributeEvent may be pushed from VTN to VEN when events are created
     * or modified
     * 
     * @param event
     */
    public OadrResponseType oadrDistributeEvent(OadrDistributeEventType event) throws Oadr20bException;

}
