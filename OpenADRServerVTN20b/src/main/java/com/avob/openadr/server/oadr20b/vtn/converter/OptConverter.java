package com.avob.openadr.server.oadr20b.vtn.converter;

import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;

public class OptConverter {
    private OptConverter() {
    }

    public static final OptTypeType convert(DemandResponseEventOptEnum opt) {
        if (opt == null) {
            return null;
        }
        switch (opt) {
        case OPT_IN:
            return OptTypeType.OPT_IN;
        case OPT_OUT:
            return OptTypeType.OPT_OUT;
        default:
            return null;
        }
    }

    public static final DemandResponseEventOptEnum convert(OptTypeType opt) {
        if (opt == null) {
            return null;
        }
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
