package com.avob.openadr.model.oadr20b.builders.eipayload;

import java.util.Arrays;
import java.util.Collection;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.emix.ServiceAreaType;
import com.avob.openadr.model.oadr20b.power.AggregatedPnodeType;
import com.avob.openadr.model.oadr20b.power.EndDeviceAssetType;
import com.avob.openadr.model.oadr20b.power.MeterAssetType;
import com.avob.openadr.model.oadr20b.power.PnodeType;
import com.avob.openadr.model.oadr20b.power.ServiceDeliveryPointType;
import com.avob.openadr.model.oadr20b.power.ServiceLocationType;
import com.avob.openadr.model.oadr20b.power.TransportInterfaceType;

public class Oadr20bEiTargetTypeBuilder {

    private EiTargetType eiTarget;

    public Oadr20bEiTargetTypeBuilder() {
        eiTarget = Oadr20bFactory.createEiTargetType();
    }

    public Oadr20bEiTargetTypeBuilder addTransportInterface(Collection<TransportInterfaceType> transportInterfaceType) {
        eiTarget.getTransportInterface().addAll(transportInterfaceType);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addTransportInterface(TransportInterfaceType transportInterfaceType) {
        this.addTransportInterface(Arrays.asList(transportInterfaceType));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addServiceLocation(Collection<ServiceLocationType> serviceLocationType) {
        eiTarget.getServiceLocation().addAll(serviceLocationType);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addServiceLocation(ServiceLocationType serviceLocationType) {
        this.addServiceLocation(Arrays.asList(serviceLocationType));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addServiceDeliveryPoint(
            Collection<ServiceDeliveryPointType> serviceDeliveryPointType) {
        eiTarget.getServiceDeliveryPoint().addAll(serviceDeliveryPointType);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addServiceDeliveryPoint(ServiceDeliveryPointType serviceDeliveryPointType) {
        this.addServiceDeliveryPoint(Arrays.asList(serviceDeliveryPointType));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addServiceArea(Collection<ServiceAreaType> serviceAreaType) {
        eiTarget.getServiceArea().addAll(serviceAreaType);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addServiceArea(ServiceAreaType serviceAreaType) {
        this.addServiceArea(Arrays.asList(serviceAreaType));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addPnode(Collection<PnodeType> pnode) {
        eiTarget.getPnode().addAll(pnode);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addPnode(PnodeType pnode) {
        this.addPnode(Arrays.asList(pnode));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addMeterAsset(Collection<MeterAssetType> meterAssetType) {
        eiTarget.getMeterAsset().addAll(meterAssetType);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addMeterAsset(MeterAssetType meterAssetType) {
        this.addMeterAsset(Arrays.asList(meterAssetType));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addGroupName(Collection<String> groupName) {
        eiTarget.getGroupName().addAll(groupName);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addGroupName(String groupName) {
        this.addGroupName(Arrays.asList(groupName));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addEndDeviceAsset(Collection<EndDeviceAssetType> endDeviceAsset) {
        eiTarget.getEndDeviceAsset().addAll(endDeviceAsset);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addEndDeviceAsset(EndDeviceAssetType endDeviceAsset) {
        this.addEndDeviceAsset(Arrays.asList(endDeviceAsset));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addAggregatedPnode(Collection<AggregatedPnodeType> aggregatedPnode) {
        eiTarget.getAggregatedPnode().addAll(aggregatedPnode);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addAggregatedPnode(AggregatedPnodeType aggregatedPnode) {
        this.addAggregatedPnode(Arrays.asList(aggregatedPnode));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addGroupId(String group) {
        this.addGroupId(Arrays.asList(group));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addGroupId(Collection<String> groups) {
        eiTarget.getGroupID().addAll(groups);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addResourceId(String resource) {
        this.addResourceId(Arrays.asList(resource));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addResourceId(Collection<String> resources) {
        eiTarget.getResourceID().addAll(resources);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addVenId(String ven) {
        this.addVenId(Arrays.asList(ven));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addVenId(Collection<String> vens) {
        eiTarget.getVenID().addAll(vens);
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addPartyId(String party) {
        this.addPartyId(Arrays.asList(party));
        return this;
    }

    public Oadr20bEiTargetTypeBuilder addPartyId(Collection<String> parties) {
        eiTarget.getPartyID().addAll(parties);
        return this;
    }

    public EiTargetType build() {
        return eiTarget;
    }

}
