package com.avob.openadr.service.oadr20b.common;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;

/**
 * The EiReport service supports the exchange of reports between VEN and VTN and
 * vice-versa.
 * 
 * @author bertrand
 *
 */
public interface Oadr20bEiReport {
    /**
     * Register reporting capabilities
     * 
     * from VTN/VEN to VEN/VTN
     * 
     * The source party sends its reporting capabilities to the target party.
     * The source party's reporting capabilities are specified using a special
     * well-known report profil called the METADATA report, which is exchanged
     * using the same schema as any other report
     * 
     * @param report
     * @return
     */
    public OadrRegisteredReportType oadrRegisterReport(OadrRegisterReportType report) throws Oadr20bException;

    /**
     * Acknowledge previous interaction
     * 
     * from VTN/VEN to VEN/VTN
     * 
     * If the target party request that the source party create any report as
     * part of a previous exchange, the source party responds with the
     * oadrCreatedReport payload
     * 
     * @param payload
     * @return
     */
    public OadrResponseType oadrCreatedReport(OadrCreatedReportType payload) throws Oadr20bException;

    /**
     * Request reports
     * 
     * from VTN/VEN to VEN/VTN
     * 
     * the source party requests a report from the target by using the
     * oadrCreateReport payload. that payload contains a set of
     * reportSpecifiedID's that correspond to report capabilities in the
     * METADATA report that was previously sent by the party as part of the
     * previously decribed oadrRegisterReport interaction
     * 
     * @param payload
     * @return
     */
    public OadrCreatedReportType oadrCreateReport(OadrCreateReportType payload) throws Oadr20bException;

    /**
     * Send reports
     * 
     * from VTN/VEN to VEN/VTN
     * 
     * This operation can be performed by the source party only after a previous
     * report request interaction is performed by the target party
     * 
     * @param payload
     * @return
     */
    public OadrUpdatedReportType oadrUpdateReport(OadrUpdateReportType payload) throws Oadr20bException;

    /**
     * Cancel reports
     * 
     * from VTN/VEN to VEN/VTN
     * 
     * the source party uses the oadrCancelReport payload with the appropriate
     * reportRequestIds that where specified by the source party in a previous
     * request report interaction. Upon receiving the oadrCancelReport payload
     * the target party stops generating and sending reports corresponding to
     * the reportRequestIDs
     * 
     * @param payload
     * @return
     */
    public OadrCanceledReportType oadrCancelReport(OadrCancelReportType payload) throws Oadr20bException;

}
