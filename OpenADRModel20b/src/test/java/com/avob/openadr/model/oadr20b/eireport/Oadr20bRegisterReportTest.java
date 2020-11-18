package com.avob.openadr.model.oadr20b.eireport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.EndDeviceAssertMridType;
import com.avob.openadr.model.oadr20b.builders.eireport.PowerRealUnitType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.power.PowerRealType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;

public class Oadr20bRegisterReportTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bRegisterReportTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String requestId = null;
		String venId = "venId";
		OadrRegisterReportType request = Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId, venId)
				.build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eireport/unvalidatingOadrRegisterReport.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrRegisterReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eireport/oadrRegisterReport.xml");
		OadrRegisterReportType unmarshal = jaxbContext.unmarshal(file, OadrRegisterReportType.class);
		assertEquals("REQ_12345", unmarshal.getRequestID());
		assertEquals("VEN_123", unmarshal.getVenID());
		assertEquals(1, unmarshal.getOadrReport().size());
		assertEquals("PT60M", unmarshal.getOadrReport().get(0).getDuration().getDuration());
		assertEquals("2001-12-17T09:30:47Z", unmarshal.getOadrReport().get(0).getCreatedDateTime().toString());
		assertEquals("0", unmarshal.getOadrReport().get(0).getReportRequestID());
		assertEquals("RS_12345", unmarshal.getOadrReport().get(0).getReportSpecifierID());
		assertEquals(ReportNameEnumeratedType.METADATA_HISTORY_USAGE.value(),
				unmarshal.getOadrReport().get(0).getReportName());
		assertEquals(1, unmarshal.getOadrReport().get(0).getOadrReportDescription().size());
		assertEquals("123", unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getRID());
		assertEquals(2, unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getReportDataSource()
				.getMeterAsset().size());
		assertEquals("12345", unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getReportDataSource()
				.getMeterAsset().get(0).getMrid());
		assertEquals("54321", unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getReportDataSource()
				.getMeterAsset().get(1).getMrid());
		assertEquals(ReportEnumeratedType.USAGE.value(),
				unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getReportType());
		assertEquals(ReadingTypeEnumeratedType.DIRECT_READ.value(),
				unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getReadingType());
		assertEquals("http://www.myprogram.com",
				unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getMarketContext());
		assertEquals("PT15M", unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getOadrSamplingRate()
				.getOadrMaxPeriod());
		assertEquals("PT15M", unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getOadrSamplingRate()
				.getOadrMinPeriod());
		assertEquals(false, unmarshal.getOadrReport().get(0).getOadrReportDescription().get(0).getOadrSamplingRate()
				.isOadrOnChange());

		JAXBElement<? extends ItemBaseType> itemBase = unmarshal.getOadrReport().get(0).getOadrReportDescription()
				.get(0).getItemBase();

		assertTrue(itemBase.getValue() instanceof PowerRealType);
		PowerRealType power = (PowerRealType) itemBase.getValue();
		assertEquals("RealPower", power.getItemDescription());
		assertEquals("W", power.getItemUnits());
		assertEquals(SiScaleCodeType.KILO, power.getSiScaleCode());
		assertEquals(new BigDecimal(60), power.getPowerAttributes().getHertz());
		assertEquals(new BigDecimal(110), power.getPowerAttributes().getVoltage());
		assertEquals(true, power.getPowerAttributes().isAc());
		File file2 = new File("src/test/resources/eireport/genOadrRegisterReport.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrRegisterReport(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();
	}

	@Test
	public void testDifferentReportDescriptionMarshalling() throws Oadr20bMarshalException, Oadr20bUnmarshalException {
		String requestId = "requestId";
		String venId = "venId";
		String reportSpecifierId = "reportSpecifierId";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		long createdTimestamp = System.currentTimeMillis();
		Long start = System.currentTimeMillis();
		String duration = "P1M";
		String marketContextName = "http://oadr.avob.com";

		OadrReportType build = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(reportSpecifierId, reportName, createdTimestamp)
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withCurrencyBase(CurrencyItemDescriptionType.CURRENCY, ISO3AlphaCurrencyCodeContentType.EUR,
								SiScaleCodeType.NONE)
						.withSubject(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
								.addEndDeviceAsset(Arrays.asList(Oadr20bFactory
										.createEndDeviceAssetType(EndDeviceAssertMridType.BASEBOARD_HEATER)))
								.build())
						.withDataSource(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addResourceId("res1").build())
						.withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withCurrencyBase(CurrencyItemDescriptionType.CURRENCY_PER_K_WH,
								ISO3AlphaCurrencyCodeContentType.EUR, SiScaleCodeType.NONE)
						.withSubject(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
								.addEndDeviceAsset(Arrays.asList(Oadr20bFactory
										.createEndDeviceAssetType(EndDeviceAssertMridType.BASEBOARD_HEATER)))
								.build())
						.withDataSource(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addResourceId("res1").build())
						.withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withCurrencyBase(CurrencyItemDescriptionType.CURRENCY_PER_KW,
								ISO3AlphaCurrencyCodeContentType.EUR, SiScaleCodeType.NONE)
						.withSubject(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
								.addEndDeviceAsset(Arrays.asList(Oadr20bFactory
										.createEndDeviceAssetType(EndDeviceAssertMridType.BASEBOARD_HEATER)))
								.build())
						.withDataSource(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addResourceId("res1").build())
						.withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withEnergyApparentBase(SiScaleCodeType.CENTI).withOadrSamplingRate("PT15M", "PT1H", false)
						.build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withEnergyReactiveBase(SiScaleCodeType.GIGA).withOadrSamplingRate("PT15M", "PT1H", false)
						.build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withEnergyRealBase(SiScaleCodeType.KILO).withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withFrequencyBase(SiScaleCodeType.MEGA).withOadrSamplingRate("PT15M", "PT1H", false).build())

				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withPowerApparentBase(SiScaleCodeType.MICRO, BigDecimal.valueOf(1), BigDecimal.valueOf(1),
								false)
						.withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withPowerReactiveBase(SiScaleCodeType.MICRO, BigDecimal.valueOf(1), BigDecimal.valueOf(1),
								false)
						.withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withPowerRealBase(PowerRealUnitType.WATT, SiScaleCodeType.NANO, BigDecimal.valueOf(1),
								BigDecimal.valueOf(1), false)
						.withOadrSamplingRate("PT15M", "PT1H", false).build())

				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withTemperatureBase(TemperatureUnitType.CELSIUS, SiScaleCodeType.NONE)
						.withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withPulseCountBase(18).withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withThermBase(SiScaleCodeType.PICO).withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.READING,
								ReadingTypeEnumeratedType.DIRECT_READ)
						.withVoltageBase(SiScaleCodeType.TERA).withOadrSamplingRate("PT15M", "PT1H", false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.OPERATING_STATE,
								ReadingTypeEnumeratedType.ESTIMATED)
						.withCustomUnitBase("x-custom-mesurement", "x-custom-unit", SiScaleCodeType.DECI)
						.withOadrSamplingRate("PT15M", "PT1H", false).withMarketContext(marketContextName).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder("rid", ReportEnumeratedType.OPERATING_STATE,
								ReadingTypeEnumeratedType.ESTIMATED)
						.withCustomUnitBase("x-custom-mesurement", "x-custom-unit", SiScaleCodeType.DECI)
						.withOadrSamplingRate("PT15M", "PT1H", false).withMarketContext(marketContextName).build())
				.addInterval(Oadr20bEiBuilders
						.newOadr20bReportIntervalTypeBuilder("intervalId", 0L, "PT1H", "rid", 0L, 0F, 0F).build())
				.addInterval(Arrays.asList(Oadr20bEiBuilders
						.newOadr20bReportIntervalTypeBuilder("intervalId", 0L, "PT1H", "rid", 0L, 0F, 0F).build()))
				.withStart(start).withDuration(duration).build();

		OadrRegisterReportType request = Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId, venId)
				.addOadrReport(build).build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}
}
