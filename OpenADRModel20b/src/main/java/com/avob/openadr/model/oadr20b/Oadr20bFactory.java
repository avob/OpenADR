package com.avob.openadr.model.oadr20b;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import com.avob.openadr.model.oadr20b.atom.FeedType;
import com.avob.openadr.model.oadr20b.avob.KeyTokenType;
import com.avob.openadr.model.oadr20b.avob.PayloadAvobVenServiceRequestType;
import com.avob.openadr.model.oadr20b.avob.PayloadKeyTokenType;
import com.avob.openadr.model.oadr20b.builders.eireport.PowerRealUnitType;
import com.avob.openadr.model.oadr20b.ei.CurrentValueType;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventBaselineType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalsType;
import com.avob.openadr.model.oadr20b.ei.EiEventType;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType.EiMarketContext;
import com.avob.openadr.model.oadr20b.ei.EventResponses;
import com.avob.openadr.model.oadr20b.ei.EventResponses.EventResponse;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.QualifiedEventIDType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportSpecifierType;
import com.avob.openadr.model.oadr20b.ei.SignalPayloadType;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.emix.ServiceAreaType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bInitializationException;
import com.avob.openadr.model.oadr20b.gml.FeatureCollection;
import com.avob.openadr.model.oadr20b.gml.FeatureCollection.Location;
import com.avob.openadr.model.oadr20b.gml.FeatureCollection.Location.Polygon;
import com.avob.openadr.model.oadr20b.gml.FeatureCollection.Location.Polygon.Exterior;
import com.avob.openadr.model.oadr20b.gml.FeatureCollection.Location.Polygon.Exterior.LinearRing;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.BaseUnitType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyType;
import com.avob.openadr.model.oadr20b.oadr.CurrentType;
import com.avob.openadr.model.oadr20b.oadr.FrequencyType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrGBItemBase;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrPendingReportsType;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles;
import com.avob.openadr.model.oadr20b.oadr.OadrProfiles.OadrProfile;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrSamplingRateType;
import com.avob.openadr.model.oadr20b.oadr.OadrSignedObject;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransports;
import com.avob.openadr.model.oadr20b.oadr.OadrTransports.OadrTransport;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.oadr.PulseCountType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.oadr.ThermType;
import com.avob.openadr.model.oadr20b.power.AggregatedPnodeType;
import com.avob.openadr.model.oadr20b.power.EndDeviceAssetType;
import com.avob.openadr.model.oadr20b.power.EnergyApparentType;
import com.avob.openadr.model.oadr20b.power.EnergyReactiveType;
import com.avob.openadr.model.oadr20b.power.EnergyRealType;
import com.avob.openadr.model.oadr20b.power.MeterAssetType;
import com.avob.openadr.model.oadr20b.power.PnodeType;
import com.avob.openadr.model.oadr20b.power.PowerApparentType;
import com.avob.openadr.model.oadr20b.power.PowerAttributesType;
import com.avob.openadr.model.oadr20b.power.PowerReactiveType;
import com.avob.openadr.model.oadr20b.power.PowerRealType;
import com.avob.openadr.model.oadr20b.power.ServiceDeliveryPointType;
import com.avob.openadr.model.oadr20b.power.ServiceLocationType;
import com.avob.openadr.model.oadr20b.power.TransportInterfaceType;
import com.avob.openadr.model.oadr20b.power.VoltageType;
import com.avob.openadr.model.oadr20b.pyld.EiCreatedEvent;
import com.avob.openadr.model.oadr20b.pyld.EiRequestEvent;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.model.oadr20b.strm.Intervals;
import com.avob.openadr.model.oadr20b.xcal.ArrayOfVavailabilityContainedComponents;
import com.avob.openadr.model.oadr20b.xcal.AvailableType;
import com.avob.openadr.model.oadr20b.xcal.Dtstart;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.model.oadr20b.xcal.Properties;
import com.avob.openadr.model.oadr20b.xcal.Properties.Tolerance;
import com.avob.openadr.model.oadr20b.xcal.Properties.Tolerance.Tolerate;
import com.avob.openadr.model.oadr20b.xcal.Uid;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.model.oadr20b.xmldsig.properties.NonceValueType;
import com.avob.openadr.model.oadr20b.xmldsig.properties.ReplayProtectType;

public class Oadr20bFactory {

	private static final String UNLIMITED_DURATION_TAG = "0";

