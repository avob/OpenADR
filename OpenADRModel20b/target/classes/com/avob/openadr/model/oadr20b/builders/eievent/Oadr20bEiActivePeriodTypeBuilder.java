package com.avob.openadr.model.oadr20b.builders.eievent;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.xcal.Properties;

public class Oadr20bEiActivePeriodTypeBuilder {

    private EiActivePeriodType eiActivePeriodType;

    public Oadr20bEiActivePeriodTypeBuilder(long timestampStart, String eventXmlDuration, String toleranceXmlDuration,
            String notificationXmlDuration) {

        eiActivePeriodType = Oadr20bFactory.createEiActivePeriodType();

        Properties properties = Oadr20bFactory.createProperties(timestampStart, eventXmlDuration);
        properties.setTolerance(Oadr20bFactory.createPropertiesTolerance(toleranceXmlDuration));
        properties.setXEiNotification(Oadr20bFactory.createDurationPropType(notificationXmlDuration));

        eiActivePeriodType.setProperties(properties);

    }

    public Oadr20bEiActivePeriodTypeBuilder withRampUp(String rampUpXmlDuration) {
        eiActivePeriodType.getProperties().setXEiRampUp(Oadr20bFactory.createDurationPropType(rampUpXmlDuration));
        return this;
    }

    public Oadr20bEiActivePeriodTypeBuilder withRecovery(String recoveryXmlDuration) {
        eiActivePeriodType.getProperties().setXEiRecovery(Oadr20bFactory.createDurationPropType(recoveryXmlDuration));
        return this;
    }

    public Oadr20bEiActivePeriodTypeBuilder withComponent(Object component) {
        eiActivePeriodType.setComponents(component);
        return this;
    }

    public EiActivePeriodType build() {
        return eiActivePeriodType;
    }
}
