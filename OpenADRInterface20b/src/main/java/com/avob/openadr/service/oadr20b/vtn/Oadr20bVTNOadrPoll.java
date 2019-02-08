package com.avob.openadr.service.oadr20b.vtn;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;

public interface Oadr20bVTNOadrPoll {

    /**
     * PULL API
     * 
     * from VEN to VTN
     * 
     * oadrPoll is periodically sent from VEN to the VTN. The VTN may replay
     * with one of the following payload:
     * 
     * - OadrResponseType ------------------------------------------------------
     * - OadrDistributeEventType -----------------------------------------------
     * - OadrCreateReportType --------------------------------------------------
     * - OadrRegisterReportType ------------------------------------------------
     * - OadrCancelReportType --------------------------------------------------
     * - OadrUpdateReportType --------------------------------------------------
     * - OadrCancelPartyRegistrationType ---------------------------------------
     * - OadrRequestReregistrationType -----------------------------------------
     * 
     * @param event
     * @return
     */
    public Object oadrPoll(OadrPollType event) throws Oadr20bException;

}
