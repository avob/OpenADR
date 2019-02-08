package com.avob.openadr.model.oadr20b.dto;

public class VenOptDto {

    private String venID;

    private String marketContextName;

    private String venResourceName;

    private String eventID;

    private Long eventModificationNumber;

    private String optId;

    private Long start;

    private Long end;

    public String getVenID() {
        return venID;
    }

    public void setVenID(String venID) {
        this.venID = venID;
    }

    public String getMarketContextName() {
        return marketContextName;
    }

    public void setMarketContextName(String marketContextName) {
        this.marketContextName = marketContextName;
    }

    public String getVenResourceName() {
        return venResourceName;
    }

    public void setVenResourceName(String venResourceName) {
        this.venResourceName = venResourceName;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public Long getEventModificationNumber() {
        return eventModificationNumber;
    }

    public void setEventModificationNumber(Long eventModificationNumber) {
        this.eventModificationNumber = eventModificationNumber;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

}
