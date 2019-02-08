package com.avob.openadr.server.common.vtn.models.demandresponseevent;

public class DemandResponseEventDto {

    private Long id;

    private String eventId;

    private Long start;

    private String duration;

    private String notificationDuration;

    private String toleranceDuration;

    private String rampUpDuration;

    private String recoveryDuration;

    private Long createdTimestamp;

    private Long lastUpdateTimestamp;

    private String marketContext;

    private String comaSeparatedTargetedVenUsername;

    private DemandResponseEventSimpleValueEnum value;

    private DemandResponseEventStateEnum state;

    private Long modificationNumber;

    private Long priority;

    private boolean testEvent = false;

    private String event;

    public DemandResponseEventStateEnum getState() {
        return state;
    }

    public void setState(DemandResponseEventStateEnum state) {
        this.state = state;
    }

    public DemandResponseEventSimpleValueEnum getValue() {
        return value;
    }

    public void setValue(DemandResponseEventSimpleValueEnum value) {
        this.value = value;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMarketContext() {
        return marketContext;
    }

    public void setMarketContext(String marketContext) {
        this.marketContext = marketContext;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public Long getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Long modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    public String getComaSeparatedTargetedVenUsername() {
        return comaSeparatedTargetedVenUsername;
    }

    public void setComaSeparatedTargetedVenUsername(String comaSeparatedTargetedVenUsername) {
        this.comaSeparatedTargetedVenUsername = comaSeparatedTargetedVenUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getToleranceDuration() {
        return toleranceDuration;
    }

    public void setToleranceDuration(String toleranceDuration) {
        this.toleranceDuration = toleranceDuration;
    }

    public String getRampUpDuration() {
        return rampUpDuration;
    }

    public void setRampUpDuration(String rampUpDuration) {
        this.rampUpDuration = rampUpDuration;
    }

    public String getRecoveryDuration() {
        return recoveryDuration;
    }

    public void setRecoveryDuration(String recoveryDuration) {
        this.recoveryDuration = recoveryDuration;
    }

    public boolean isTestEvent() {
        return testEvent;
    }

    public void setTestEvent(boolean testEvent) {
        this.testEvent = testEvent;
    }

    public String getNotificationDuration() {
        return notificationDuration;
    }

    public void setNotificationDuration(String notificationDuration) {
        this.notificationDuration = notificationDuration;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
