package com.avob.openadr.service.oadr20b.ven;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

/**
 * The OpenADR 2.0 B profile uses EiRegisterParty service to support in-band
 * registration of VENs with VTNs
 * 
 * @author bertrand
 *
 */
public interface Oadr20bVENEiRegisterParty {

    /**
     * from VTN/VEN to VEN/VTN
     * 
     * The VTN or VEN may cancel an active registration using the
     * oadrCancelPartyRegistration payload, referencing the registrationID.
     * 
     * @param payload
     * @return
     */
    public OadrCanceledPartyRegistrationType oadrCancelPartyRegistrationType(OadrCancelPartyRegistrationType payload)
            throws Oadr20bException;

    /**
     * from VTN to VEN
     * 
     * If VTN's registration information changes, the VTN can request the VTN to
     * reregister using the oadrRequestRegistration payload. The response to
     * this request is an OadrResponse followed by an asynchronous
     * oadrCreatePartyRegistration request from the VEN
     * 
     * @param payload
     * @return
     */
    public OadrResponseType oadrRequestReregistrationType(OadrRequestReregistrationType payload)
            throws Oadr20bException;

}