	private static DatatypeFactory datatypeFactory;
	static {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new Oadr20bInitializationException(e);
		}

	}
	private static final com.avob.openadr.model.oadr20b.oadr.ObjectFactory factory = new com.avob.openadr.model.oadr20b.oadr.ObjectFactory();

	private static final com.avob.openadr.model.oadr20b.ei.ObjectFactory eiFactory = new com.avob.openadr.model.oadr20b.ei.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.strm.ObjectFactory strmFactory = new com.avob.openadr.model.oadr20b.strm.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.xcal.ObjectFactory xcalFactory = new com.avob.openadr.model.oadr20b.xcal.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.emix.ObjectFactory emixFactory = new com.avob.openadr.model.oadr20b.emix.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.gml.ObjectFactory gmlFactory = new com.avob.openadr.model.oadr20b.gml.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.power.ObjectFactory powerFactory = new com.avob.openadr.model.oadr20b.power.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.pyld.ObjectFactory pyldFactory = new com.avob.openadr.model.oadr20b.pyld.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.xmldsig.properties.ObjectFactory xmldsigPropertiesFactory = new com.avob.openadr.model.oadr20b.xmldsig.properties.ObjectFactory();
	private static final com.avob.openadr.model.oadr20b.avob.ObjectFactory avobFactory = new com.avob.openadr.model.oadr20b.avob.ObjectFactory();

	private Oadr20bFactory() {

	}

	public static XMLGregorianCalendar timestamptoXMLCalendar(long timestamp) {
		TimeZone utc = TimeZone.getTimeZone("UTC");
		GregorianCalendar cal = new GregorianCalendar(utc);
		cal.setTimeZone(utc);
		cal.setTimeInMillis(timestamp);
		XMLGregorianCalendar newXMLGregorianCalendar = datatypeFactory
				.newXMLGregorianCalendar(cal.getTime().toInstant().toString());
		newXMLGregorianCalendar.setTimezone(0);
		return newXMLGregorianCalendar;
	}

	public static Long xmlCalendarToTimestamp(XMLGregorianCalendar calendar) {
		GregorianCalendar gregorianCalendar = calendar.toGregorianCalendar();
		gregorianCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		return gregorianCalendar.getTimeInMillis();
	}

	/**
	 * Can return null to signify no end expected
	 * 
	 * @param timestamp
	 * @param duration
	 * @return
	 */
	public static Long addXMLDurationToTimestamp(Long timestamp, String duration) {
		if (UNLIMITED_DURATION_TAG.equals(duration)) {
			return null;
		}
		Duration newDuration = datatypeFactory.newDuration(duration);
		Date date = new Date();
		date.setTime(timestamp);
		newDuration.addTo(date);
		return date.getTime();
	}

	public static Long xmlDurationToMillisecond(String duration) {
		Duration newDuration = datatypeFactory.newDuration(duration);
		return newDuration.getTimeInMillis(Calendar.getInstance());
	}

	public static String millisecondToXmlDuration(Long millisecond) {
		Duration newDuration = datatypeFactory.newDuration(millisecond);
		if (millisecond.equals(0L)) {
			return "PT0S";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("P");

		if (newDuration.getDays() > 0) {
			builder.append(newDuration.getDays());
			builder.append("D");
		}
		if (newDuration.getHours() > 0 || newDuration.getMinutes() > 0 || newDuration.getSeconds() > 0) {
			builder.append("T");
			if (newDuration.getHours() > 0) {
				builder.append(newDuration.getHours());
				builder.append("H");
			}
			if (newDuration.getMinutes() > 0) {
				builder.append(newDuration.getMinutes());
				builder.append("M");
			}

			if (newDuration.getSeconds() > 0) {
				builder.append(newDuration.getSeconds());
				builder.append("S");
			}
		}

		return builder.toString();
	}

	public static String multiplyXmlDuration(String duration, int multiplier) {
		Duration newDuration = datatypeFactory.newDuration(duration);
		Duration multiply = newDuration.multiply(multiplier);
		return multiply.toString();
	}

	public static OadrProfiles createOadrProfiles() {
		return factory.createOadrProfiles();
	}

	public static OadrTransports createOadrTransports() {
		return factory.createOadrTransports();
	}

	public static OadrCreatedPartyRegistrationType createOadrCreatedPartyRegistrationType(EiResponseType eiresponse,
			String venId, String vtnId) {
		OadrCreatedPartyRegistrationType createOadrCreatedPartyRegistrationType = factory
				.createOadrCreatedPartyRegistrationType();
		createOadrCreatedPartyRegistrationType.setEiResponse(eiresponse);
		createOadrCreatedPartyRegistrationType.setVenID(venId);
		createOadrCreatedPartyRegistrationType.setVtnID(vtnId);
		OadrProfiles createOadrProfiles = Oadr20bFactory.createOadrProfiles();
		createOadrCreatedPartyRegistrationType.setOadrProfiles(createOadrProfiles);
		return createOadrCreatedPartyRegistrationType;
	}

	public static OadrDistributeEventType createOadrDistributeEventType(String vtnId, String requestId) {
		OadrDistributeEventType createOadrDistributeEventType = factory.createOadrDistributeEventType();
		createOadrDistributeEventType.setVtnID(vtnId);
		createOadrDistributeEventType.setRequestID(requestId);
		return createOadrDistributeEventType;
	}

	public static OadrPayload createOadrPayload(String id, Object value) {
		OadrSignedObject createOadrSignedObject = factory.createOadrSignedObject();
		createOadrSignedObject.setId(id);

		if (value instanceof OadrDistributeEventType) {
			createOadrSignedObject.setOadrDistributeEvent((OadrDistributeEventType) value);
		} else if (value instanceof OadrCreatedEventType) {
			createOadrSignedObject.setOadrCreatedEvent((OadrCreatedEventType) value);
		} else if (value instanceof OadrRequestEventType) {
			createOadrSignedObject.setOadrRequestEvent((OadrRequestEventType) value);
		} else if (value instanceof OadrResponseType) {
			createOadrSignedObject.setOadrResponse((OadrResponseType) value);
		} else if (value instanceof OadrCancelOptType) {
			createOadrSignedObject.setOadrCancelOpt((OadrCancelOptType) value);
		} else if (value instanceof OadrCanceledOptType) {
			createOadrSignedObject.setOadrCanceledOpt((OadrCanceledOptType) value);
		} else if (value instanceof OadrCreateOptType) {
			createOadrSignedObject.setOadrCreateOpt((OadrCreateOptType) value);
		} else if (value instanceof OadrCreatedOptType) {
			createOadrSignedObject.setOadrCreatedOpt((OadrCreatedOptType) value);
		} else if (value instanceof OadrCancelReportType) {
			createOadrSignedObject.setOadrCancelReport((OadrCancelReportType) value);
		} else if (value instanceof OadrCanceledReportType) {
			createOadrSignedObject.setOadrCanceledReport((OadrCanceledReportType) value);
		} else if (value instanceof OadrCreateReportType) {
			createOadrSignedObject.setOadrCreateReport((OadrCreateReportType) value);
		} else if (value instanceof OadrCreatedReportType) {
			createOadrSignedObject.setOadrCreatedReport((OadrCreatedReportType) value);
		} else if (value instanceof OadrRegisterReportType) {
			createOadrSignedObject.setOadrRegisterReport((OadrRegisterReportType) value);
		} else if (value instanceof OadrRegisteredReportType) {
			createOadrSignedObject.setOadrRegisteredReport((OadrRegisteredReportType) value);
		} else if (value instanceof OadrUpdateReportType) {
			createOadrSignedObject.setOadrUpdateReport((OadrUpdateReportType) value);
		} else if (value instanceof OadrUpdatedReportType) {
			createOadrSignedObject.setOadrUpdatedReport((OadrUpdatedReportType) value);
		} else if (value instanceof OadrCancelPartyRegistrationType) {
			createOadrSignedObject.setOadrCancelPartyRegistration((OadrCancelPartyRegistrationType) value);
		} else if (value instanceof OadrCanceledPartyRegistrationType) {
			createOadrSignedObject.setOadrCanceledPartyRegistration((OadrCanceledPartyRegistrationType) value);
		} else if (value instanceof OadrCreatePartyRegistrationType) {
			createOadrSignedObject.setOadrCreatePartyRegistration((OadrCreatePartyRegistrationType) value);
		} else if (value instanceof OadrCreatedPartyRegistrationType) {
			createOadrSignedObject.setOadrCreatedPartyRegistration((OadrCreatedPartyRegistrationType) value);
		} else if (value instanceof OadrRequestReregistrationType) {
			createOadrSignedObject.setOadrRequestReregistration((OadrRequestReregistrationType) value);
		} else if (value instanceof OadrQueryRegistrationType) {
			createOadrSignedObject.setOadrQueryRegistration((OadrQueryRegistrationType) value);
		} else if (value instanceof OadrPollType) {
			createOadrSignedObject.setOadrPoll((OadrPollType) value);
		}
		OadrPayload createOadrPayload = factory.createOadrPayload();
		createOadrPayload.setOadrSignedObject(createOadrSignedObject);
		return createOadrPayload;
	}

	public static <T> T getSignedObjectFromOadrPayload(OadrPayload payload, Class<T> klass) {
		Object signedObjectFromOadrPayload = getSignedObjectFromOadrPayload(payload);
		if (signedObjectFromOadrPayload == null || !klass.equals(signedObjectFromOadrPayload.getClass())) {
			return null;
		}
		return klass.cast(signedObjectFromOadrPayload);

	}

	public static Object getSignedObjectFromOadrPayload(OadrPayload payload) {
		OadrSignedObject createOadrSignedObject = payload.getOadrSignedObject();

		if (createOadrSignedObject.getOadrDistributeEvent() != null) {
			return createOadrSignedObject.getOadrDistributeEvent();
		} else if (createOadrSignedObject.getOadrCreatedEvent() != null) {
			return createOadrSignedObject.getOadrCreatedEvent();
		} else if (createOadrSignedObject.getOadrRequestEvent() != null) {
			return createOadrSignedObject.getOadrRequestEvent();
		} else if (createOadrSignedObject.getOadrResponse() != null) {
			return createOadrSignedObject.getOadrResponse();
		} else if (createOadrSignedObject.getOadrCancelOpt() != null) {
			return createOadrSignedObject.getOadrCancelOpt();
		} else if (createOadrSignedObject.getOadrCanceledOpt() != null) {
			return createOadrSignedObject.getOadrCanceledOpt();
		} else if (createOadrSignedObject.getOadrCreateOpt() != null) {
			return createOadrSignedObject.getOadrCreateOpt();
		} else if (createOadrSignedObject.getOadrCreatedOpt() != null) {
			return createOadrSignedObject.getOadrCreatedOpt();
		} else if (createOadrSignedObject.getOadrCancelReport() != null) {
			return createOadrSignedObject.getOadrCancelReport();
		} else if (createOadrSignedObject.getOadrCanceledReport() != null) {
			return createOadrSignedObject.getOadrCanceledReport();
		} else if (createOadrSignedObject.getOadrCreateReport() != null) {
			return createOadrSignedObject.getOadrCreateReport();
		} else if (createOadrSignedObject.getOadrCreatedReport() != null) {
			return createOadrSignedObject.getOadrCreatedReport();
		} else if (createOadrSignedObject.getOadrRegisterReport() != null) {
			return createOadrSignedObject.getOadrRegisterReport();
		} else if (createOadrSignedObject.getOadrRegisteredReport() != null) {
			return createOadrSignedObject.getOadrRegisteredReport();
		} else if (createOadrSignedObject.getOadrUpdateReport() != null) {
			return createOadrSignedObject.getOadrUpdateReport();
		} else if (createOadrSignedObject.getOadrUpdatedReport() != null) {
			return createOadrSignedObject.getOadrUpdatedReport();
		} else if (createOadrSignedObject.getOadrCancelPartyRegistration() != null) {
			return createOadrSignedObject.getOadrCancelPartyRegistration();
		} else if (createOadrSignedObject.getOadrCanceledPartyRegistration() != null) {
			return createOadrSignedObject.getOadrCanceledPartyRegistration();
		} else if (createOadrSignedObject.getOadrCreatePartyRegistration() != null) {
			return createOadrSignedObject.getOadrCreatePartyRegistration();
		} else if (createOadrSignedObject.getOadrCreatedPartyRegistration() != null) {
			return createOadrSignedObject.getOadrCreatedPartyRegistration();
		} else if (createOadrSignedObject.getOadrRequestReregistration() != null) {
			return createOadrSignedObject.getOadrRequestReregistration();
		} else if (createOadrSignedObject.getOadrQueryRegistration() != null) {
			return createOadrSignedObject.getOadrQueryRegistration();
		} else if (createOadrSignedObject.getOadrPoll() != null) {
			return createOadrSignedObject.getOadrPoll();
		}

		return null;
	}

	public static OadrCreatedEventType createOadrCreatedEventType(String venId) {
		OadrCreatedEventType createOadrCreatedEventType = factory.createOadrCreatedEventType();
		EiCreatedEvent value = pyldFactory.createEiCreatedEvent();
		value.setVenID(venId);
		createOadrCreatedEventType.setEiCreatedEvent(value);
		return createOadrCreatedEventType;
	}

	public static OadrRequestEventType createOadrRequestEventType(String venId, String requestId) {
		OadrRequestEventType createOadrRequestEventType = factory.createOadrRequestEventType();
		EiRequestEvent eiRequestEvent = pyldFactory.createEiRequestEvent();
		eiRequestEvent.setVenID(venId);
		eiRequestEvent.setRequestID(requestId);
		createOadrRequestEventType.setEiRequestEvent(eiRequestEvent);
		return createOadrRequestEventType;
	}

	public static OadrResponseType createOadrResponseType(String requestId, int responseCode) {
		OadrResponseType createOadrResponseType = factory.createOadrResponseType();
		EiResponseType eiResponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrResponseType.setEiResponse(eiResponse);
		return createOadrResponseType;
	}

	public static OadrResponseType createOadrResponseType(String requestId, int responseCode, String venId) {
		OadrResponseType createOadrResponseType = factory.createOadrResponseType();
		EiResponseType eiResponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrResponseType.setEiResponse(eiResponse);
		createOadrResponseType.setVenID(venId);
		return createOadrResponseType;
	}

	public static OadrCancelOptType createOadrCancelOptType(String requestId, String optId, String venId) {
		OadrCancelOptType createOadrCancelOptType = factory.createOadrCancelOptType();
		createOadrCancelOptType.setRequestID(requestId);
		createOadrCancelOptType.setOptID(optId);
		createOadrCancelOptType.setVenID(venId);
		return createOadrCancelOptType;
	}

	public static OadrCanceledOptType createOadrCanceledOptType(String requestId, int responseCode, String optId) {
		OadrCanceledOptType createOadrCanceledOptType = factory.createOadrCanceledOptType();
		EiResponseType eiResponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrCanceledOptType.setEiResponse(eiResponse);
		createOadrCanceledOptType.setOptID(optId);
		return createOadrCanceledOptType;
	}

	public static OadrCreateOptType createOadrCreateOptType(String requestId, String venId, Long createdDatetime,
			String eventId, long modificationNumber, String optId, OptTypeType optType,
			OptReasonEnumeratedType optReason) {
		OadrCreateOptType createOadrCreateOptType = factory.createOadrCreateOptType();
		createOadrCreateOptType.setCreatedDateTime(timestamptoXMLCalendar(createdDatetime));
		createOadrCreateOptType.setOptID(optId);
		createOadrCreateOptType.setOptType(optType);
		createOadrCreateOptType.setRequestID(requestId);
		createOadrCreateOptType.setVenID(venId);
		createOadrCreateOptType.setOptReason(optReason.value());
		QualifiedEventIDType qualifiedEventId = Oadr20bFactory.createQualifiedEventIDType(eventId, modificationNumber);
		createOadrCreateOptType.setQualifiedEventID(qualifiedEventId);

		return createOadrCreateOptType;
	}

	public static OadrCreateOptType createOadrCreateOptType(String requestId, String venId, Long createdDatetime,
			VavailabilityType vavailabilityType, String optId, OptTypeType optType, OptReasonEnumeratedType optReason) {
		OadrCreateOptType createOadrCreateOptType = factory.createOadrCreateOptType();
		createOadrCreateOptType.setCreatedDateTime(timestamptoXMLCalendar(createdDatetime));
		createOadrCreateOptType.setOptID(optId);
		createOadrCreateOptType.setOptType(optType);
		createOadrCreateOptType.setRequestID(requestId);
		createOadrCreateOptType.setVenID(venId);
		createOadrCreateOptType.setOptReason(optReason.value());
		createOadrCreateOptType.setVavailability(vavailabilityType);

		return createOadrCreateOptType;
	}

	public static OadrCreatedOptType createOadrCreatedOptType(String requestId, int responseCode, String optId) {
		OadrCreatedOptType createOadrCreatedOptType = factory.createOadrCreatedOptType();
		createOadrCreatedOptType.setOptID(optId);
		EiResponseType eiReponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrCreatedOptType.setEiResponse(eiReponse);
		return createOadrCreatedOptType;
	}

	public static OadrCancelReportType createOadrCancelReportType(String requestId, String venId,
			boolean reportToFollow) {
		OadrCancelReportType createOadrCancelReportType = factory.createOadrCancelReportType();
		createOadrCancelReportType.setRequestID(requestId);
		createOadrCancelReportType.setVenID(venId);
		createOadrCancelReportType.setReportToFollow(reportToFollow);
		return createOadrCancelReportType;
	}

	public static OadrCanceledReportType createOadrCanceledReportType(String requestId, int responseCode,
			String venId) {
		OadrCanceledReportType createOadrCanceledReportType = factory.createOadrCanceledReportType();
		EiResponseType eiResponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrCanceledReportType.setVenID(venId);
		createOadrCanceledReportType.setEiResponse(eiResponse);
		OadrPendingReportsType pendingReports = Oadr20bFactory.createOadrPendingReportsType();
		createOadrCanceledReportType.setOadrPendingReports(pendingReports);
		return createOadrCanceledReportType;
	}

	public static OadrCreateReportType createOadrCreateReportType(String requestId, String venId) {
		OadrCreateReportType createOadrCreateReportType = factory.createOadrCreateReportType();
		createOadrCreateReportType.setRequestID(requestId);
		createOadrCreateReportType.setVenID(venId);
		return createOadrCreateReportType;
	}

	public static OadrCreatedReportType createOadrCreatedReportType(String requestId, int responseCode, String venId) {
		OadrCreatedReportType createOadrCreatedReportType = factory.createOadrCreatedReportType();
		EiResponseType eiResponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrCreatedReportType.setEiResponse(eiResponse);
		createOadrCreatedReportType.setVenID(venId);
		OadrPendingReportsType penddingReports = Oadr20bFactory.createOadrPendingReportsType();
		createOadrCreatedReportType.setOadrPendingReports(penddingReports);
		return createOadrCreatedReportType;
	}

	public static OadrRegisterReportType createOadrRegisterReportType(String requestId, String venId) {
		OadrRegisterReportType createOadrRegisterReportType = factory.createOadrRegisterReportType();
		createOadrRegisterReportType.setRequestID(requestId);
		createOadrRegisterReportType.setVenID(venId);
		createOadrRegisterReportType.setReportRequestID("0");
		return createOadrRegisterReportType;
	}

	public static OadrRegisteredReportType createOadrRegisteredReportType(String requestId, int responseCode,
			String venId) {
		OadrRegisteredReportType createOadrRegisteredReportType = factory.createOadrRegisteredReportType();
		EiResponseType eiResponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrRegisteredReportType.setEiResponse(eiResponse);
		createOadrRegisteredReportType.setVenID(venId);
		return createOadrRegisteredReportType;
	}

	public static OadrUpdateReportType createOadrUpdateReportType(String requestId, String venId) {
		OadrUpdateReportType createOadrUpdateReportType = factory.createOadrUpdateReportType();
		createOadrUpdateReportType.setRequestID(requestId);
		createOadrUpdateReportType.setVenID(venId);
		return createOadrUpdateReportType;
	}

	public static OadrUpdatedReportType createOadrUpdatedReportType(String requestId, int responseCode, String venId) {
		OadrUpdatedReportType createOadrUpdatedReportType = factory.createOadrUpdatedReportType();
		EiResponseType eiResponse = Oadr20bFactory.createEiResponseType(requestId, responseCode);
		createOadrUpdatedReportType.setEiResponse(eiResponse);
		createOadrUpdatedReportType.setVenID(venId);
		return createOadrUpdatedReportType;
	}

	public static OadrCancelPartyRegistrationType createOadrCancelPartyRegistrationType(String requestId,
			String registrationId, String venId) {
		OadrCancelPartyRegistrationType createOadrCancelPartyRegistrationType = factory
				.createOadrCancelPartyRegistrationType();
		createOadrCancelPartyRegistrationType.setRequestID(requestId);
		createOadrCancelPartyRegistrationType.setRegistrationID(registrationId);
		createOadrCancelPartyRegistrationType.setVenID(venId);
		return createOadrCancelPartyRegistrationType;
	}

	public static OadrCanceledPartyRegistrationType createOadrCanceledPartyRegistrationType(EiResponseType eiResponse,
			String registrationId, String venId) {
		OadrCanceledPartyRegistrationType createOadrCanceledPartyRegistrationType = factory
				.createOadrCanceledPartyRegistrationType();
		createOadrCanceledPartyRegistrationType.setEiResponse(eiResponse);
		createOadrCanceledPartyRegistrationType.setRegistrationID(registrationId);
		createOadrCanceledPartyRegistrationType.setVenID(venId);
		return createOadrCanceledPartyRegistrationType;
	}

	public static OadrCreatePartyRegistrationType createOadrCreatePartyRegistrationType(String requestId, String venId,
			String profilName) {
		OadrCreatePartyRegistrationType createOadrCreatePartyRegistrationType = factory
				.createOadrCreatePartyRegistrationType();
		createOadrCreatePartyRegistrationType.setRequestID(requestId);
		createOadrCreatePartyRegistrationType.setVenID(venId);
		createOadrCreatePartyRegistrationType.setOadrProfileName(profilName);
		createOadrCreatePartyRegistrationType.setOadrHttpPullModel(true);
		createOadrCreatePartyRegistrationType.setOadrReportOnly(false);
		createOadrCreatePartyRegistrationType.setOadrXmlSignature(false);
		return createOadrCreatePartyRegistrationType;
	}

	public static OadrRequestReregistrationType createOadrRequestReregistrationType(String venId) {
		OadrRequestReregistrationType createOadrRequestReregistrationType = factory
				.createOadrRequestReregistrationType();
		createOadrRequestReregistrationType.setVenID(venId);
		return createOadrRequestReregistrationType;
	}

	public static OadrQueryRegistrationType createOadrQueryRegistrationType(String requestId) {
		OadrQueryRegistrationType createOadrQueryRegistrationType = factory.createOadrQueryRegistrationType();
		createOadrQueryRegistrationType.setRequestID(requestId);
		return createOadrQueryRegistrationType;
	}

	public static OadrPollType createOadrPollType(String venId) {
		OadrPollType createOadrPollType = factory.createOadrPollType();
		createOadrPollType.setVenID(venId);
		return createOadrPollType;
	}

	public static OadrProfile createOadrProfilesOadrProfile(String profileName) {
		OadrProfile createOadrProfilesOadrProfile = factory.createOadrProfilesOadrProfile();
		createOadrProfilesOadrProfile.setOadrProfileName(profileName);
		OadrTransports transports = Oadr20bFactory.createOadrTransports();
		createOadrProfilesOadrProfile.setOadrTransports(transports);
		return createOadrProfilesOadrProfile;
	}

	public static OadrTransport createOadrTransportsOadrTransport(OadrTransportType transportName) {
		OadrTransport createOadrTransportsOadrTransport = factory.createOadrTransportsOadrTransport();
		createOadrTransportsOadrTransport.setOadrTransportName(transportName);
		return createOadrTransportsOadrTransport;
	}

	public static OadrPendingReportsType createOadrPendingReportsType() {
		return factory.createOadrPendingReportsType();
	}

	public static BaseUnitType createBaseUnitType(String description, String units, SiScaleCodeType siscaleCode) {
		BaseUnitType createBaseUnitType = factory.createBaseUnitType();
		createBaseUnitType.setItemDescription(description);
		createBaseUnitType.setItemUnits(units);
		createBaseUnitType.setSiScaleCode(siscaleCode);
		return createBaseUnitType;
	}

	public static CurrencyType createCurrencyType(CurrencyItemDescriptionType description,
			ISO3AlphaCurrencyCodeContentType units, SiScaleCodeType siscaleCode) {
		CurrencyType createCurrencyType = factory.createCurrencyType();
		createCurrencyType.setItemDescription(description);
		createCurrencyType.setItemUnits(units);
		createCurrencyType.setSiScaleCode(siscaleCode);
		return createCurrencyType;
	}

	public static FrequencyType createFrequencyType(SiScaleCodeType siscaleCode) {
		FrequencyType createFrequencyType = factory.createFrequencyType();
		createFrequencyType.setSiScaleCode(siscaleCode);
		return createFrequencyType;
	}

	public static ThermType createThermType(SiScaleCodeType siscaleCode) {
		ThermType createThermType = factory.createThermType();
		createThermType.setSiScaleCode(siscaleCode);
		return createThermType;
	}

	public static TemperatureType createTemperatureType(TemperatureUnitType units, SiScaleCodeType siscaleCode) {
		TemperatureType createTemperatureType = factory.createTemperatureType();
		createTemperatureType.setItemUnits(units);
		createTemperatureType.setSiScaleCode(siscaleCode);
		return createTemperatureType;
	}

	public static PulseCountType createPulseCountType(float pulseFactor) {
		PulseCountType createPulseCountType = factory.createPulseCountType();
		createPulseCountType.setPulseFactor(pulseFactor);
		return createPulseCountType;
	}

	public static OadrReportRequestType createOadrReportRequestType(String reportRequestId, String reportSpecifierId,
			String granularity, String reportBackDuration) {
		OadrReportRequestType createOadrReportRequestType = factory.createOadrReportRequestType();
		createOadrReportRequestType.setReportRequestID(reportRequestId);
		ReportSpecifierType reportSpecifier = Oadr20bFactory.createReportSpecifierType(reportSpecifierId, granularity,
				reportBackDuration);
		createOadrReportRequestType.setReportSpecifier(reportSpecifier);
		return createOadrReportRequestType;
	}

	public static OadrReportType createOadrUpdateReportOadrReportType(String reportId, String reportSpecifierId,
			String reportrequestId, ReportNameEnumeratedType reportName, long createdTimestamp, Long startTimestamp,
			String duration) {
		OadrReportType createOadrReportType = factory.createOadrReportType();
		createOadrReportType.setReportRequestID(reportrequestId);
		createOadrReportType.setReportSpecifierID(reportSpecifierId);
		if (reportName != null) {
			createOadrReportType.setReportName(reportName.value());
		}
		createOadrReportType.setCreatedDateTime(timestamptoXMLCalendar(createdTimestamp));
		if (startTimestamp != null) {
			createOadrReportType.setDtstart(Oadr20bFactory.createDtstart(startTimestamp));
		}
		if (duration != null) {
			createOadrReportType.setDuration(Oadr20bFactory.createDurationPropType(duration));
		}
		Intervals intervals = Oadr20bFactory.createIntervals();
		createOadrReportType.setIntervals(intervals);
		return createOadrReportType;
	}

	public static OadrReportType createOadrRegisterReportOadrReportType(String reportSpecifierId,
			ReportNameEnumeratedType reportName, long createdTimestamp) {
		OadrReportType createOadrReportType = factory.createOadrReportType();
		createOadrReportType.setReportSpecifierID(reportSpecifierId);
		createOadrReportType.setReportName(reportName.value());
		createOadrReportType.setCreatedDateTime(timestamptoXMLCalendar(createdTimestamp));
		createOadrReportType.setReportRequestID("0");
		return createOadrReportType;
	}

	public static OadrReportType createOadrUpdateReportOadrReportType(String reportSpecifierId, String reportRequestId,
			ReportNameEnumeratedType reportName, long createdTimestamp) {
		OadrReportType createOadrReportType = factory.createOadrReportType();
		createOadrReportType.setReportSpecifierID(reportSpecifierId);
		createOadrReportType.setReportName(reportName.value());
		createOadrReportType.setCreatedDateTime(timestamptoXMLCalendar(createdTimestamp));
		createOadrReportType.setReportRequestID(reportRequestId);
		return createOadrReportType;
	}

	public static OadrReportDescriptionType createOadrReportDescriptionType() {
		return factory.createOadrReportDescriptionType();
	}

	public static OadrSamplingRateType createOadrSamplingRateType() {
		return factory.createOadrSamplingRateType();
	}

	public static OadrEvent createOadrDistributeEventTypeOadrEvent() {
		OadrEvent createOadrDistributeEventTypeOadrEvent = factory.createOadrDistributeEventTypeOadrEvent();
		createOadrDistributeEventTypeOadrEvent.setEiEvent(Oadr20bFactory.createEiEventType());
		return createOadrDistributeEventTypeOadrEvent;
	}

	public static JAXBElement<OadrDistributeEventType> createOadrDistributeEvent(OadrDistributeEventType value) {
		return factory.createOadrDistributeEvent(value);
	}

	public static JAXBElement<OadrCreatedEventType> createOadrCreatedEvent(OadrCreatedEventType value) {
		return factory.createOadrCreatedEvent(value);
	}

	public static JAXBElement<OadrRequestEventType> createOadrRequestEvent(OadrRequestEventType value) {
		return factory.createOadrRequestEvent(value);
	}

	public static JAXBElement<OadrResponseType> createOadrResponse(OadrResponseType value) {
		return factory.createOadrResponse(value);
	}

	public static JAXBElement<OadrCancelOptType> createOadrCancelOpt(OadrCancelOptType value) {
		return factory.createOadrCancelOpt(value);
	}

	public static JAXBElement<OadrCanceledOptType> createOadrCanceledOpt(OadrCanceledOptType value) {
		return factory.createOadrCanceledOpt(value);
	}

	public static JAXBElement<OadrCreateOptType> createOadrCreateOpt(OadrCreateOptType value) {
		return factory.createOadrCreateOpt(value);
	}

	public static JAXBElement<OadrCreatedOptType> createOadrCreatedOpt(OadrCreatedOptType value) {
		return factory.createOadrCreatedOpt(value);
	}

	public static JAXBElement<OadrCancelReportType> createOadrCancelReport(OadrCancelReportType value) {
		return factory.createOadrCancelReport(value);
	}

	public static JAXBElement<OadrCanceledReportType> createOadrCanceledReport(OadrCanceledReportType value) {
		return factory.createOadrCanceledReport(value);
	}

	public static JAXBElement<OadrCreateReportType> createOadrCreateReport(OadrCreateReportType value) {
		return factory.createOadrCreateReport(value);
	}

	public static JAXBElement<OadrCreatedReportType> createOadrCreatedReport(OadrCreatedReportType value) {
		return factory.createOadrCreatedReport(value);
	}

	public static JAXBElement<OadrRegisterReportType> createOadrRegisterReport(OadrRegisterReportType value) {
		return factory.createOadrRegisterReport(value);
	}

	public static JAXBElement<OadrRegisteredReportType> createOadrRegisteredReport(OadrRegisteredReportType value) {
		return factory.createOadrRegisteredReport(value);
	}

	public static JAXBElement<OadrUpdateReportType> createOadrUpdateReport(OadrUpdateReportType value) {
		return factory.createOadrUpdateReport(value);
	}

	public static JAXBElement<OadrUpdatedReportType> createOadrUpdatedReport(OadrUpdatedReportType value) {
		return factory.createOadrUpdatedReport(value);
	}

	public static JAXBElement<OadrCancelPartyRegistrationType> createOadrCancelPartyRegistration(
			OadrCancelPartyRegistrationType value) {
		return factory.createOadrCancelPartyRegistration(value);
	}

	public static JAXBElement<OadrCanceledPartyRegistrationType> createOadrCanceledPartyRegistration(
			OadrCanceledPartyRegistrationType value) {
		return factory.createOadrCanceledPartyRegistration(value);
	}

	public static JAXBElement<OadrCreatePartyRegistrationType> createOadrCreatePartyRegistration(
			OadrCreatePartyRegistrationType value) {
		return factory.createOadrCreatePartyRegistration(value);
	}

	public static JAXBElement<OadrCreatedPartyRegistrationType> createOadrCreatedPartyRegistration(
			OadrCreatedPartyRegistrationType value) {
		return factory.createOadrCreatedPartyRegistration(value);
	}

	public static JAXBElement<OadrRequestReregistrationType> createOadrRequestReregistration(
			OadrRequestReregistrationType value) {
		return factory.createOadrRequestReregistration(value);
	}

	public static JAXBElement<OadrQueryRegistrationType> createOadrQueryRegistration(OadrQueryRegistrationType value) {
		return factory.createOadrQueryRegistration(value);
	}

	public static JAXBElement<OadrPollType> createOadrPoll(OadrPollType value) {
		return factory.createOadrPoll(value);
	}

	public static JAXBElement<BaseUnitType> createCustomUnit(BaseUnitType value) {
		return factory.createCustomUnit(value);
	}

	public static JAXBElement<CurrencyType> createCurrency(CurrencyType value) {
		return factory.createCurrency(value);
	}

	public static JAXBElement<CurrencyType> createCurrencyPerKWh(CurrencyType value) {
		return factory.createCurrencyPerKWh(value);
	}

	public static JAXBElement<CurrencyType> createCurrencyPerKW(CurrencyType value) {
		return factory.createCurrencyPerKW(value);
	}

	public static JAXBElement<FrequencyType> createFrequency(FrequencyType value) {
		return factory.createFrequency(value);
	}

	public static JAXBElement<ThermType> createTherm(ThermType value) {
		return factory.createTherm(value);
	}

	public static JAXBElement<TemperatureType> createTemperature(TemperatureType value) {
		return factory.createTemperature(value);
	}

	public static JAXBElement<PulseCountType> createPulseCount(PulseCountType value) {
		return factory.createPulseCount(value);
	}

	public static JAXBElement<OadrGBItemBase> createGBItemBase(OadrGBItemBase value) {
		return factory.createOadrGBDataDescription(value);
	}

	public static EventResponses createEventResponses() {
		return eiFactory.createEventResponses();
	}

	public static EventDescriptorType createEventDescriptorType(Long createdTimespamp, String eventId,
			Long modificationNumber, String marketContext, EventStatusEnumeratedType status) {

		EventDescriptorType eventDescriptor = eiFactory.createEventDescriptorType();
		eventDescriptor.setCreatedDateTime(timestamptoXMLCalendar(createdTimespamp));
		if (eventId != null) {
			eventDescriptor.setEventID(eventId);
		}
		if (modificationNumber != null) {
			eventDescriptor.setModificationNumber(modificationNumber);
		}
		eventDescriptor.setEiMarketContext(Oadr20bFactory.createEventDescriptorTypeEiMarketContext(marketContext));
		eventDescriptor.setEventStatus(status);
		eventDescriptor.setTestEvent("false");

		return eventDescriptor;
	}

	public static EiResponseType createEiResponseType(String requestId, int responseCode) {
		EiResponseType createEiResponseType = eiFactory.createEiResponseType();
		createEiResponseType.setRequestID(requestId);
		createEiResponseType.setResponseCode(String.valueOf(responseCode));
		return createEiResponseType;
	}

	public static EventResponse createEventResponsesEventResponse() {
		return eiFactory.createEventResponsesEventResponse();
	}

	public static QualifiedEventIDType createQualifiedEventIDType(String eventId, long modificationNumber) {
		QualifiedEventIDType createQualifiedEventIDType = eiFactory.createQualifiedEventIDType();
		createQualifiedEventIDType.setEventID(eventId);
		createQualifiedEventIDType.setModificationNumber(modificationNumber);
		return createQualifiedEventIDType;
	}

	public static IntervalType createSignalIntervalType(String intervalId, long start, String xmlDuration,
			Float value) {
		IntervalType createIntervalType = eiFactory.createIntervalType();
		createIntervalType.setUid(Oadr20bFactory.createUidType(intervalId));
		createIntervalType.setDuration(Oadr20bFactory.createDurationPropType(xmlDuration));
		createIntervalType.setDtstart(Oadr20bFactory.createDtstart(start));
		createIntervalType.getStreamPayloadBase()
				.add(Oadr20bFactory.createSignalPayload(Oadr20bFactory.createSignalPayloadType(value)));
		return createIntervalType;
	}

	public static IntervalType createReportIntervalType(String intervalId, Long start, String xmlDuration, String rid,
			Long confidence, Float accuracy, Float value) {
		IntervalType createIntervalType = eiFactory.createIntervalType();
		createIntervalType.setUid(Oadr20bFactory.createUidType(intervalId));
		if (xmlDuration != null) {
			createIntervalType.setDuration(Oadr20bFactory.createDurationPropType(xmlDuration));
		}
		if (start != null) {
			createIntervalType.setDtstart(Oadr20bFactory.createDtstart(start));
		}

		createIntervalType.getStreamPayloadBase().add(Oadr20bFactory
				.createOadrReportPayload(Oadr20bFactory.createReportPayloadType(rid, confidence, accuracy, value)));

		return createIntervalType;
	}

	public static IntervalType createKeyTokenReportIntervalType(String intervalId, Long start, String xmlDuration,
			String rid, Long confidence, Float accuracy, PayloadKeyTokenType tokens) {
		IntervalType createIntervalType = eiFactory.createIntervalType();
		createIntervalType.setUid(Oadr20bFactory.createUidType(intervalId));
		if (xmlDuration != null) {
			createIntervalType.setDuration(Oadr20bFactory.createDurationPropType(xmlDuration));
		}
		if (start != null) {
			createIntervalType.setDtstart(Oadr20bFactory.createDtstart(start));
		}

		OadrReportPayloadType createReportPayloadType = Oadr20bFactory.createReportPayloadType(rid, confidence,
				accuracy, tokens);
		createIntervalType.getStreamPayloadBase().add(Oadr20bFactory.createOadrReportPayload(createReportPayloadType));

		return createIntervalType;
	}

	public static IntervalType createAvobVenServiceRequestReportIntervalType(String intervalId, Long start,
			String xmlDuration, String rid, Long confidence, Float accuracy,
			PayloadAvobVenServiceRequestType requests) {
		IntervalType createIntervalType = eiFactory.createIntervalType();
		createIntervalType.setUid(Oadr20bFactory.createUidType(intervalId));
		createIntervalType.setDuration(Oadr20bFactory.createDurationPropType(xmlDuration));
		createIntervalType.setDtstart(Oadr20bFactory.createDtstart(start));

		OadrReportPayloadType createReportPayloadType = Oadr20bFactory.createReportPayloadType(rid, confidence,
				accuracy, requests);
		createIntervalType.getStreamPayloadBase().add(Oadr20bFactory.createOadrReportPayload(createReportPayloadType));

		return createIntervalType;
	}

	public static IntervalType createReportIntervalType(String intervalId, Long start, String xmlDuration, String rid,
			Long confidence, Float accuracy, OadrPayloadResourceStatusType value) {
		IntervalType createIntervalType = eiFactory.createIntervalType();
		createIntervalType.setUid(Oadr20bFactory.createUidType(intervalId));
		createIntervalType.setDuration(Oadr20bFactory.createDurationPropType(xmlDuration));
		createIntervalType.setDtstart(Oadr20bFactory.createDtstart(start));
		createIntervalType.getStreamPayloadBase().add(Oadr20bFactory
				.createOadrReportPayload(Oadr20bFactory.createReportPayloadType(rid, confidence, accuracy, value)));

		return createIntervalType;
	}

	public static CurrentValueType createCurrentValueType(float value) {
		CurrentValueType createCurrentValueType = eiFactory.createCurrentValueType();
		createCurrentValueType.setPayloadFloat(Oadr20bFactory.createPayloadFloatType(value));
		return createCurrentValueType;
	}

	public static PayloadFloatType createPayloadFloatType(float value) {
		PayloadFloatType createPayloadFloatType = eiFactory.createPayloadFloatType();
		createPayloadFloatType.setValue(value);
		return createPayloadFloatType;
	}

	public static EiEventType createEiEventType() {
		EiEventType createEiEventType = eiFactory.createEiEventType();
		createEiEventType.setEiEventSignals(Oadr20bFactory.createEiEventSignalsType());
		return createEiEventType;
	}

	public static EiActivePeriodType createEiActivePeriodType() {
		return eiFactory.createEiActivePeriodType();
	}

	public static SignalPayloadType createSignalPayloadType(float value) {
		SignalPayloadType createSignalPayloadType = eiFactory.createSignalPayloadType();
		createSignalPayloadType.setPayloadBase(Oadr20bFactory.createPayloadFloat(value));
		return createSignalPayloadType;
	}

	public static EiTargetType createEiTargetType() {
		return eiFactory.createEiTargetType();
	}

	public static JAXBElement<EiTargetType> createEiTarget(EiTargetType element) {
		return eiFactory.createEiTarget(element);
	}

	public static EiEventSignalType createEiEventSignalType() {
		return eiFactory.createEiEventSignalType();
	}

	public static EiEventSignalsType createEiEventSignalsType() {
		return eiFactory.createEiEventSignalsType();
	}

	public static ReportSpecifierType createReportSpecifierType(String reportSpecifierId, String granularity,
			String reportBackDuration) {

		ReportSpecifierType createReportSpecifierType = eiFactory.createReportSpecifierType();
		createReportSpecifierType.setReportSpecifierID(reportSpecifierId);

		DurationPropType granularityDuration = Oadr20bFactory.createDurationPropType(granularity);
		createReportSpecifierType.setGranularity(granularityDuration);

		DurationPropType reportBackDurationProp = Oadr20bFactory.createDurationPropType(reportBackDuration);
		createReportSpecifierType.setReportBackDuration(reportBackDurationProp);
		return createReportSpecifierType;
	}

	public static SpecifierPayloadType createSpecifierPayloadType(JAXBElement<? extends ItemBaseType> baseItem,
			ReadingTypeEnumeratedType readingTypeEnumeratedType, String rid) {
		SpecifierPayloadType specifierPayload = eiFactory.createSpecifierPayloadType();
		specifierPayload.setItemBase(baseItem);
		specifierPayload.setReadingType(readingTypeEnumeratedType.value());
		specifierPayload.setRID(rid);
		return specifierPayload;
	}

	public static EiMarketContext createEventDescriptorTypeEiMarketContext(String marketContext) {
		EiMarketContext createEventDescriptorTypeEiMarketContext = eiFactory.createEventDescriptorTypeEiMarketContext();
		createEventDescriptorTypeEiMarketContext.setMarketContext(marketContext);
		return createEventDescriptorTypeEiMarketContext;
	}

	public static JAXBElement<SignalPayloadType> createSignalPayload(SignalPayloadType value) {
		return eiFactory.createSignalPayload(value);
	}

	public static JAXBElement<PayloadFloatType> createPayloadFloat(float value) {
		return eiFactory.createPayloadFloat(Oadr20bFactory.createPayloadFloatType(value));
	}

	public static Intervals createIntervals() {
		return strmFactory.createIntervals();
	}

	public static Properties createProperties(Long start, String duration) {
		Properties createProperties = xcalFactory.createProperties();
		createProperties.setDtstart(Oadr20bFactory.createDtstart(start));
		createProperties.setDuration(Oadr20bFactory.createDurationPropType(duration));
		return createProperties;
	}

	public static Tolerance createPropertiesTolerance(String startAfter) {
		Tolerance createPropertiesTolerance = xcalFactory.createPropertiesTolerance();
		Tolerate tolerate = Oadr20bFactory.createPropertiesToleranceTolerate(startAfter);
		createPropertiesTolerance.setTolerate(tolerate);
		return createPropertiesTolerance;
	}

	public static DurationPropType createDurationPropType(String duration) {
		DurationPropType createDurationPropType = xcalFactory.createDurationPropType();
		createDurationPropType.setDuration(duration);
		return createDurationPropType;
	}

	public static Uid createUidType(String uid) {
		Uid createUid = xcalFactory.createUid();
		createUid.setText(uid);
		return createUid;
	}

	public static Dtstart createDtstart(long startTimestamp) {
		Dtstart createDtstart = xcalFactory.createDtstart();
		createDtstart.setDateTime(timestamptoXMLCalendar(startTimestamp));
		return createDtstart;
	}

	public static VavailabilityType createVavailabilityType() {
		VavailabilityType createVavailabilityType = xcalFactory.createVavailabilityType();
		ArrayOfVavailabilityContainedComponents value = Oadr20bFactory.createArrayOfVavailabilityContainedComponents();
		createVavailabilityType.setComponents(value);
		return createVavailabilityType;
	}

	public static AvailableType createAvailableType(long startTimestamp, String duration) {
		AvailableType createAvailableType = xcalFactory.createAvailableType();
		createAvailableType.setProperties(Oadr20bFactory.createProperties(startTimestamp, duration));
		return createAvailableType;
	}

	public static WsCalendarIntervalType createWsCalendarIntervalType(Long start, String duration) {
		WsCalendarIntervalType createWsCalendarIntervalType = xcalFactory.createWsCalendarIntervalType();
		createWsCalendarIntervalType.setProperties(Oadr20bFactory.createProperties(start, duration));
		return createWsCalendarIntervalType;
	}

	public static ArrayOfVavailabilityContainedComponents createArrayOfVavailabilityContainedComponents() {
		return xcalFactory.createArrayOfVavailabilityContainedComponents();
	}

	public static Tolerate createPropertiesToleranceTolerate(String startAfter) {
		Tolerate createPropertiesToleranceTolerate = xcalFactory.createPropertiesToleranceTolerate();
		createPropertiesToleranceTolerate.setStartafter(startAfter);
		return createPropertiesToleranceTolerate;
	}

	public static ReplayProtectType createReplayProtectType(String nonce, Long timestamp) {
		ReplayProtectType createReplayProtectType = xmldsigPropertiesFactory.createReplayProtectType();
		createReplayProtectType.setNonce(Oadr20bFactory.createNonceValueType(nonce));
		createReplayProtectType.setTimestamp(timestamptoXMLCalendar(timestamp));
		return createReplayProtectType;
	}

	public static NonceValueType createNonceValueType(String value) {
		NonceValueType createNonceValueType = xmldsigPropertiesFactory.createNonceValueType();
		createNonceValueType.setValue(value);
		return createNonceValueType;
	}

	public static JAXBElement<ReplayProtectType> createReplayProtect(ReplayProtectType value) {
		return xmldsigPropertiesFactory.createReplayProtect(value);
	}

	public static EndDeviceAssetType createEndDeviceAssetType(String mrid) {
		EndDeviceAssetType createEndDeviceAssetType = powerFactory.createEndDeviceAssetType();
		createEndDeviceAssetType.setMrid(mrid);
		return createEndDeviceAssetType;
	}

	public static MeterAssetType createMeterAssetType(String mrid) {
		MeterAssetType createMeterAssetType = powerFactory.createMeterAssetType();
		createMeterAssetType.setMrid(mrid);
		return createMeterAssetType;
	}

	public static PnodeType createPnodeType(String node) {
		PnodeType createPnodeType = powerFactory.createPnodeType();
		createPnodeType.setNode(node);
		return createPnodeType;
	}

	public static AggregatedPnodeType createAggregatedPnodeType(String node) {
		AggregatedPnodeType createAggregatedPnodeType = powerFactory.createAggregatedPnodeType();
		createAggregatedPnodeType.setNode(node);
		return createAggregatedPnodeType;
	}

	public static ServiceLocationType createServiceLocationType(String featureCollectionId, String locationId,
			Collection<? extends Double> positions) {
		ServiceLocationType createServiceLocationType = powerFactory.createServiceLocationType();
		createServiceLocationType.setFeatureCollection(
				Oadr20bFactory.createFeatureCollection(featureCollectionId, locationId, positions));
		return createServiceLocationType;
	}

	public static ServiceDeliveryPointType createServiceDeliveryPointType(String node) {
		ServiceDeliveryPointType createServiceDeliveryPointType = powerFactory.createServiceDeliveryPointType();
		createServiceDeliveryPointType.setNode(node);
		return createServiceDeliveryPointType;
	}

	public static TransportInterfaceType createTransportInterfaceType(String pointOfReceipt, String pointOfDelivery) {
		TransportInterfaceType createTransportInterfaceType = powerFactory.createTransportInterfaceType();
		createTransportInterfaceType.setPointOfDelivery(pointOfDelivery);
		createTransportInterfaceType.setPointOfReceipt(pointOfReceipt);
		return createTransportInterfaceType;
	}

	public static VoltageType createVoltageType(SiScaleCodeType siscaleCode) {
		VoltageType createVoltageType = powerFactory.createVoltageType();
		createVoltageType.setSiScaleCode(siscaleCode);
		return createVoltageType;
	}

	public static OadrGBItemBase createGBItemBaseType(FeedType feed) {
		OadrGBItemBase createOadrGBItemBase = factory.createOadrGBItemBase();
		createOadrGBItemBase.setFeed(feed);
		return createOadrGBItemBase;

	}

	public static CurrentType createCurrentType(SiScaleCodeType siscaleCode) {
		CurrentType createCurrentType = factory.createCurrentType();
		createCurrentType.setSiScaleCode(siscaleCode);
		return createCurrentType;

	}

	public static EnergyApparentType createEnergyApparentType(SiScaleCodeType siscaleCode) {
		EnergyApparentType createEnergyApparentType = powerFactory.createEnergyApparentType();
		createEnergyApparentType.setSiScaleCode(siscaleCode);
		createEnergyApparentType.setItemDescription("ApparentEnergy");
		createEnergyApparentType.setItemUnits("VAh");
		return createEnergyApparentType;
	}

	public static EnergyReactiveType createEnergyReactiveType(SiScaleCodeType siscaleCode) {
		EnergyReactiveType createEnergyReactiveType = powerFactory.createEnergyReactiveType();
		createEnergyReactiveType.setSiScaleCode(siscaleCode);
		createEnergyReactiveType.setItemDescription("ReactiveEnergy");
		createEnergyReactiveType.setItemUnits("VARh");
		return createEnergyReactiveType;
	}

	public static EnergyRealType createEnergyRealType(SiScaleCodeType siscaleCode) {
		EnergyRealType createEnergyRealType = powerFactory.createEnergyRealType();
		createEnergyRealType.setSiScaleCode(siscaleCode);
		createEnergyRealType.setItemDescription("RealEnergy");
		createEnergyRealType.setItemUnits("Wh");
		return createEnergyRealType;
	}

	public static PowerApparentType createPowerApparentType(SiScaleCodeType siscaleCode, BigDecimal hertz,
			BigDecimal voltage, boolean isAC) {
		PowerApparentType createPowerApparentType = powerFactory.createPowerApparentType();
		createPowerApparentType.setSiScaleCode(siscaleCode);
		createPowerApparentType.setItemDescription("ApparentPower");
		createPowerApparentType.setItemUnits("VA");
		PowerAttributesType powerAttributes = Oadr20bFactory.createPowerAttributesType(hertz, voltage, isAC);
		createPowerApparentType.setPowerAttributes(powerAttributes);
		return createPowerApparentType;
	}

	public static PowerReactiveType createPowerReactiveType(SiScaleCodeType siscaleCode, BigDecimal hertz,
			BigDecimal voltage, boolean isAC) {
		PowerReactiveType createPowerReactiveType = powerFactory.createPowerReactiveType();
		createPowerReactiveType.setSiScaleCode(siscaleCode);
		createPowerReactiveType.setItemDescription("ReactivePower");
		createPowerReactiveType.setItemUnits("VAR");
		PowerAttributesType powerAttributes = Oadr20bFactory.createPowerAttributesType(hertz, voltage, isAC);
		createPowerReactiveType.setPowerAttributes(powerAttributes);
		return createPowerReactiveType;
	}

	public static PowerRealType createPowerRealType(PowerRealUnitType unit, SiScaleCodeType siscaleCode,
			BigDecimal hertz, BigDecimal voltage, boolean isAC) {
		PowerRealType createPowerRealType = powerFactory.createPowerRealType();
		createPowerRealType.setSiScaleCode(siscaleCode);
		createPowerRealType.setItemDescription("RealPower");
		createPowerRealType.setItemUnits(unit.getCode());
		PowerAttributesType powerAttributes = Oadr20bFactory.createPowerAttributesType(hertz, voltage, isAC);
		createPowerRealType.setPowerAttributes(powerAttributes);
		return createPowerRealType;
	}

	public static PowerAttributesType createPowerAttributesType(BigDecimal hertz, BigDecimal voltage, boolean isAC) {
		PowerAttributesType createPowerAttributesType = powerFactory.createPowerAttributesType();
		createPowerAttributesType.setHertz(hertz);
		createPowerAttributesType.setVoltage(voltage);
		createPowerAttributesType.setAc(isAC);
		return createPowerAttributesType;
	}

	public static JAXBElement<PowerAttributesType> createPowerAttributes(PowerAttributesType value) {
		return powerFactory.createPowerAttributes(value);
	}

	public static JAXBElement<VoltageType> createVoltage(VoltageType value) {
		return powerFactory.createVoltage(value);
	}

	public static JAXBElement<CurrentType> createCurrent(CurrentType value) {
		return factory.createCurrent(value);
	}

	public static JAXBElement<EnergyApparentType> createEnergyApparent(EnergyApparentType value) {
		return powerFactory.createEnergyApparent(value);
	}

	public static JAXBElement<EnergyReactiveType> createEnergyReactive(EnergyReactiveType value) {
		return powerFactory.createEnergyReactive(value);
	}

	public static JAXBElement<EnergyRealType> createEnergyReal(EnergyRealType value) {
		return powerFactory.createEnergyReal(value);
	}

	public static JAXBElement<PowerApparentType> createPowerApparent(PowerApparentType value) {
		return powerFactory.createPowerApparent(value);
	}

	public static JAXBElement<PowerReactiveType> createPowerReactive(PowerReactiveType value) {
		return powerFactory.createPowerReactive(value);
	}

	public static JAXBElement<PowerRealType> createPowerReal(PowerRealType value) {
		return powerFactory.createPowerReal(value);
	}

	public static ServiceAreaType createServiceAreaType(String featureCollectionId, String locationId,
			Collection<? extends Double> gmlPosition) {
		ServiceAreaType createServiceAreaType = emixFactory.createServiceAreaType();
		createServiceAreaType.setFeatureCollection(
				Oadr20bFactory.createFeatureCollection(featureCollectionId, locationId, gmlPosition));

		return createServiceAreaType;
	}

	public static FeatureCollection createFeatureCollection(String featureCollectionId, String locationId,
			Collection<? extends Double> positions) {
		FeatureCollection createFeatureCollection = gmlFactory.createFeatureCollection();
		createFeatureCollection.setId(featureCollectionId);
		createFeatureCollection.setLocation(Oadr20bFactory.createFeatureCollectionLocation(locationId, positions));
		return createFeatureCollection;
	}

	public static Location createFeatureCollectionLocation(String id, Collection<? extends Double> positions) {
		Location createFeatureCollectionLocation = gmlFactory.createFeatureCollectionLocation();
		createFeatureCollectionLocation
				.setPolygon(Oadr20bFactory.createFeatureCollectionLocationPolygon(id, positions));
		return createFeatureCollectionLocation;
	}

	public static Polygon createFeatureCollectionLocationPolygon(String id, Collection<? extends Double> positions) {
		Polygon createFeatureCollectionLocationPolygon = gmlFactory.createFeatureCollectionLocationPolygon();
		createFeatureCollectionLocationPolygon.setId(id);
		createFeatureCollectionLocationPolygon
				.setExterior(Oadr20bFactory.createFeatureCollectionLocationPolygonExterior(positions));
		return createFeatureCollectionLocationPolygon;
	}

	public static Exterior createFeatureCollectionLocationPolygonExterior(Collection<? extends Double> positions) {
		Exterior createFeatureCollectionLocationPolygonExterior = gmlFactory
				.createFeatureCollectionLocationPolygonExterior();
		createFeatureCollectionLocationPolygonExterior
				.setLinearRing(Oadr20bFactory.createFeatureCollectionLocationPolygonExteriorLinearRing(positions));
		return createFeatureCollectionLocationPolygonExterior;
	}

	public static LinearRing createFeatureCollectionLocationPolygonExteriorLinearRing(
			Collection<? extends Double> positions) {
		LinearRing createFeatureCollectionLocationPolygonExteriorLinearRing = gmlFactory
				.createFeatureCollectionLocationPolygonExteriorLinearRing();
		List<String> collect = positions.stream().map(String::valueOf).collect(Collectors.toList());
		createFeatureCollectionLocationPolygonExteriorLinearRing.getPosList().addAll(collect);
		return createFeatureCollectionLocationPolygonExteriorLinearRing;
	}

	public static JAXBElement<OadrReportDescriptionType> createOadrReportDescription(OadrReportDescriptionType value) {
		return factory.createOadrReportDescription(value);
	}

	public static OadrReportPayloadType createReportPayloadType(String rid, Long confidence, Float accuracy,
			Float value) {
		OadrReportPayloadType createReportPayloadType = factory.createOadrReportPayloadType();
		createReportPayloadType.setRID(rid);
		createReportPayloadType.setConfidence(confidence);
		createReportPayloadType.setAccuracy(accuracy);
		createReportPayloadType.setPayloadBase(Oadr20bFactory.createPayloadFloat(value));
		return createReportPayloadType;
	}

	public static OadrReportPayloadType createReportPayloadType(String rid, Long confidence, Float accuracy,
			OadrPayloadResourceStatusType value) {
		OadrReportPayloadType createReportPayloadType = factory.createOadrReportPayloadType();
		createReportPayloadType.setRID(rid);
		createReportPayloadType.setConfidence(confidence);
		createReportPayloadType.setAccuracy(accuracy);
		createReportPayloadType.setPayloadBase(Oadr20bFactory.createOadrPayloadResourceStatus(value));
		return createReportPayloadType;
	}

	public static OadrReportPayloadType createReportPayloadType(String rid, Long confidence, Float accuracy,
			PayloadKeyTokenType tokens) {
		OadrReportPayloadType createReportPayloadType = factory.createOadrReportPayloadType();
		createReportPayloadType.setRID(rid);
		createReportPayloadType.setConfidence(confidence);
		createReportPayloadType.setAccuracy(accuracy);

		createReportPayloadType.setPayloadBase(Oadr20bFactory.createPayloadKeyToken(tokens));
		return createReportPayloadType;
	}

	public static OadrReportPayloadType createReportPayloadType(String rid, Long confidence, Float accuracy,
			PayloadAvobVenServiceRequestType requests) {
		OadrReportPayloadType createReportPayloadType = factory.createOadrReportPayloadType();
		createReportPayloadType.setRID(rid);
		createReportPayloadType.setConfidence(confidence);
		createReportPayloadType.setAccuracy(accuracy);

		createReportPayloadType.setPayloadBase(Oadr20bFactory.createPayloadAvobVenServiceRequest(requests));
		return createReportPayloadType;
	}

	public static JAXBElement<OadrReportPayloadType> createOadrReportPayload(OadrReportPayloadType value) {
		return factory.createOadrReportPayload(value);
	}

	public static OadrPayloadResourceStatusType createOadrPayloadResourceStatusType(
			OadrLoadControlStateType loadControlState, boolean manualOverride, boolean online) {
		OadrPayloadResourceStatusType createOadrPayloadResourceStatusType = factory
				.createOadrPayloadResourceStatusType();
		createOadrPayloadResourceStatusType.setOadrLoadControlState(loadControlState);
		createOadrPayloadResourceStatusType.setOadrManualOverride(manualOverride);
		createOadrPayloadResourceStatusType.setOadrOnline(online);
		return createOadrPayloadResourceStatusType;
	}

	public static JAXBElement<OadrPayloadResourceStatusType> createOadrPayloadResourceStatus(
			OadrPayloadResourceStatusType value) {
		return factory.createOadrPayloadResourceStatus(value);
	}

	public static OadrLoadControlStateType createOadrLoadControlStateType(OadrLoadControlStateTypeType capacity,
			OadrLoadControlStateTypeType levelOffset, OadrLoadControlStateTypeType percentOffset,
			OadrLoadControlStateTypeType setPoint) {
		OadrLoadControlStateType createOadrLoadControlStateType = factory.createOadrLoadControlStateType();
		createOadrLoadControlStateType.setOadrCapacity(capacity);
		createOadrLoadControlStateType.setOadrLevelOffset(levelOffset);
		createOadrLoadControlStateType.setOadrPercentOffset(percentOffset);
		createOadrLoadControlStateType.setOadrSetPoint(setPoint);
		return createOadrLoadControlStateType;
	}

	public static OadrLoadControlStateTypeType createOadrLoadControlStateTypeType(float current, Float normal,
			Float min, Float max) {
		OadrLoadControlStateTypeType createOadrLoadControlStateTypeType = factory.createOadrLoadControlStateTypeType();
		createOadrLoadControlStateTypeType.setOadrCurrent(current);
		createOadrLoadControlStateTypeType.setOadrNormal(normal);
		createOadrLoadControlStateTypeType.setOadrMin(min);
		createOadrLoadControlStateTypeType.setOadrMax(max);
		return createOadrLoadControlStateTypeType;
	}

	public static PayloadKeyTokenType createPayloadKeyTokenType(List<KeyTokenType> tokens) {
		PayloadKeyTokenType createPayloadKeyTokenType = avobFactory.createPayloadKeyTokenType();
		createPayloadKeyTokenType.getTokens().addAll(tokens);
		return createPayloadKeyTokenType;
	}

	public static JAXBElement<PayloadKeyTokenType> createPayloadKeyToken(PayloadKeyTokenType value) {
		return avobFactory.createPayloadKeyToken(value);
	}

	public static JAXBElement<PayloadAvobVenServiceRequestType> createPayloadAvobVenServiceRequest(
			PayloadAvobVenServiceRequestType value) {
		return avobFactory.createPayloadAvobVenServiceRequest(value);
	}

	public static JAXBElement<? extends ItemBaseType> createItemBase(ItemBaseType value) {

		Class<? extends ItemBaseType> declaredType = value.getClass();
		if (declaredType.equals(VoltageType.class)) {
			return powerFactory.createVoltage((VoltageType) value);

		} else if (declaredType.equals(EnergyApparentType.class)) {
			return powerFactory.createEnergyApparent((EnergyApparentType) value);

		} else if (declaredType.equals(EnergyReactiveType.class)) {
			return powerFactory.createEnergyReactive((EnergyReactiveType) value);

		} else if (declaredType.equals(EnergyRealType.class)) {
			return powerFactory.createEnergyReal((EnergyRealType) value);

		} else if (declaredType.equals(PowerApparentType.class)) {
			return powerFactory.createPowerApparent((PowerApparentType) value);

		} else if (declaredType.equals(PowerReactiveType.class)) {
			return powerFactory.createPowerReactive((PowerReactiveType) value);

		} else if (declaredType.equals(PowerRealType.class)) {
			return powerFactory.createPowerReal((PowerRealType) value);

		} else if (declaredType.equals(BaseUnitType.class)) {
			return factory.createCustomUnit((BaseUnitType) value);

		} else if (declaredType.equals(CurrencyType.class)) {
			return factory.createCurrency((CurrencyType) value);

		} else if (declaredType.equals(FrequencyType.class)) {
			return factory.createFrequency((FrequencyType) value);

		} else if (declaredType.equals(ThermType.class)) {
			return factory.createTherm((ThermType) value);

		} else if (declaredType.equals(TemperatureType.class)) {
			return factory.createTemperature((TemperatureType) value);

		} else if (declaredType.equals(PulseCountType.class)) {
			return factory.createPulseCount((PulseCountType) value);

		} else if (declaredType.equals(OadrGBItemBase.class)) {
			return factory.createOadrGBDataDescription((OadrGBItemBase) value);
		} else {
			return emixFactory.createItemBase(value);
		}

	}

	public static EiEventBaselineType createEiEventBaselineType() {
		return eiFactory.createEiEventBaselineType();
	}

}
