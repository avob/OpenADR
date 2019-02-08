package com.avob.openadr.server.common.vtn.models.venmarketcontext;

public class VenMarketContextDto {

    private Long id;

    private String name;

    public VenMarketContextDto() {
    }

    public VenMarketContextDto(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
