package com.avob.openadr.server.oadr20b.vtn.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.eireport.PowerRealUnitType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalTypeEnum;
import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportId;
import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalId;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitId;
import com.avob.openadr.server.common.vtn.service.KnownReportService;
import com.avob.openadr.server.common.vtn.service.KnownSignalService;
import com.avob.openadr.server.common.vtn.service.KnownUnitService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiServiceUtils;

@Profile({ "fake-data-known" })
@Component
public class InitKnownController {

	@Resource
	private KnownUnitService knownUnitService;

	@Resource
	private KnownSignalService knownSignalService;

	@Resource
	private KnownReportService knownReportService;

	private List<ISO3AlphaCurrencyCodeContentType> supportedCurrencies = Arrays
			.asList(ISO3AlphaCurrencyCodeContentType.EUR, ISO3AlphaCurrencyCodeContentType.USD);

	@PostConstruct
	public void init() throws Oadr20bMarshalException {

		initKnownUnit();

		initKnownSignal();

		initKnownReport();
	}

	private List<KnownUnit> getCurrencyUnit(CurrencyItemDescriptionType type) throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();
		for (ISO3AlphaCurrencyCodeContentType c : supportedCurrencies) {
			units.add(from(Oadr20bFactory.createCurrencyType(type, c, SiScaleCodeType.NONE)));
		}

