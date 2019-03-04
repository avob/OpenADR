package com.avob.openadr.server.common.vtn.models.vengroup;

public class VenGroupDto {

    private Long id;

    private String name;
    
    private String description;

    public VenGroupDto() {
    }

    public VenGroupDto(String name) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
