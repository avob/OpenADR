package com.avob.openadr.model.oadr20a.builders;

import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aCreatedEventBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aCreatedEventEventResponseBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aDistributeEventBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aDistributeEventOadrEventBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiActivePeriodTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiEventSignalTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aEventDescriptorTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aIntervalTypeBuilder;
import com.avob.openadr.model.oadr20a.builders.eievent.Oadr20aRequestEventBuilder;
import com.avob.openadr.model.oadr20a.builders.response.Oadr20aEiResponseBuilder;
import com.avob.openadr.model.oadr20a.builders.response.Oadr20aResponseBuilder;
import com.avob.openadr.model.oadr20a.ei.EiResponse;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.OptTypeType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;

public class Oadr20aBuilders {

	private Oadr20aBuilders() {
	}

	public static Oadr20aCreatedEventBuilder newCreatedEventBuilder(String venId, String requestId, int responseCode) {
		return new Oadr20aCreatedEventBuilder(venId, requestId, responseCode);
	}

	public static Oadr20aRequestEventBuilder newOadrRequestEventBuilder(String venId, String requestId) {
		return new Oadr20aRequestEventBuilder(venId, requestId);
	}

	public static Oadr20aDistributeEventBuilder newOadr20aDistributeEventBuilder(String vtnId, String requestId) {
		return new Oadr20aDistributeEventBuilder(vtnId, requestId);
	}

	public static Oadr20aDistributeEventOadrEventBuilder newOadr20aDistributeEventOadrEventBuilder() {
		return new Oadr20aDistributeEventOadrEventBuilder();
	}

	public static Oadr20aEventDescriptorTypeBuilder newOadr20aEventDescriptorTypeBuilder(Long createdTimespamp,
			String eventId, long modificationNumber, String marketContext, EventStatusEnumeratedType status) {
		return new Oadr20aEventDescriptorTypeBuilder(createdTimespamp, eventId, modificationNumber, marketContext,
				status);
	}

	public static Oadr20aEiTargetTypeBuilder newOadr20aEiTargetTypeBuilder() {
		return new Oadr20aEiTargetTypeBuilder();
	}

	public static Oadr20aEiEventSignalTypeBuilder newOadr20aEiEventSignalTypeBuilder(String signalId, String signalName,
			SignalTypeEnumeratedType signalType, float currentValue) {
		return new Oadr20aEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue);
	}

	public static Oadr20aEiActivePeriodTypeBuilder newOadr20aEiActivePeriodTypeBuilder(long timestampStart,
			String eventXmlDuration, String toleranceXmlDuration, String notificationXmlDuration) {
		return new Oadr20aEiActivePeriodTypeBuilder(timestampStart, eventXmlDuration, toleranceXmlDuration,
				notificationXmlDuration);
	}

	public static Oadr20aResponseBuilder newOadr20aResponseBuilder(String requestId, int responseCode) {
		return new Oadr20aResponseBuilder(requestId, responseCode);
	}

	public static Oadr20aResponseBuilder newOadr20aResponseBuilder(EiResponse response) {
		return new Oadr20aResponseBuilder(response);
	}

	public static Oadr20aIntervalTypeBuilder newOadr20aIntervalTypeBuilder(String intervalId, String xmlDuration,
			float value) {
		return new Oadr20aIntervalTypeBuilder(intervalId, xmlDuration, value);
	}

	public static Oadr20aEiResponseBuilder newOadr20aEiResponseBuilder(String requestId, int responseCode) {
		return new Oadr20aEiResponseBuilder(requestId, responseCode);
	}

	public static Oadr20aCreatedEventEventResponseBuilder newOadr20aCreatedEventEventResponseBuilder(String eventId,
			long modificationNumber, String requestId, int responseCode, OptTypeType opt) {
		return new Oadr20aCreatedEventEventResponseBuilder(eventId, modificationNumber, requestId, responseCode, opt);
	}

}
