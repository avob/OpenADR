package com.avob.openadr.model.oadr20b.builders.eireport;

import java.math.BigDecimal;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.BaseUnitType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyType;
import com.avob.openadr.model.oadr20b.oadr.FrequencyType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrSamplingRateType;
import com.avob.openadr.model.oadr20b.oadr.PulseCountType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.oadr.ThermType;
import com.avob.openadr.model.oadr20b.power.EnergyApparentType;
import com.avob.openadr.model.oadr20b.power.EnergyReactiveType;
import com.avob.openadr.model.oadr20b.power.EnergyRealType;
import com.avob.openadr.model.oadr20b.power.PowerApparentType;
import com.avob.openadr.model.oadr20b.power.PowerReactiveType;
import com.avob.openadr.model.oadr20b.power.PowerRealType;
import com.avob.openadr.model.oadr20b.power.VoltageType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;

public class Oadr20bOadrReportDescriptionBuilder {

	private OadrReportDescriptionType reportDescription;

	public Oadr20bOadrReportDescriptionBuilder(String rid, ReportEnumeratedType reportType,
			ReadingTypeEnumeratedType readingType) {
		reportDescription = Oadr20bFactory.createOadrReportDescriptionType();
		reportDescription.setRID(rid);
		reportDescription.setReportType(reportType.value());
		reportDescription.setReadingType(readingType.value());
	}

	public Oadr20bOadrReportDescriptionBuilder withOadrSamplingRate(String minPeriod, String maxPeriod,
			boolean onChange) {
		OadrSamplingRateType oadrSamplingRate = Oadr20bFactory.createOadrSamplingRateType();
		oadrSamplingRate.setOadrMaxPeriod(maxPeriod);
		oadrSamplingRate.setOadrMinPeriod(minPeriod);
		oadrSamplingRate.setOadrOnChange(onChange);
		reportDescription.setOadrSamplingRate(oadrSamplingRate);
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withMarketContext(String marketContextName) {
		reportDescription.setMarketContext(marketContextName);
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withDataSource(EiTargetType eiTarget) {
		reportDescription.setReportDataSource(eiTarget);
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withSubject(EiTargetType eiTarget) {
		reportDescription.setReportSubject(eiTarget);
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withVoltageBase(SiScaleCodeType siscaleCode) {
		VoltageType voltage = Oadr20bFactory.createVoltageType(siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createVoltage(voltage));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withEnergyApparentBase(SiScaleCodeType siscaleCode) {
		EnergyApparentType energyApparentType = Oadr20bFactory.createEnergyApparentType(siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createEnergyApparent(energyApparentType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withEnergyReactiveBase(SiScaleCodeType siscaleCode) {
		EnergyReactiveType energyReactiveType = Oadr20bFactory.createEnergyReactiveType(siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createEnergyReactive(energyReactiveType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withEnergyRealBase(SiScaleCodeType siscaleCode) {
		EnergyRealType createEnergyRealType = Oadr20bFactory.createEnergyRealType(siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createEnergyReal(createEnergyRealType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withPowerApparentBase(SiScaleCodeType siscaleCode, BigDecimal hertz,
			BigDecimal voltage, boolean isAC) {
		PowerApparentType createPowerApparentType = Oadr20bFactory.createPowerApparentType(siscaleCode, hertz, voltage,
				isAC);
		reportDescription.setItemBase(Oadr20bFactory.createPowerApparent(createPowerApparentType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withPowerReactiveBase(SiScaleCodeType siscaleCode, BigDecimal hertz,
			BigDecimal voltage, boolean isAC) {
		PowerReactiveType createPowerReactiveType = Oadr20bFactory.createPowerReactiveType(siscaleCode, hertz, voltage,
				isAC);
		reportDescription.setItemBase(Oadr20bFactory.createPowerReactive(createPowerReactiveType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withPowerRealBase(PowerRealUnitType unit, SiScaleCodeType siscaleCode,
			BigDecimal hertz, BigDecimal voltage, boolean isAC) {
		PowerRealType createPowerRealType = Oadr20bFactory.createPowerRealType(unit, siscaleCode, hertz, voltage, isAC);
		reportDescription.setItemBase(Oadr20bFactory.createPowerReal(createPowerRealType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withCustomUnitBase(String description, String units,
			SiScaleCodeType siscaleCode) {
		BaseUnitType createBaseUnitType = Oadr20bFactory.createBaseUnitType(description, units, siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createCustomUnit(createBaseUnitType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withCurrencyBase(CurrencyItemDescriptionType description,
			ISO3AlphaCurrencyCodeContentType units, SiScaleCodeType siscaleCode) {
		CurrencyType createCurrencyType = Oadr20bFactory.createCurrencyType(description, units, siscaleCode);

		// TODO bzanni: oadr_20b.xsd createCurrencyPerThm WTF ???
		switch (description) {
		case CURRENCY:
			reportDescription.setItemBase(Oadr20bFactory.createCurrency(createCurrencyType));
			break;
		case CURRENCY_PER_KW:
			reportDescription.setItemBase(Oadr20bFactory.createCurrencyPerKW(createCurrencyType));
			break;
		case CURRENCY_PER_K_WH:
			reportDescription.setItemBase(Oadr20bFactory.createCurrencyPerKWh(createCurrencyType));
			break;
		default:
			break;
		}
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withFrequencyBase(SiScaleCodeType siscaleCode) {
		FrequencyType createFrequencyType = Oadr20bFactory.createFrequencyType(siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createFrequency(createFrequencyType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withThermBase(SiScaleCodeType siscaleCode) {
		ThermType createThermType = Oadr20bFactory.createThermType(siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createTherm(createThermType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withTemperatureBase(TemperatureUnitType units,
			SiScaleCodeType siscaleCode) {
		TemperatureType createTemperatureType = Oadr20bFactory.createTemperatureType(units, siscaleCode);
		reportDescription.setItemBase(Oadr20bFactory.createTemperature(createTemperatureType));
		return this;
	}

	public Oadr20bOadrReportDescriptionBuilder withPulseCountBase(float pulseFactor) {
		PulseCountType createPulseCountType = Oadr20bFactory.createPulseCountType(pulseFactor);
		reportDescription.setItemBase(Oadr20bFactory.createPulseCount(createPulseCountType));
		return this;
	}

	public OadrReportDescriptionType build() {
		return reportDescription;
	}

}
