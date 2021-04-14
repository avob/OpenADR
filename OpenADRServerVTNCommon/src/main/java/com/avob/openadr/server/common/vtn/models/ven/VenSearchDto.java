package com.avob.openadr.server.common.vtn.models.ven;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilter;
import com.avob.openadr.server.common.vtn.models.ven.sort.VenSort;

public class VenSearchDto {
	
	
	private List<VenFilter> filters;
	private List<VenSort> sorts;
	public List<VenFilter> getFilters() {
		return filters;
	}
	public void setFilters(List<VenFilter> filters) {
		this.filters = filters;
	}
	public List<VenSort> getSorts() {
		return sorts;
	}
	public void setSorts(List<VenSort> sorts) {
		this.sorts = sorts;
	}

}
