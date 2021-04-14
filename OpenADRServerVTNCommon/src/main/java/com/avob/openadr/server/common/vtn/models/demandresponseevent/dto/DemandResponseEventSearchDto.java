package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.filter.DemandResponseEventFilter;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.sort.DemandResponseEventSort;

public class DemandResponseEventSearchDto {

	private List<DemandResponseEventFilter> filters;
	private List<DemandResponseEventSort> sorts;
	public List<DemandResponseEventFilter> getFilters() {
		return filters;
	}
	public void setFilters(List<DemandResponseEventFilter> filters) {
		this.filters = filters;
	}
	public List<DemandResponseEventSort> getSorts() {
		return sorts;
	}
	public void setSorts(List<DemandResponseEventSort> sorts) {
		this.sorts = sorts;
	}
}
