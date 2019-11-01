package com.avob.openadr.server.common.vtn.models.demandresponseevent.filter;

import java.util.ArrayList;
import java.util.List;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;

public class DemandResponseEventFilter {
	private DemandResponseEventFilterType type;
	private String value;

	public DemandResponseEventFilter() {
	}

	public DemandResponseEventFilter(DemandResponseEventFilterType type, String value) {
		this.type = type;
		this.value = value;
	}

	public static class Builder {
		private List<DemandResponseEventFilter> list = new ArrayList<>();

		public Builder addVenId(String venId) {
			list.add(new DemandResponseEventFilter(DemandResponseEventFilterType.VEN, venId));
			return this;
		}

		public Builder addMarketContext(String marketContext) {
			list.add(new DemandResponseEventFilter(DemandResponseEventFilterType.MARKET_CONTEXT, marketContext));
			return this;
		}

		public Builder addState(DemandResponseEventStateEnum state) {
			list.add(new DemandResponseEventFilter(DemandResponseEventFilterType.EVENT_STATE, state.toString()));
			return this;
		}

		public Builder isSendable() {
			list.add(new DemandResponseEventFilter(DemandResponseEventFilterType.EVENT_SENDABLE, "SENDABLE"));
			return this;
		}

		public Builder isNotSendable() {
			list.add(new DemandResponseEventFilter(DemandResponseEventFilterType.EVENT_SENDABLE, "NOT_SENDABLE"));
			return this;
		}

		public Builder isPublished() {
			list.add(new DemandResponseEventFilter(DemandResponseEventFilterType.EVENT_PUBLISHED, "PUBLISHED"));
			return this;
		}

		public Builder isNotPublished() {
			list.add(new DemandResponseEventFilter(DemandResponseEventFilterType.EVENT_PUBLISHED, "NOT_PUBLISHED"));
			return this;
		}

		public List<DemandResponseEventFilter> build() {
			return list;
		}
	}

	public static Builder builder() {
		return new DemandResponseEventFilter.Builder();
	}

	public DemandResponseEventFilterType getType() {
		return type;
	}

	public void setType(DemandResponseEventFilterType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
