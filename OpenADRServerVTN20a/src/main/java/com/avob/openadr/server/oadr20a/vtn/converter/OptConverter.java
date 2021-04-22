package com.avob.openadr.server.oadr20a.vtn.converter;

import com.avob.openadr.model.oadr20a.ei.OptTypeType;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventOptEnum;

public class OptConverter {

	private OptConverter() {
	}

	public static final DemandResponseEventOptEnum convert(OptTypeType opt) {
		switch (opt) {
		case OPT_IN:
			return DemandResponseEventOptEnum.OPT_IN;
		case OPT_OUT:
			return DemandResponseEventOptEnum.OPT_OUT;
		default:
			return null;
		}
	}

}
