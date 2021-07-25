package com.avob.openadr.server.oadr20b.vtn.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.eipayload.SignalTargetAllowedEndDeviceType;
import com.avob.openadr.model.oadr20b.builders.eireport.PowerRealUnitType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalNameEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventSignalTypeEnum;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitId;
import com.avob.openadr.server.common.vtn.service.KnownReportService;
import com.avob.openadr.server.common.vtn.service.KnownSignalService;
import com.avob.openadr.server.common.vtn.service.KnownUnitService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiServiceUtils;

@RestController
@RequestMapping("/Definition")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER') or hasRole('ROLE_DRPROGRAM')")
public class DefinitionController {

	@Resource
	private KnownReportService knownReportService;

	@Resource
	private KnownSignalService knownSignalService;

	@Resource
	private KnownUnitService knownUnitService;

	@RequestMapping(value = "/signal/name", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findSignalName() {

		List<String> findSignalName = knownSignalService.findSignalName();

		Arrays.asList(DemandResponseEventSignalNameEnum.values()).forEach(s -> {
			if (!findSignalName.contains(s.getLabel())) {
				findSignalName.add(s.getLabel());
			}
		});

		return findSignalName;
	}

	@RequestMapping(value = "/signal/type", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findSignalType() {

		return Arrays.asList(DemandResponseEventSignalTypeEnum.values()).stream()
				.map(DemandResponseEventSignalTypeEnum::getLabel).collect(Collectors.toList());
	}

	@RequestMapping(value = "/report/name", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findReportName() {

		List<String> findReportName = knownReportService.findReportName();

		Arrays.asList(ReportNameEnumeratedType.values()).forEach(s -> {
			if (!findReportName.contains(s.name())) {
				findReportName.add(s.name());
			}
		});

		return findReportName;
	}

	@RequestMapping(value = "/report/type", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findReportType(@RequestParam("reportName") String reportName) {

		return Arrays.asList(ReportEnumeratedType.values()).stream().map(ReportEnumeratedType::name)
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/unit/itemDescription", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findUnitItemDescription() throws Oadr20bMarshalException {

		List<String> findItemDescription = knownUnitService.findItemDescription();

		genericUnit().stream().map(u -> u.getKnownUnitId().getItemDescription()).forEach(i -> {
			if (!findItemDescription.contains(i)) {
				findItemDescription.add(i);
			}
		});

		return findItemDescription;
	}

	@RequestMapping(value = "/unit/itemUnits", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findUnitItemUnits(@RequestParam(value = "description", required = false) String description)
			throws Oadr20bMarshalException {

		List<String> findItemUnits = knownUnitService.findItemUnits(description);

		List<KnownUnit> units = genericUnitOther();
		units.addAll(genericUnitCurrency());

		units.stream().filter(u -> description == null || description.equals(u.getKnownUnitId().getItemDescription()))
				.map(u -> u.getKnownUnitId().getItemUnits()).forEach(u -> {
					if (!findItemUnits.contains(u)) {
						findItemUnits.add(u);
					}

				});

		return findItemUnits;
	}

	@RequestMapping(value = "/unit/siScaleCode", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findUnitSiScaleCode() {

		return Arrays.asList(SiScaleCodeType.values()).stream().map(SiScaleCodeType::value)
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/target/endDeviceAsset", method = RequestMethod.GET)
	@ResponseBody
	public List<String> findEndDeviceAsset() {

		return Arrays.asList(SignalTargetAllowedEndDeviceType.values()).stream()
				.map(SignalTargetAllowedEndDeviceType::label).collect(Collectors.toList());
	}

	private List<KnownUnit> genericUnit() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();

		units.addAll(genericUnitCurrency());
		units.addAll(genericUnitOther());

		return units;

	}

	private List<KnownUnit> genericUnitCurrency() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();

		units.addAll(getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY));
		units.addAll(getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_K_WH));
		units.addAll(getCurrencyUnit(CurrencyItemDescriptionType.CURRENCY_PER_KW));

		return units;

	}

	private List<KnownUnit> genericUnitOther() throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();

		units.add(from(Oadr20bFactory.createCurrentType(SiScaleCodeType.NONE)));

		units.addAll(getEnergyUnit());
		units.addAll(getPowerUnit());

		units.add(from(Oadr20bFactory.createFrequencyType(SiScaleCodeType.NONE)));

		units.add(from(Oadr20bFactory.createPulseCountType(0f)));

		units.add(from(Oadr20bFactory.createTemperatureType(TemperatureUnitType.CELSIUS, SiScaleCodeType.NONE)));

		units.add(from(Oadr20bFactory.createTemperatureType(TemperatureUnitType.FAHRENHEIT, SiScaleCodeType.NONE)));

		units.add(from(Oadr20bFactory.createThermType(SiScaleCodeType.NONE)));

		return units;

	}

	private KnownUnit from(ItemBaseType type) throws Oadr20bMarshalException {

		ItemBase createItemBase = Oadr20bVTNEiServiceUtils.createItemBase(type, type.getClass());

		KnownUnit unit = new KnownUnit();
		KnownUnitId knownUnitId = new KnownUnitId();

		knownUnitId.setItemDescription(createItemBase.getItemDescription());
		knownUnitId.setItemUnits(createItemBase.getItemUnits());
		knownUnitId.setXmlType(createItemBase.getXmlType());
		knownUnitId.setSiScaleCode(createItemBase.getSiScaleCode());
		unit.setKnownUnitId(knownUnitId);
		unit.setAttributes(createItemBase.getAttributes());
		return unit;

	}

	private List<KnownUnit> getCurrencyUnit(CurrencyItemDescriptionType type) throws Oadr20bMarshalException {
		List<KnownUnit> units = new ArrayList<>();
		for (ISO3AlphaCurrencyCodeContentType c : ISO3AlphaCurrencyCodeContentType.values()) {
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

}
