package com.avob.openadr.server.common.vtn.models.venrequestcount;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "venrequestcount")
public class VenRequestCount {

    @Id
    private String venId;

    private Long requestCount = 0L;

    public VenRequestCount() {
    }

    public VenRequestCount(String venId) {
        this.setVenId(venId);
    }

    public String getVenId() {
        return venId;
    }

    public void setVenId(String venId) {
        this.venId = venId;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }
}
