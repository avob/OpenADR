package com.avob.openadr.server.common.vtn.models.venresource;

public class VenResourceDto {

    private Long id;

    private String name;
    

    public VenResourceDto() {
    }

    public VenResourceDto(String name) {
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
