package com.avob.openadr.server.common.vtn.models.venmarketcontext;

public class VenMarketContextDto {

	private Long id;

	private String name;

	private String description;

	private String color;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
