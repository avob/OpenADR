package com.avob.openadr.server.common.vtn.models.ven;

public class VenUpdateDto {

    private String name;

    private Long pullFrequencySeconds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPullFrequencySeconds() {
        return pullFrequencySeconds;
    }

    public void setPullFrequencySeconds(Long pullFrequencySeconds) {
        this.pullFrequencySeconds = pullFrequencySeconds;
    }

}
