package com.avob.openadr.service.oadr20b.vtn;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;

/**
 * The OpenADR 2.0 B profile uses EiRegisterParty service to support in-band
 * registration of VENs with VTNs
 * 
 * @author bertrand
 *
 */
public interface Oadr20bVTNEiRegisterParty {

    /**
     * from VEN to VTN
     * 
     * Registration is always initiated by the VEN with the
     * oadrCreatePartyRegistration payload. This payload provides the
     * information on the profile and transport the VEN has decided to use for
     * communication with the VTN, in addition to other registration related
     * information.
     * 
     * the VTN returns a registrationID in its reponse payload, which is used
     * for subsequent operations pertainin to the registration instance
     * 
     * @param payload
     * @return
     */
    public OadrCreatedPartyRegistrationType oadrCreatePartyRegistration(OadrCreatePartyRegistrationType payload)
            throws Oadr20bException;

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
     * from VEN to VTN
     * 
     * @param payload
     * @return
     */
    public OadrCreatedPartyRegistrationType oadrQueryRegistrationType(OadrQueryRegistrationType payload)
            throws Oadr20bException;

}
