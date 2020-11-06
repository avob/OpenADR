package com.avob.openadr.model.oadr20b.eipayload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.EndDeviceAssertMridType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.emix.ServiceAreaType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.power.AggregatedPnodeType;
import com.avob.openadr.model.oadr20b.power.EndDeviceAssetType;
import com.avob.openadr.model.oadr20b.power.MeterAssetType;
import com.avob.openadr.model.oadr20b.power.PnodeType;
import com.avob.openadr.model.oadr20b.power.ServiceDeliveryPointType;
import com.avob.openadr.model.oadr20b.power.ServiceLocationType;
import com.avob.openadr.model.oadr20b.power.TransportInterfaceType;

public class Oadr20bEiTargetTypeBuilderTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bEiTargetTypeBuilderTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	private void checkMarshalling(EiTargetType build) throws Oadr20bMarshalException {
		jaxbContext.marshal(Oadr20bFactory.createEiTarget(build));
	}

	@Test
	public void test() throws Oadr20bMarshalException {
		EiTargetType build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().build();
		checkMarshalling(build);

		AggregatedPnodeType aggregatedPnode = Oadr20bFactory.createAggregatedPnodeType("aggregatedPnode");
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addAggregatedPnode(aggregatedPnode).build();
		checkMarshalling(build);

		EndDeviceAssetType endDeviceAsset = Oadr20bFactory
				.createEndDeviceAssetType(EndDeviceAssertMridType.EXTERIOR_LIGHTING);
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addEndDeviceAsset(endDeviceAsset).build();
		checkMarshalling(build);

		String groupName = "group";
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupName(groupName).build();
		checkMarshalling(build);

		String groupId = "groupId";
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId(groupId).build();
		checkMarshalling(build);

		MeterAssetType meterAssetType = Oadr20bFactory.createMeterAssetType("mrid");
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addMeterAsset(meterAssetType).build();
		checkMarshalling(build);

		String party = "party";
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addPartyId(party).build();
		checkMarshalling(build);

		PnodeType pnode = Oadr20bFactory.createPnodeType("pnode");
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addPnode(pnode).build();
		checkMarshalling(build);

		String resource = "resourceId";
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addResourceId(resource).build();
		checkMarshalling(build);

		List<Double> pos = new ArrayList<Double>();
		pos.add(Double.valueOf(12));
		ServiceAreaType serviceAreaType = Oadr20bFactory.createServiceAreaType("featureCollectionId", "locationId",
				pos);
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addServiceArea(serviceAreaType).build();
		checkMarshalling(build);

		ServiceDeliveryPointType serviceDeliveryPointType = Oadr20bFactory.createServiceDeliveryPointType("node");
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addServiceDeliveryPoint(serviceDeliveryPointType)
				.build();
		checkMarshalling(build);

		ServiceLocationType serviceLocationType = Oadr20bFactory.createServiceLocationType("featureCollectionId",
				"locationId", Arrays.asList(12D, 14D, 16D, 18D));
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addServiceLocation(serviceLocationType).build();
		checkMarshalling(build);

		TransportInterfaceType transportInterfaceType = Oadr20bFactory.createTransportInterfaceType("receiptPointId",
				"deliveryPointId");
		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addTransportInterface(transportInterfaceType).build();
		checkMarshalling(build);

		build = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId("venId").build();
		checkMarshalling(build);
	}
}
