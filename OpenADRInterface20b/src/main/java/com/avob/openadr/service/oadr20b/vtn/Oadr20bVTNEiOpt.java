package com.avob.openadr.service.oadr20b.vtn;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;

/**
 * The OpenADR 2.0 B profile specified the EiOpt service to create and
 * communicate Opt-In and Opt-Out from the VEN to the VTN. These schedules
 * define temporary changes in the availability, and may be combined with longer
 * term availability schedules and the Market Context requirements to give a
 * complete picture of the willingness of the VEN to respond to EiEvents
 * received by the VEN
 * 
 * @author bertrand
 *
 */
public interface Oadr20bVTNEiOpt {

    /**
     * from VEN to VTN
     * 
     * oadrCreateOpt payload includes an optID, which can be used in subsequence
     * operations to reference this scheduled
     * 
     * @param payload
     * @return
     */
    public OadrCreatedOptType oadrCreateOpt(OadrCreateOptType payload) throws Oadr20bException;

    /**
     * from VEN to VTN
     * 
     * The VEN may at any time cancel a temporary availability schedule by usin
     * goadrCancelOpt with an optID referencing the schedule to be canceled
     * 
     * @param payload
     * @return
     */
    public OadrCanceledOptType oadrCancelOptType(OadrCancelOptType payload) throws Oadr20bException;

}
