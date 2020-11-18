package com.avob.openadr.server.oadr20b.vtn.models;

import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.oadr.BaseUnitType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyType;
import com.avob.openadr.model.oadr20b.oadr.CurrentType;
import com.avob.openadr.model.oadr20b.oadr.FrequencyType;
import com.avob.openadr.model.oadr20b.oadr.OadrGBItemBase;
import com.avob.openadr.model.oadr20b.oadr.PulseCountType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.ThermType;
import com.avob.openadr.model.oadr20b.power.EnergyApparentType;
import com.avob.openadr.model.oadr20b.power.EnergyReactiveType;
import com.avob.openadr.model.oadr20b.power.EnergyRealType;
import com.avob.openadr.model.oadr20b.power.PowerApparentType;
import com.avob.openadr.model.oadr20b.power.PowerReactiveType;
import com.avob.openadr.model.oadr20b.power.PowerRealType;
import com.avob.openadr.model.oadr20b.power.VoltageType;

public enum ItemBaseXmlTypeEnum {

	VOLTAGE("VoltageType", VoltageType.class), ENERGY_APPARENT("EnergyApparentType", EnergyApparentType.class),
	ENERGY_REAL("EnergyRealType", EnergyRealType.class),
	ENERGY_REACTIVE("EnergyReactiveType", EnergyReactiveType.class),
	POWER_APPARENT("PowerApparentType", PowerApparentType.class), POWER_REAL("PowerRealType", PowerRealType.class),
	POWER_REACTIVE("PowerReactiveType", PowerReactiveType.class), BASE_UNIT("BaseUnitType", BaseUnitType.class),
	CURRENT("CurrentType", CurrentType.class), CURRENCY("CurrencyType", CurrencyType.class),
	FREQUENCY("FrequencyType", FrequencyType.class), THERM("ThermType", ThermType.class),
	TEMPERATURE("TemperatureType", TemperatureType.class), PULSE_COUNT("PulseCountType", PulseCountType.class),
	OADR_GBITEMBASE("oadrGBItemBase", OadrGBItemBase.class);

	private String xmlType;
	private Class<? extends ItemBaseType> klass;

	private ItemBaseXmlTypeEnum(String xmlType, Class<? extends ItemBaseType> klass) {
		this.xmlType = xmlType;
		this.klass = klass;
	}

	public String getXmlType() {
		return xmlType;
	}

	public Class<? extends ItemBaseType> getKlass() {
		return klass;
	}

	public static ItemBaseXmlTypeEnum fromXmlType(String xmlType) {
		for (ItemBaseXmlTypeEnum el : ItemBaseXmlTypeEnum.values()) {
			if (el.getXmlType().equals(xmlType)) {
				return el;
			}
		}
		throw new IllegalStateException("xmlType: " + xmlType + " is not a known xmlType");
	}

}
