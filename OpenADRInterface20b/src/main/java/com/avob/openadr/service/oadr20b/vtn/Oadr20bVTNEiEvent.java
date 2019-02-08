package com.avob.openadr.service.oadr20b.vtn;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

/**
 * The OpenADR 2.0 B profile uses EiEvent service to exchange demand-response
 * events between VTN and VEN.
 * 
 * @author bertrand
 *
 */
public interface Oadr20bVTNEiEvent {

    /**
     * from VEN to VTN
     * 
     * oadrCreatedEvent is called according to the same rules as the pull
     * scenario after receiving an oadrDistributeEvent from the VTN
     * 
     * @param event
     * @return
     */
    public OadrResponseType oadrCreatedEvent(OadrCreatedEventType event) throws Oadr20bException;

    /**
     * from VEN to VTN
     * 
     * Used instead of oadrPoll for one-time event request
     * 
     * @param payload
     * @return
     */
    public OadrDistributeEventType oadrRequestEvent(OadrRequestEventType payload) throws Oadr20bException;

}
