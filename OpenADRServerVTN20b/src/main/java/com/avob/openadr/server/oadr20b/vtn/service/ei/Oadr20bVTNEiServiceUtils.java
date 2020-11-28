package com.avob.openadr.server.oadr20b.vtn.service.ei;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.atom.FeedType;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.Oadr20bEiTargetTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.BaseUnitType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyType;
import com.avob.openadr.model.oadr20b.oadr.CurrentType;
import com.avob.openadr.model.oadr20b.oadr.FrequencyType;
import com.avob.openadr.model.oadr20b.oadr.OadrGBItemBase;
import com.avob.openadr.model.oadr20b.oadr.PulseCountType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.oadr.ThermType;
import com.avob.openadr.model.oadr20b.power.EndDeviceAssetType;
import com.avob.openadr.model.oadr20b.power.EnergyApparentType;
import com.avob.openadr.model.oadr20b.power.EnergyReactiveType;
import com.avob.openadr.model.oadr20b.power.EnergyRealType;
import com.avob.openadr.model.oadr20b.power.VoltageType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.oadr20b.vtn.models.ItemBaseXmlTypeEnum;

public class Oadr20bVTNEiServiceUtils {

	public static EiTargetType createEiTarget(List<Target> targets) {
		Oadr20bEiTargetTypeBuilder newOadr20bEiTargetTypeBuilder = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder();
		for (Target target : targets) {
			switch (target.getTargetType()) {
			case GROUP:
				newOadr20bEiTargetTypeBuilder.addGroupId(target.getTargetId());
				break;
			case VEN:
				newOadr20bEiTargetTypeBuilder.addVenId(target.getTargetId());
				break;
			case ENDDEVICE_ASSET:
				EndDeviceAssetType endDeviceAssetType = new EndDeviceAssetType();
				endDeviceAssetType.setMrid(target.getTargetId());
				newOadr20bEiTargetTypeBuilder.addEndDeviceAsset(endDeviceAssetType);
				break;
			default:
				break;

			}

		}
		return newOadr20bEiTargetTypeBuilder.build();
	}

	public static List<Target> createTargetList(EiTargetType targets) {

		List<Target> list = new ArrayList<>();

//		targets.getAggregatedPnode();
//		targets.getGroupName();
//		targets.getMeterAsset();
//		targets.getPartyID();
//		targets.getPnode();
//		targets.getResourceID();
//		targets.getServiceArea();
//		targets.getServiceDeliveryPoint();
//		targets.getServiceLocation();
//		targets.getTransportInterface();

		targets.getEndDeviceAsset().forEach(el -> {
			list.add(new Target(TargetTypeEnum.ENDDEVICE_ASSET, el.getMrid()));
		});
		targets.getGroupID().forEach(el -> {
			list.add(new Target(TargetTypeEnum.GROUP, el));
		});
		targets.getVenID().forEach(el -> {
			list.add(new Target(TargetTypeEnum.VEN, el));
		});

		return list;
	}

	public static JAXBElement<? extends ItemBaseType> createItemBase(ItemBase itemBase) {

		if(itemBase.getXmlType() == null) {
			return null;
		}
		
		SiScaleCodeType siscaleCode = SiScaleCodeType.fromValue(itemBase.getSiScaleCode());

		ItemBaseXmlTypeEnum xmlType = ItemBaseXmlTypeEnum.fromXmlType(itemBase.getXmlType());

		if (VoltageType.class.equals(xmlType.getKlass())) {
			VoltageType createVoltageType = Oadr20bFactory.createVoltageType(siscaleCode);
			return Oadr20bFactory.createVoltage(createVoltageType);

		} else if (EnergyApparentType.class.equals(xmlType.getKlass())) {
			EnergyApparentType createEnergyApparentType = Oadr20bFactory.createEnergyApparentType(siscaleCode);
			return Oadr20bFactory.createEnergyApparent(createEnergyApparentType);

		} else if (EnergyRealType.class.equals(xmlType.getKlass())) {
			EnergyRealType createEnergyRealType = Oadr20bFactory.createEnergyRealType(siscaleCode);
			return Oadr20bFactory.createEnergyReal(createEnergyRealType);

		} else if (EnergyReactiveType.class.equals(xmlType.getKlass())) {
			EnergyReactiveType createEnergyReactiveType = Oadr20bFactory.createEnergyReactiveType(siscaleCode);
			return Oadr20bFactory.createEnergyReactive(createEnergyReactiveType);

		} else if (BaseUnitType.class.equals(xmlType.getKlass())) {
			BaseUnitType createBaseUnitType = Oadr20bFactory.createBaseUnitType(itemBase.getItemDescription(),
					itemBase.getItemUnits(), siscaleCode);
			return Oadr20bFactory.createItemBase(createBaseUnitType);

		} else if (CurrentType.class.equals(xmlType.getKlass())) {
			CurrentType createCurrentType = Oadr20bFactory.createCurrentType(siscaleCode);
			return Oadr20bFactory.createCurrent(createCurrentType);

		} else if (CurrencyType.class.equals(xmlType.getKlass())) {
			CurrencyItemDescriptionType description = CurrencyItemDescriptionType
					.fromValue(itemBase.getItemDescription());
			ISO3AlphaCurrencyCodeContentType units = ISO3AlphaCurrencyCodeContentType
					.fromValue(itemBase.getItemUnits());
			CurrencyType createCurrencyType = Oadr20bFactory.createCurrencyType(description, units, siscaleCode);
			return Oadr20bFactory.createCurrency(createCurrencyType);

		} else if (FrequencyType.class.equals(xmlType.getKlass())) {
			FrequencyType createFrequencyType = Oadr20bFactory.createFrequencyType(siscaleCode);
			return Oadr20bFactory.createFrequency(createFrequencyType);

		} else if (ThermType.class.equals(xmlType.getKlass())) {
			ThermType createThermType = Oadr20bFactory.createThermType(siscaleCode);
			return Oadr20bFactory.createTherm(createThermType);

		} else if (TemperatureType.class.equals(xmlType.getKlass())) {
			TemperatureUnitType units = TemperatureUnitType.fromValue(itemBase.getItemUnits());
			TemperatureType createTemperatureType = Oadr20bFactory.createTemperatureType(units, siscaleCode);
			return Oadr20bFactory.createTemperature(createTemperatureType);

		} else if (PulseCountType.class.equals(xmlType.getKlass())) {
			float pulseFactor = Float.valueOf(itemBase.getItemDescription());
			PulseCountType createPulseCountType = Oadr20bFactory.createPulseCountType(pulseFactor);
			return Oadr20bFactory.createPulseCount(createPulseCountType);

		} else if (OadrGBItemBase.class.equals(xmlType.getKlass())) {
			FeedType feed = new FeedType();
			feed.setBase(itemBase.getItemDescription());
			feed.setLang(itemBase.getItemUnits());
			OadrGBItemBase createGBItemBaseType = Oadr20bFactory.createGBItemBaseType(feed);
			return Oadr20bFactory.createItemBase(createGBItemBaseType);

		} else {
			return null;
		}

	}

}