		return units;
	}

	private List<KnownUnit> getRealEnergyUnit() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();
		units.add(from(Oadr20bFactory.createEnergyRealType(SiScaleCodeType.NONE)));
		return units;
	}

	private List<KnownUnit> getEnergyUnit() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();
		units.addAll(getRealEnergyUnit());

		units.add(from(Oadr20bFactory.createEnergyReactiveType(SiScaleCodeType.NONE)));

		units.add(from(Oadr20bFactory.createEnergyApparentType(SiScaleCodeType.NONE)));

		return units;
	}

	private List<KnownUnit> getRealPowerUnit() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();
		units.add(from(Oadr20bFactory.createPowerRealType(PowerRealUnitType.JOULES_PER_SECOND, SiScaleCodeType.NONE,
				null, null, false)));
		return units;
	}

	private List<KnownUnit> getPowerUnit() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();
		units.addAll(getRealPowerUnit());

		units.add(from(Oadr20bFactory.createPowerReactiveType(SiScaleCodeType.NONE, null, null, false)));

		units.add(from(Oadr20bFactory.createPowerApparentType(SiScaleCodeType.NONE, null, null, false)));

		return units;
	}

	private void initKnownUnit() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();

		units.addAll(getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY));
		units.addAll(getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_K_WH));
		units.addAll(getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_KW));

		units.add(from(Oadr20bFactory.createCurrentType(SiScaleCodeType.NONE)));

		units.addAll(getEnergyUnit());
		units.addAll(getPowerUnit());

		units.add(from(Oadr20bFactory.createFrequencyType(SiScaleCodeType.NONE)));

		units.add(from(Oadr20bFactory.createPulseCountType(0f)));

		units.add(from(Oadr20bFactory.createTemperatureType(TemperatureUnitType.CELSIUS, SiScaleCodeType.NONE)));

		units.add(from(Oadr20bFactory.createTemperatureType(TemperatureUnitType.FAHRENHEIT, SiScaleCodeType.NONE)));

		units.add(from(Oadr20bFactory.createThermType(SiScaleCodeType.NONE)));

		knownUnitService.save(units);

	}

	private KnownUnit from(ItemBaseType type) throws Oadr20bMarshalException {

		ItemBase createItemBase = Oadr20bVTNEiServiceUtils.createItemBase(type, type.getClass());

		KnownUnit unit = new KnownUnit();
		KnownUnitId knownUnitId = new KnownUnitId();

		knownUnitId.setItemDescription(createItemBase.getItemDescription());
		knownUnitId.setItemUnits(createItemBase.getItemUnits());
		knownUnitId.setXmlType(createItemBase.getXmlType());
		unit.setKnownUnitId(knownUnitId);
		unit.setAttributes(createItemBase.getAttributes());
		return unit;

	}

	private void initKnownReport() throws Oadr20bMarshalException {
		List<KnownUnit> unit = null;
		List<KnownReport> reports = new ArrayList<>();

		unit = getRealPowerUnit();
		unit.addAll(getRealEnergyUnit());
		reports.addAll(from("TELEMETRY_USAGE", ReportEnumeratedType.USAGE.value(),
				ReadingTypeEnumeratedType.DIRECT_READ.value(), PayloadFloatType.class.getSimpleName(), unit));

		unit = null;
		reports.addAll(from("TELEMETRY_STATUS", ReportEnumeratedType.X_RESOURCE_STATUS.value(),
				ReadingTypeEnumeratedType.X_NOT_APPLICABLE.value(), OadrPayloadResourceStatusType.class.getSimpleName(),
				unit));

		unit = getRealPowerUnit();
		unit.addAll(getRealEnergyUnit());
		reports.addAll(from("HISTORY_USAGE", ReportEnumeratedType.USAGE.value(),
				ReadingTypeEnumeratedType.DIRECT_READ.value(), PayloadFloatType.class.getSimpleName(), unit));

		unit = getRealPowerUnit();
		unit.addAll(getRealEnergyUnit());
		reports.addAll(from("HISTORY_GREENBUTTON", ReportEnumeratedType.USAGE.value(),
				ReadingTypeEnumeratedType.DIRECT_READ.value(), PayloadFloatType.class.getSimpleName(), unit));

		knownReportService.save(reports);
	}

	private List<KnownReport> from(String reportName, String reportType, String readingType, String payloadBase,
			List<KnownUnit> units) {
		return units != null ? units.stream().map(u -> {
			KnownReport knownReport = new KnownReport();
			KnownReportId knownReportId = new KnownReportId();
			knownReport.setKnownReportId(knownReportId);
			knownReport.getKnownReportId().setReportName(reportName);
			knownReport.getKnownReportId().setReportType(reportType);
			knownReport.getKnownReportId().setReadingType(readingType);
			knownReport.getKnownReportId().setPayloadBase(payloadBase);
			knownReport.getKnownReportId().setUnit(u);
			return knownReport;
		}).collect(Collectors.toList()) : new ArrayList<>();

	}

	private void initKnownSignal() throws Oadr20bMarshalException {

		List<KnownSignal> signals = new ArrayList<>();

		signals.addAll(from(DemandResponseEventSignalNameEnum.SIMPLE.getLabel(),
				DemandResponseEventSignalTypeEnum.LEVEL, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.ELECTRICITY_PRICE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE,
				getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_K_WH)));

		signals.addAll(from(DemandResponseEventSignalNameEnum.ELECTRICITY_PRICE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE_RELATIVE,
				getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_K_WH)));

		signals.addAll(from(DemandResponseEventSignalNameEnum.ELECTRICITY_PRICE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE_MULTIPLIER, null));

		signals.addAll(
				from(DemandResponseEventSignalNameEnum.ENERGY_PRICE.getLabel(), DemandResponseEventSignalTypeEnum.PRICE,
						getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_K_WH)));

		signals.addAll(from(DemandResponseEventSignalNameEnum.ENERGY_PRICE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE_RELATIVE,
				getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_K_WH)));

		signals.addAll(from(DemandResponseEventSignalNameEnum.ENERGY_PRICE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE_MULTIPLIER, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.DEMAND_CHARGE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE, getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_KW)));

		signals.addAll(from(DemandResponseEventSignalNameEnum.DEMAND_CHARGE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE_RELATIVE,
				getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_KW)));

		signals.addAll(from(DemandResponseEventSignalNameEnum.DEMAND_CHARGE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE_MULTIPLIER, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.BID_PRICE.getLabel(),
				DemandResponseEventSignalTypeEnum.PRICE, getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY)));

		signals.addAll(from(DemandResponseEventSignalNameEnum.BID_LOAD.getLabel(),
				DemandResponseEventSignalTypeEnum.SETPOINT, getPowerUnit()));

		signals.addAll(from(DemandResponseEventSignalNameEnum.BID_ENERGY.getLabel(),
				DemandResponseEventSignalTypeEnum.SETPOINT, getEnergyUnit()));

		signals.addAll(from(DemandResponseEventSignalNameEnum.CHARGE_STATE.getLabel(),
				DemandResponseEventSignalTypeEnum.SETPOINT, getEnergyUnit()));

		signals.addAll(from(DemandResponseEventSignalNameEnum.CHARGE_STATE.getLabel(),
				DemandResponseEventSignalTypeEnum.DELTA, getEnergyUnit()));

		signals.addAll(from(DemandResponseEventSignalNameEnum.CHARGE_STATE.getLabel(),
				DemandResponseEventSignalTypeEnum.MULTIPLIER, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_DISPATCH.getLabel(),
				DemandResponseEventSignalTypeEnum.SETPOINT, getPowerUnit()));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_DISPATCH.getLabel(),
				DemandResponseEventSignalTypeEnum.DELTA, getPowerUnit()));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_DISPATCH.getLabel(),
				DemandResponseEventSignalTypeEnum.MULTIPLIER, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_DISPATCH.getLabel(),
				DemandResponseEventSignalTypeEnum.LEVEL, getPowerUnit()));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_CONTROL.getLabel(),
				DemandResponseEventSignalTypeEnum.X_LOAD_CONTROL_CAPACITY, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_CONTROL.getLabel(),
				DemandResponseEventSignalTypeEnum.X_LOAD_CONTROL_LEVEL_OFFSET, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_CONTROL.getLabel(),
				DemandResponseEventSignalTypeEnum.X_LOAD_CONTROL_PERCENT_OFFSET, null));

		signals.addAll(from(DemandResponseEventSignalNameEnum.LOAD_CONTROL.getLabel(),
				DemandResponseEventSignalTypeEnum.X_LOAD_CONTROL_SETPOINT, null));

		knownSignalService.save(signals);

	}

	private List<KnownSignal> from(String signalName, DemandResponseEventSignalTypeEnum signalType,
			List<KnownUnit> units) {
		return units != null ? units.stream().map(u -> {
			KnownSignal knownSignal = new KnownSignal();
			KnownSignalId knownSignalId = new KnownSignalId();
			knownSignalId.setSignalName(signalName);
			knownSignalId.setSignalType(signalType);
			knownSignalId.setUnit(u);
			knownSignal.setKnownSignalId(knownSignalId);

			return knownSignal;
		}).collect(Collectors.toList()) : new ArrayList<>();

	}

}
