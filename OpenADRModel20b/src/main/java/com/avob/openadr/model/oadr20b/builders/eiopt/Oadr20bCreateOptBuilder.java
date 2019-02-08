package com.avob.openadr.model.oadr20b.builders.eiopt;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;

public class Oadr20bCreateOptBuilder {

    private OadrCreateOptType oadrCreateOptType;

    public Oadr20bCreateOptBuilder(String requestId, String venId, Long createdDatetime, String eventId,
            long modificationNumber, String optId, OptTypeType optType, OptReasonEnumeratedType optReason) {
        oadrCreateOptType = Oadr20bFactory.createOadrCreateOptType(requestId, venId, createdDatetime, eventId,
                modificationNumber, optId, optType, optReason);
        oadrCreateOptType.setEiTarget(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(venId).build());
    }

    public Oadr20bCreateOptBuilder(String requestId, String venId, Long createdDatetime,
            VavailabilityType vavailabilityType, String optId, OptTypeType optType, OptReasonEnumeratedType optReason) {
        oadrCreateOptType = Oadr20bFactory.createOadrCreateOptType(requestId, venId, createdDatetime, vavailabilityType,
                optId, optType, optReason);
        oadrCreateOptType.setEiTarget(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(venId).build());
    }

    public Oadr20bCreateOptBuilder withSchemaVersion(String schemaVersion) {
        oadrCreateOptType.setSchemaVersion(schemaVersion);
        return this;
    }

    public Oadr20bCreateOptBuilder addTargetedResource(String resourceName) {
        oadrCreateOptType.getEiTarget().getResourceID().add(resourceName);
        return this;
    }

    public Oadr20bCreateOptBuilder withMarketContext(String marketContext) {
        oadrCreateOptType.setMarketContext(marketContext);
        return this;
    }

    public Oadr20bCreateOptBuilder withOadrDeviceClass(EiTargetType oadrDeviceClass) {
        oadrCreateOptType.setOadrDeviceClass(oadrDeviceClass);
        return this;
    }

    public Oadr20bCreateOptBuilder withOptReason(OptReasonEnumeratedType optReason) {
        oadrCreateOptType.setOptReason(optReason.value());
        return this;
    }

    public OadrCreateOptType build() {
        return oadrCreateOptType;
    }
}
