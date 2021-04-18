package com.avob.openadr.server.common.vtn.models.venresource;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

public class VenResourceWithChildrenCount {

	private Long id;

	private VenResourceType type;

	private Long venResourceId;

	private String venResourceLabel;

	private Ven ven;

	private VenResource parent;

	private String name;

	private Long countChildren;

	public VenResourceWithChildrenCount() {
	}

	public VenResourceWithChildrenCount(Long id, VenResourceType type, Long venResourceId, String venResourceLabel,
			Ven ven, VenResource parent, String name, Long countChildren) {
		super();
		this.id = id;
		this.type = type;
		this.venResourceId = venResourceId;
		this.venResourceLabel = venResourceLabel;
		this.ven = ven;
		this.parent = parent;
		this.name = name;
		this.setCountChildren(countChildren);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VenResourceType getType() {
		return type;
	}

	public void setType(VenResourceType type) {
		this.type = type;
	}

	public Long getVenResourceId() {
		return venResourceId;
	}

	public void setVenResourceId(Long venResourceId) {
		this.venResourceId = venResourceId;
	}

	public String getVenResourceLabel() {
		return venResourceLabel;
	}

	public void setVenResourceLabel(String venResourceLabel) {
		this.venResourceLabel = venResourceLabel;
	}

	public Ven getVen() {
		return ven;
	}

	public void setVen(Ven ven) {
		this.ven = ven;
	}

	public VenResource getParent() {
		return parent;
	}

	public void setParent(VenResource parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCountChildren() {
		return countChildren;
	}

	public void setCountChildren(Long countChildren) {
		this.countChildren = countChildren;
	}

}
