package com.avob.openadr.model.oadr20b.builders;

import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bCreatedEventBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bCreatedEventEventResponseBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bDistributeEventBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bDistributeEventOadrEventBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiActivePeriodTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiEventBaselineTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEiEventSignalTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bEventDescriptorTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eievent.Oadr20bRequestEventBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;

public class Oadr20bEiEventBuilders {

	private Oadr20bEiEventBuilders() {
	}

	public static Oadr20bCreatedEventBuilder newCreatedEventBuilder(EiResponseType eiresponse, String venId) {
		return new Oadr20bCreatedEventBuilder(eiresponse, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bDistributeEventBuilder newOadr20bDistributeEventBuilder(String vtnId, String requestId) {
		return new Oadr20bDistributeEventBuilder(vtnId, requestId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bRequestEventBuilder newOadrRequestEventBuilder(String venId, String requestId) {
		return new Oadr20bRequestEventBuilder(venId, requestId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bDistributeEventOadrEventBuilder newOadr20bDistributeEventOadrEventBuilder() {
		return new Oadr20bDistributeEventOadrEventBuilder();
	}

	public static Oadr20bEventDescriptorTypeBuilder newOadr20bEventDescriptorTypeBuilder(Long createdTimespamp,
			String eventId, Long modificationNumber, String marketContext, EventStatusEnumeratedType status) {
		return new Oadr20bEventDescriptorTypeBuilder(createdTimespamp, eventId, modificationNumber, marketContext,
				status);
	}

	public static Oadr20bEiEventSignalTypeBuilder newOadr20bEiEventSignalTypeBuilder(String signalId,
			SignalNameEnumeratedType signalName, SignalTypeEnumeratedType signalType, float currentValue) {
		return new Oadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue);
	}

	public static Oadr20bEiEventBaselineTypeBuilder newOadr20bEiEventBaselineTypeBuilder(String baselineId,
			String baselineName, long baselineStart, String baselineDuration) {
		return new Oadr20bEiEventBaselineTypeBuilder(baselineId, baselineName, baselineStart, baselineDuration);
	}

	public static Oadr20bEiActivePeriodTypeBuilder newOadr20bEiActivePeriodTypeBuilder(long timestampStart,
			String eventXmlDuration, String toleranceXmlDuration, String notificationXmlDuration) {
		return new Oadr20bEiActivePeriodTypeBuilder(timestampStart, eventXmlDuration, toleranceXmlDuration,
				notificationXmlDuration);
	}

	public static Oadr20bCreatedEventEventResponseBuilder newOadr20bCreatedEventEventResponseBuilder(String eventId,
			long modificationNumber, String requestId, int responseCode, OptTypeType opt) {
		return new Oadr20bCreatedEventEventResponseBuilder(eventId, modificationNumber, requestId, responseCode, opt);
	}

}
