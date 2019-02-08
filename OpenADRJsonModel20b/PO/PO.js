var PO_Module_Factory = function () {
  var PO = {
    name: 'PO',
    defaultElementNamespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110',
    typeInfos: [{
        localName: 'IconType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'iconType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'value',
            type: 'value'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrSignedObject',
        typeName: null,
        propertyInfos: [{
            name: 'oadrDistributeEvent',
            required: true,
            elementName: {
              localPart: 'oadrDistributeEvent',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrDistributeEventType'
          }, {
            name: 'oadrCreatedEvent',
            required: true,
            elementName: {
              localPart: 'oadrCreatedEvent',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreatedEventType'
          }, {
            name: 'oadrRequestEvent',
            required: true,
            elementName: {
              localPart: 'oadrRequestEvent',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrRequestEventType'
          }, {
            name: 'oadrResponse',
            required: true,
            elementName: {
              localPart: 'oadrResponse',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrResponseType'
          }, {
            name: 'oadrCancelOpt',
            required: true,
            elementName: {
              localPart: 'oadrCancelOpt',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCancelOptType'
          }, {
            name: 'oadrCanceledOpt',
            required: true,
            elementName: {
              localPart: 'oadrCanceledOpt',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCanceledOptType'
          }, {
            name: 'oadrCreateOpt',
            required: true,
            elementName: {
              localPart: 'oadrCreateOpt',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreateOptType'
          }, {
            name: 'oadrCreatedOpt',
            required: true,
            elementName: {
              localPart: 'oadrCreatedOpt',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreatedOptType'
          }, {
            name: 'oadrCancelReport',
            required: true,
            elementName: {
              localPart: 'oadrCancelReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCancelReportType'
          }, {
            name: 'oadrCanceledReport',
            required: true,
            elementName: {
              localPart: 'oadrCanceledReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCanceledReportType'
          }, {
            name: 'oadrCreateReport',
            required: true,
            elementName: {
              localPart: 'oadrCreateReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreateReportType'
          }, {
            name: 'oadrCreatedReport',
            required: true,
            elementName: {
              localPart: 'oadrCreatedReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreatedReportType'
          }, {
            name: 'oadrRegisterReport',
            required: true,
            elementName: {
              localPart: 'oadrRegisterReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrRegisterReportType'
          }, {
            name: 'oadrRegisteredReport',
            required: true,
            elementName: {
              localPart: 'oadrRegisteredReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrRegisteredReportType'
          }, {
            name: 'oadrUpdateReport',
            required: true,
            elementName: {
              localPart: 'oadrUpdateReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrUpdateReportType'
          }, {
            name: 'oadrUpdatedReport',
            required: true,
            elementName: {
              localPart: 'oadrUpdatedReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrUpdatedReportType'
          }, {
            name: 'oadrCancelPartyRegistration',
            required: true,
            elementName: {
              localPart: 'oadrCancelPartyRegistration',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCancelPartyRegistrationType'
          }, {
            name: 'oadrCanceledPartyRegistration',
            required: true,
            elementName: {
              localPart: 'oadrCanceledPartyRegistration',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCanceledPartyRegistrationType'
          }, {
            name: 'oadrCreatePartyRegistration',
            required: true,
            elementName: {
              localPart: 'oadrCreatePartyRegistration',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreatePartyRegistrationType'
          }, {
            name: 'oadrCreatedPartyRegistration',
            required: true,
            elementName: {
              localPart: 'oadrCreatedPartyRegistration',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreatedPartyRegistrationType'
          }, {
            name: 'oadrRequestReregistration',
            required: true,
            elementName: {
              localPart: 'oadrRequestReregistration',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrRequestReregistrationType'
          }, {
            name: 'oadrQueryRegistration',
            required: true,
            elementName: {
              localPart: 'oadrQueryRegistration',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrQueryRegistrationType'
          }, {
            name: 'oadrPoll',
            required: true,
            elementName: {
              localPart: 'oadrPoll',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrPollType'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'EiResponseType',
        propertyInfos: [{
            name: 'responseCode',
            required: true
          }, {
            name: 'responseDescription'
          }, {
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }]
      }, {
        localName: 'Properties.Tolerance',
        typeName: null,
        propertyInfos: [{
            name: 'tolerate',
            required: true,
            elementName: {
              localPart: 'tolerate',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Properties.Tolerance.Tolerate'
          }]
      }, {
        localName: 'TransportInterfaceType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'TransportInterfaceType'
        },
        propertyInfos: [{
            name: 'pointOfReceipt',
            required: true,
            elementName: {
              localPart: 'pointOfReceipt',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }, {
            name: 'pointOfDelivery',
            required: true,
            elementName: {
              localPart: 'pointOfDelivery',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }]
      }, {
        localName: 'SignedInfoType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'SignedInfoType'
        },
        propertyInfos: [{
            name: 'canonicalizationMethod',
            required: true,
            elementName: {
              localPart: 'CanonicalizationMethod',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.CanonicalizationMethodType'
          }, {
            name: 'signatureMethod',
            required: true,
            elementName: {
              localPart: 'SignatureMethod',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.SignatureMethodType'
          }, {
            name: 'reference',
            required: true,
            collection: true,
            elementName: {
              localPart: 'Reference',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.ReferenceType'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrCreateOptType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCreateOptType'
        },
        baseTypeInfo: '.EiOptType',
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'qualifiedEventID',
            typeInfo: '.QualifiedEventIDType'
          }, {
            name: 'eiTarget',
            required: true,
            typeInfo: '.EiTargetType'
          }, {
            name: 'oadrDeviceClass',
            elementName: {
              localPart: 'oadrDeviceClass',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.EiTargetType'
          }]
      }, {
        localName: 'X509DigestType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'X509DigestType'
        },
        propertyInfos: [{
            name: 'value',
            typeInfo: 'Base64Binary',
            type: 'value'
          }, {
            name: 'algorithm',
            required: true,
            attributeName: {
              localPart: 'Algorithm'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'Dtstart',
        typeName: null,
        propertyInfos: [{
            name: 'dateTime',
            required: true,
            elementName: {
              localPart: 'date-time',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: 'DateTime'
          }]
      }, {
        localName: 'ReadingQuality',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ReadingQuality'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'quality',
            required: true,
            elementName: {
              localPart: 'quality',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }]
      }, {
        localName: 'ElectricPowerUsageSummary',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ElectricPowerUsageSummary'
        },
        baseTypeInfo: '.IdentifiedObject',
        propertyInfos: [{
            name: 'billingPeriod',
            elementName: {
              localPart: 'billingPeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.DateTimeInterval'
          }, {
            name: 'billLastPeriod',
            elementName: {
              localPart: 'billLastPeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'billToDate',
            elementName: {
              localPart: 'billToDate',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'costAdditionalLastPeriod',
            elementName: {
              localPart: 'costAdditionalLastPeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'costAdditionalDetailLastPeriod',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'costAdditionalDetailLastPeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.LineItem'
          }, {
            name: 'currency',
            elementName: {
              localPart: 'currency',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'overallConsumptionLastPeriod',
            elementName: {
              localPart: 'overallConsumptionLastPeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'currentBillingPeriodOverAllConsumption',
            elementName: {
              localPart: 'currentBillingPeriodOverAllConsumption',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'currentDayLastYearNetConsumption',
            elementName: {
              localPart: 'currentDayLastYearNetConsumption',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'currentDayNetConsumption',
            elementName: {
              localPart: 'currentDayNetConsumption',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'currentDayOverallConsumption',
            elementName: {
              localPart: 'currentDayOverallConsumption',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'peakDemand',
            elementName: {
              localPart: 'peakDemand',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'previousDayLastYearOverallConsumption',
            elementName: {
              localPart: 'previousDayLastYearOverallConsumption',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'previousDayNetConsumption',
            elementName: {
              localPart: 'previousDayNetConsumption',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'previousDayOverallConsumption',
            elementName: {
              localPart: 'previousDayOverallConsumption',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'qualityOfReading',
            elementName: {
              localPart: 'qualityOfReading',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'ratchetDemand',
            elementName: {
              localPart: 'ratchetDemand',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.SummaryMeasurement'
          }, {
            name: 'ratchetDemandPeriod',
            elementName: {
              localPart: 'ratchetDemandPeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.DateTimeInterval'
          }, {
            name: 'statusTimeStamp',
            required: true,
            elementName: {
              localPart: 'statusTimeStamp',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }]
      }, {
        localName: 'OadrTransports.OadrTransport',
        typeName: null,
        propertyInfos: [{
            name: 'oadrTransportName',
            required: true,
            elementName: {
              localPart: 'oadrTransportName',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrTransportType'
          }]
      }, {
        localName: 'CurrentValueType',
        typeName: 'currentValueType',
        propertyInfos: [{
            name: 'payloadFloat',
            required: true,
            typeInfo: '.PayloadFloatType'
          }]
      }, {
        localName: 'OadrCreatedPartyRegistrationType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCreatedPartyRegistrationType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'registrationID'
          }, {
            name: 'venID'
          }, {
            name: 'vtnID',
            required: true
          }, {
            name: 'oadrProfiles',
            required: true,
            elementName: {
              localPart: 'oadrProfiles',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrProfiles'
          }, {
            name: 'oadrRequestedOadrPollFreq',
            elementName: {
              localPart: 'oadrRequestedOadrPollFreq',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.DurationPropType'
          }, {
            name: 'oadrServiceSpecificInfo',
            elementName: {
              localPart: 'oadrServiceSpecificInfo',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrServiceSpecificInfo'
          }, {
            name: 'oadrExtensions',
            elementName: {
              localPart: 'oadrExtensions',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreatedPartyRegistrationType.OadrExtensions'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrUpdatedReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrUpdatedReportType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'oadrCancelReport',
            elementName: {
              localPart: 'oadrCancelReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCancelReportType'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'KeyInfoReferenceType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'KeyInfoReferenceType'
        },
        propertyInfos: [{
            name: 'uri',
            required: true,
            attributeName: {
              localPart: 'URI'
            },
            type: 'attribute'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'RationalNumber',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'RationalNumber'
        },
        propertyInfos: [{
            name: 'numerator',
            elementName: {
              localPart: 'numerator',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Integer'
          }, {
            name: 'denominator',
            elementName: {
              localPart: 'denominator',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'AnyType'
          }]
      }, {
        localName: 'PowerItemType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'PowerItemType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }, {
            name: 'powerAttributes',
            required: true,
            elementName: {
              localPart: 'powerAttributes',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.PowerAttributesType'
          }]
      }, {
        localName: 'PowerRealType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'PowerRealType'
        },
        baseTypeInfo: '.PowerItemType'
      }, {
        localName: 'UsagePoint',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'UsagePoint'
        },
        baseTypeInfo: '.IdentifiedObject',
        propertyInfos: [{
            name: 'roleFlags',
            elementName: {
              localPart: 'roleFlags',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'HexBinary'
          }, {
            name: 'serviceCategory',
            elementName: {
              localPart: 'ServiceCategory',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.ServiceCategory'
          }, {
            name: 'status',
            elementName: {
              localPart: 'status',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'UnsignedByte'
          }, {
            name: 'serviceDeliveryPoint',
            elementName: {
              localPart: 'ServiceDeliveryPoint',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.ServiceDeliveryPoint'
          }]
      }, {
        localName: 'OadrCreatedOptType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCreatedOptType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'optID',
            required: true
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ServiceStatus',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ServiceStatus'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'currentStatus',
            required: true,
            elementName: {
              localPart: 'currentStatus',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }]
      }, {
        localName: 'ServiceCategory',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ServiceCategory'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'kind',
            required: true,
            elementName: {
              localPart: 'kind',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }]
      }, {
        localName: 'OadrLoadControlStateType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrLoadControlStateType'
        },
        propertyInfos: [{
            name: 'oadrCapacity',
            elementName: {
              localPart: 'oadrCapacity',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrLoadControlStateTypeType'
          }, {
            name: 'oadrLevelOffset',
            elementName: {
              localPart: 'oadrLevelOffset',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrLoadControlStateTypeType'
          }, {
            name: 'oadrPercentOffset',
            elementName: {
              localPart: 'oadrPercentOffset',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrLoadControlStateTypeType'
          }, {
            name: 'oadrSetPoint',
            elementName: {
              localPart: 'oadrSetPoint',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrLoadControlStateTypeType'
          }]
      }, {
        localName: 'RSAKeyValueType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'RSAKeyValueType'
        },
        propertyInfos: [{
            name: 'modulus',
            required: true,
            elementName: {
              localPart: 'Modulus',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'exponent',
            required: true,
            elementName: {
              localPart: 'Exponent',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }]
      }, {
        localName: 'OadrServiceSpecificInfo.OadrService',
        typeName: null,
        propertyInfos: [{
            name: 'oadrServiceName',
            required: true,
            elementName: {
              localPart: 'oadrServiceName',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrServiceNameType'
          }, {
            name: 'oadrInfo',
            required: true,
            collection: true,
            elementName: {
              localPart: 'oadrInfo',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrInfo'
          }]
      }, {
        localName: 'OadrCanceledOptType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCanceledOptType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'optID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrProfiles.OadrProfile',
        typeName: null,
        propertyInfos: [{
            name: 'oadrProfileName',
            required: true,
            elementName: {
              localPart: 'oadrProfileName',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Token',
            values: ['2.0a', '2.0b']
          }, {
            name: 'oadrTransports',
            required: true,
            elementName: {
              localPart: 'oadrTransports',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrTransports'
          }]
      }, {
        localName: 'EventResponses',
        typeName: null,
        propertyInfos: [{
            name: 'eventResponse',
            minOccurs: 0,
            collection: true,
            typeInfo: '.EventResponses.EventResponse'
          }]
      }, {
        localName: 'ServiceLocationType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'ServiceLocationType'
        },
        propertyInfos: [{
            name: 'featureCollection',
            required: true,
            elementName: {
              localPart: 'FeatureCollection',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            typeInfo: '.FeatureCollection'
          }]
      }, {
        localName: 'DEREncodedKeyValueType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'DEREncodedKeyValueType'
        },
        propertyInfos: [{
            name: 'value',
            typeInfo: 'Base64Binary',
            type: 'value'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'DateTimeType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'dateTimeType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'value',
            typeInfo: 'DateTime',
            type: 'value'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'PGPDataType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'PGPDataType'
        },
        propertyInfos: [{
            name: 'content',
            required: true,
            collection: true,
            mixed: false,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'PGPKeyID',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: 'Base64Binary'
              }, {
                elementName: {
                  localPart: 'PGPKeyPacket',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: 'Base64Binary'
              }],
            type: 'elementRefs'
          }]
      }, {
        localName: 'OadrCanceledReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCanceledReportType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'oadrPendingReports',
            required: true,
            elementName: {
              localPart: 'oadrPendingReports',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrPendingReportsType'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrInfo',
        typeName: null,
        propertyInfos: [{
            name: 'oadrKey',
            required: true,
            elementName: {
              localPart: 'oadrKey',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'oadrValue',
            required: true,
            elementName: {
              localPart: 'oadrValue',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }]
      }, {
        localName: 'OadrCanceledPartyRegistrationType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCanceledPartyRegistrationType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'registrationID'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'SourceType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'sourceType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'authorOrCategoryOrContributor',
            minOccurs: 0,
            collection: true,
            mixed: false,
            allowDom: false,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'title',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }, {
                elementName: {
                  localPart: 'contributor',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.PersonType'
              }, {
                elementName: {
                  localPart: 'id',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.IdType'
              }, {
                elementName: {
                  localPart: 'author',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.PersonType'
              }, {
                elementName: {
                  localPart: 'icon',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.IconType'
              }, {
                elementName: {
                  localPart: 'category',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.CategoryType'
              }, {
                elementName: {
                  localPart: 'generator',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.GeneratorType'
              }, {
                elementName: {
                  localPart: 'logo',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.LogoType'
              }, {
                elementName: {
                  localPart: 'subtitle',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }, {
                elementName: {
                  localPart: 'updated',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.DateTimeType'
              }, {
                elementName: {
                  localPart: 'link',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.LinkType'
              }, {
                elementName: {
                  localPart: 'rights',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }],
            type: 'elementRefs'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ArrayOfVavailabilityContainedComponents',
        typeName: {
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0',
          localPart: 'ArrayOfVavailabilityContainedComponents'
        },
        propertyInfos: [{
            name: 'available',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'available',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.AvailableType'
          }]
      }, {
        localName: 'Object',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'Object'
        },
        propertyInfos: [{
            name: 'extension',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'extension',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'AnyType'
          }]
      }, {
        localName: 'DigestMethodType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'DigestMethodType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            type: 'anyElement'
          }, {
            name: 'algorithm',
            required: true,
            attributeName: {
              localPart: 'Algorithm'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'PayloadAvobVenServiceRequestType',
        typeName: {
          namespaceURI: 'http:\/\/oadr.avob.com',
          localPart: 'payloadAvobVenServiceRequestType'
        },
        baseTypeInfo: '.PayloadBaseType',
        propertyInfos: [{
            name: 'requests',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'requests',
              namespaceURI: 'http:\/\/oadr.avob.com'
            },
            typeInfo: '.AvobVenServiceRequestType'
          }]
      }, {
        localName: 'PersonType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'personType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'nameOrUriOrEmail',
            minOccurs: 0,
            collection: true,
            mixed: false,
            allowDom: false,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'uri',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.UriType'
              }, {
                elementName: {
                  localPart: 'name',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                }
              }, {
                elementName: {
                  localPart: 'email',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: 'NormalizedString'
              }],
            type: 'elementRefs'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrCreatedReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCreatedReportType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'oadrPendingReports',
            required: true,
            elementName: {
              localPart: 'oadrPendingReports',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrPendingReportsType'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'FrequencyType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'FrequencyType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'EiRequestEvent',
        typeName: null,
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'venID',
            required: true
          }, {
            name: 'replyLimit',
            elementName: {
              localPart: 'replyLimit',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            },
            typeInfo: 'UnsignedInt'
          }]
      }, {
        localName: 'QualifiedEventIDType',
        propertyInfos: [{
            name: 'eventID',
            required: true
          }, {
            name: 'modificationNumber',
            required: true,
            typeInfo: 'UnsignedInt'
          }]
      }, {
        localName: 'CurrencyType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'currencyType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.CurrencyItemDescriptionType'
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.ISO3AlphaCurrencyCodeContentType'
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'ReportSpecifierType',
        propertyInfos: [{
            name: 'reportSpecifierID',
            required: true
          }, {
            name: 'granularity',
            required: true,
            elementName: {
              localPart: 'granularity',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.DurationPropType'
          }, {
            name: 'reportBackDuration',
            required: true,
            typeInfo: '.DurationPropType'
          }, {
            name: 'reportInterval',
            typeInfo: '.WsCalendarIntervalType'
          }, {
            name: 'specifierPayload',
            required: true,
            collection: true,
            typeInfo: '.SpecifierPayloadType'
          }]
      }, {
        localName: 'OadrReportRequestType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrReportRequestType'
        },
        propertyInfos: [{
            name: 'reportRequestID',
            required: true
          }, {
            name: 'reportSpecifier',
            required: true,
            typeInfo: '.ReportSpecifierType'
          }]
      }, {
        localName: 'Dtend',
        typeName: null,
        propertyInfos: [{
            name: 'dateTime',
            required: true,
            elementName: {
              localPart: 'date-time',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: 'DateTime'
          }]
      }, {
        localName: 'StreamBaseType',
        typeName: {
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream',
          localPart: 'StreamBaseType'
        },
        propertyInfos: [{
            name: 'dtstart',
            elementName: {
              localPart: 'dtstart',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Dtstart'
          }, {
            name: 'duration',
            elementName: {
              localPart: 'duration',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.DurationPropType'
          }, {
            name: 'intervals',
            elementName: {
              localPart: 'intervals',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
            },
            typeInfo: '.Intervals'
          }]
      }, {
        localName: 'BaseUnitType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'BaseUnitType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'OadrPollType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrPollType'
        },
        propertyInfos: [{
            name: 'venID',
            required: true
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'SpecifierPayloadType',
        propertyInfos: [{
            name: 'rid',
            required: true,
            elementName: 'rID'
          }, {
            name: 'itemBase',
            mixed: false,
            allowDom: false,
            elementName: {
              localPart: 'itemBase',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            },
            typeInfo: '.ItemBaseType',
            type: 'elementRef'
          }, {
            name: 'readingType',
            required: true
          }]
      }, {
        localName: 'KeyValueType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'KeyValueType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'RSAKeyValue',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.RSAKeyValueType'
              }, {
                elementName: {
                  localPart: 'DSAKeyValue',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.DSAKeyValueType'
              }],
            type: 'elementRefs'
          }]
      }, {
        localName: 'FeatureCollection.Location.Polygon.Exterior.LinearRing',
        typeName: null,
        propertyInfos: [{
            name: 'posList',
            required: true,
            elementName: {
              localPart: 'posList',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            typeInfo: {
              type: 'list',
              baseTypeInfo: 'Double'
            }
          }]
      }, {
        localName: 'OadrCreatedEventType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCreatedEventType'
        },
        propertyInfos: [{
            name: 'eiCreatedEvent',
            required: true,
            elementName: {
              localPart: 'eiCreatedEvent',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            },
            typeInfo: '.EiCreatedEvent'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'CanonicalizationMethodType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'CanonicalizationMethodType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            allowDom: false,
            type: 'anyElement'
          }, {
            name: 'algorithm',
            required: true,
            attributeName: {
              localPart: 'Algorithm'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'DurationPropType',
        typeName: {
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0',
          localPart: 'DurationPropType'
        },
        propertyInfos: [{
            name: 'duration',
            required: true,
            elementName: {
              localPart: 'duration',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            }
          }]
      }, {
        localName: 'EntryType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'entryType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'authorOrCategoryOrContent',
            minOccurs: 0,
            collection: true,
            mixed: false,
            allowDom: false,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'contributor',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.PersonType'
              }, {
                elementName: {
                  localPart: 'link',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.LinkType'
              }, {
                elementName: {
                  localPart: 'source',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }, {
                elementName: {
                  localPart: 'category',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.CategoryType'
              }, {
                elementName: {
                  localPart: 'published',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.DateTimeType'
              }, {
                elementName: {
                  localPart: 'rights',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }, {
                elementName: {
                  localPart: 'author',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.PersonType'
              }, {
                elementName: {
                  localPart: 'id',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.IdType'
              }, {
                elementName: {
                  localPart: 'content',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.ContentType'
              }, {
                elementName: {
                  localPart: 'updated',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.DateTimeType'
              }, {
                elementName: {
                  localPart: 'title',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }, {
                elementName: {
                  localPart: 'summary',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }],
            type: 'elementRefs'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrReportDescriptionType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrReportDescriptionType'
        },
        propertyInfos: [{
            name: 'rid',
            required: true,
            elementName: 'rID'
          }, {
            name: 'reportSubject',
            typeInfo: '.EiTargetType'
          }, {
            name: 'reportDataSource',
            typeInfo: '.EiTargetType'
          }, {
            name: 'reportType',
            required: true
          }, {
            name: 'itemBase',
            mixed: false,
            allowDom: false,
            elementName: {
              localPart: 'itemBase',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            },
            typeInfo: '.ItemBaseType',
            type: 'elementRef'
          }, {
            name: 'readingType',
            required: true
          }, {
            name: 'marketContext',
            elementName: {
              localPart: 'marketContext',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            }
          }, {
            name: 'oadrSamplingRate',
            elementName: {
              localPart: 'oadrSamplingRate',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrSamplingRateType'
          }]
      }, {
        localName: 'LinkType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'linkType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'content',
            type: 'value'
          }, {
            name: 'href',
            required: true,
            attributeName: {
              localPart: 'href'
            },
            type: 'attribute'
          }, {
            name: 'rel',
            attributeName: {
              localPart: 'rel'
            },
            type: 'attribute'
          }, {
            name: 'type',
            attributeName: {
              localPart: 'type'
            },
            type: 'attribute'
          }, {
            name: 'hreflang',
            typeInfo: 'NMToken',
            attributeName: {
              localPart: 'hreflang'
            },
            type: 'attribute'
          }, {
            name: 'title',
            attributeName: {
              localPart: 'title'
            },
            type: 'attribute'
          }, {
            name: 'length',
            typeInfo: 'PositiveInteger',
            attributeName: {
              localPart: 'length'
            },
            type: 'attribute'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ECValidationDataType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'ECValidationDataType'
        },
        propertyInfos: [{
            name: 'seed',
            required: true,
            elementName: {
              localPart: 'seed',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'hashAlgorithm',
            required: true,
            attributeName: {
              localPart: 'hashAlgorithm'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'EiActivePeriodType',
        typeName: 'eiActivePeriodType',
        propertyInfos: [{
            name: 'properties',
            required: true,
            elementName: {
              localPart: 'properties',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Properties'
          }, {
            name: 'components',
            required: true,
            elementName: {
              localPart: 'components',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: 'AnyType'
          }]
      }, {
        localName: 'ElectricPowerQualitySummary',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ElectricPowerQualitySummary'
        },
        baseTypeInfo: '.IdentifiedObject',
        propertyInfos: [{
            name: 'flickerPlt',
            elementName: {
              localPart: 'flickerPlt',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'flickerPst',
            elementName: {
              localPart: 'flickerPst',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'harmonicVoltage',
            elementName: {
              localPart: 'harmonicVoltage',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'longInterruptions',
            elementName: {
              localPart: 'longInterruptions',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'mainsVoltage',
            elementName: {
              localPart: 'mainsVoltage',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'measurementProtocol',
            elementName: {
              localPart: 'measurementProtocol',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'UnsignedByte'
          }, {
            name: 'powerFrequency',
            elementName: {
              localPart: 'powerFrequency',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'rapidVoltageChanges',
            elementName: {
              localPart: 'rapidVoltageChanges',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'shortInterruptions',
            elementName: {
              localPart: 'shortInterruptions',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'summaryInterval',
            required: true,
            elementName: {
              localPart: 'summaryInterval',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.DateTimeInterval'
          }, {
            name: 'supplyVoltageDips',
            elementName: {
              localPart: 'supplyVoltageDips',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'supplyVoltageImbalance',
            elementName: {
              localPart: 'supplyVoltageImbalance',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'supplyVoltageVariations',
            elementName: {
              localPart: 'supplyVoltageVariations',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'tempOvervoltage',
            elementName: {
              localPart: 'tempOvervoltage',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }]
      }, {
        localName: 'MeterAssetType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'MeterAssetType'
        },
        propertyInfos: [{
            name: 'mrid',
            required: true,
            elementName: {
              localPart: 'mrid',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }]
      }, {
        localName: 'OadrCreateReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCreateReportType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'oadrReportRequest',
            required: true,
            collection: true,
            elementName: {
              localPart: 'oadrReportRequest',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrReportRequestType'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'EventResponses.EventResponse',
        typeName: null,
        propertyInfos: [{
            name: 'responseCode',
            required: true
          }, {
            name: 'responseDescription'
          }, {
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'qualifiedEventID',
            required: true,
            typeInfo: '.QualifiedEventIDType'
          }, {
            name: 'optType',
            required: true,
            typeInfo: '.OptTypeType'
          }]
      }, {
        localName: 'TransformType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'TransformType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            elementName: {
              localPart: 'XPath',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            type: 'elementRef'
          }, {
            name: 'algorithm',
            required: true,
            attributeName: {
              localPart: 'Algorithm'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ContentType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'contentType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'content',
            collection: true,
            allowDom: false,
            type: 'anyElement'
          }, {
            name: 'type',
            attributeName: {
              localPart: 'type'
            },
            type: 'attribute'
          }, {
            name: 'src',
            attributeName: {
              localPart: 'src'
            },
            type: 'attribute'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrGBItemBase',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrGBItemBase'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'feed',
            required: true,
            elementName: {
              localPart: 'feed',
              namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
            },
            typeInfo: '.FeedType'
          }]
      }, {
        localName: 'EiOptType',
        propertyInfos: [{
            name: 'optID',
            required: true
          }, {
            name: 'optType',
            required: true,
            typeInfo: '.OptTypeType'
          }, {
            name: 'optReason',
            required: true
          }, {
            name: 'marketContext',
            elementName: {
              localPart: 'marketContext',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            }
          }, {
            name: 'venID',
            required: true
          }, {
            name: 'vavailability',
            elementName: {
              localPart: 'vavailability',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.VavailabilityType'
          }, {
            name: 'createdDateTime',
            required: true,
            typeInfo: 'DateTime'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'PowerApparentType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'PowerApparentType'
        },
        baseTypeInfo: '.PowerItemType'
      }, {
        localName: 'PowerReactiveType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'PowerReactiveType'
        },
        baseTypeInfo: '.PowerItemType'
      }, {
        localName: 'TnBFieldParamsType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'TnBFieldParamsType'
        },
        baseTypeInfo: '.CharTwoFieldParamsType',
        propertyInfos: [{
            name: 'k',
            required: true,
            elementName: {
              localPart: 'K',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'PositiveInteger'
          }]
      }, {
        localName: 'EventDescriptorType',
        typeName: 'eventDescriptorType',
        propertyInfos: [{
            name: 'eventID',
            required: true
          }, {
            name: 'modificationNumber',
            required: true,
            typeInfo: 'UnsignedInt'
          }, {
            name: 'modificationDateTime',
            typeInfo: 'DateTime'
          }, {
            name: 'modificationReason'
          }, {
            name: 'priority',
            typeInfo: 'UnsignedInt'
          }, {
            name: 'eiMarketContext',
            required: true,
            typeInfo: '.EventDescriptorType.EiMarketContext'
          }, {
            name: 'createdDateTime',
            required: true,
            typeInfo: 'DateTime'
          }, {
            name: 'eventStatus',
            required: true,
            typeInfo: '.EventStatusEnumeratedType'
          }, {
            name: 'testEvent'
          }, {
            name: 'vtnComment'
          }]
      }, {
        localName: 'OadrCreatedPartyRegistrationType.OadrExtensions.OadrExtension',
        typeName: null,
        propertyInfos: [{
            name: 'oadrExtensionName',
            required: true,
            elementName: {
              localPart: 'oadrExtensionName',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'oadrInfo',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrInfo',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrInfo'
          }]
      }, {
        localName: 'LineItem',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'LineItem'
        },
        propertyInfos: [{
            name: 'amount',
            required: true,
            elementName: {
              localPart: 'amount',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'rounding',
            elementName: {
              localPart: 'rounding',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'dateTime',
            required: true,
            elementName: {
              localPart: 'dateTime',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'note',
            required: true,
            elementName: {
              localPart: 'note',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }]
      }, {
        localName: 'VavailabilityType',
        typeName: {
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0',
          localPart: 'VavailabilityType'
        },
        propertyInfos: [{
            name: 'components',
            required: true,
            elementName: {
              localPart: 'components',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.ArrayOfVavailabilityContainedComponents'
          }]
      }, {
        localName: 'OadrReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrReportType'
        },
        baseTypeInfo: '.StreamBaseType',
        propertyInfos: [{
            name: 'eiReportID'
          }, {
            name: 'oadrReportDescription',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrReportDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrReportDescriptionType'
          }, {
            name: 'reportRequestID',
            required: true
          }, {
            name: 'reportSpecifierID',
            required: true
          }, {
            name: 'reportName'
          }, {
            name: 'createdDateTime',
            required: true,
            typeInfo: 'DateTime'
          }]
      }, {
        localName: 'SignatureType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'SignatureType'
        },
        propertyInfos: [{
            name: 'signedInfo',
            required: true,
            elementName: {
              localPart: 'SignedInfo',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.SignedInfoType'
          }, {
            name: 'signatureValue',
            required: true,
            elementName: {
              localPart: 'SignatureValue',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.SignatureValueType'
          }, {
            name: 'keyInfo',
            elementName: {
              localPart: 'KeyInfo',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.KeyInfoType'
          }, {
            name: 'object',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'Object',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.ObjectType'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrUpdateReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrUpdateReportType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'oadrReport',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrReportType'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'EnergyRealType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'EnergyRealType'
        },
        baseTypeInfo: '.EnergyItemType'
      }, {
        localName: 'EiTargetType',
        propertyInfos: [{
            name: 'aggregatedPnode',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'aggregatedPnode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.AggregatedPnodeType'
          }, {
            name: 'endDeviceAsset',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'endDeviceAsset',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.EndDeviceAssetType'
          }, {
            name: 'meterAsset',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'meterAsset',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.MeterAssetType'
          }, {
            name: 'pnode',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'pnode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.PnodeType'
          }, {
            name: 'serviceArea',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'serviceArea',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            },
            typeInfo: '.ServiceAreaType'
          }, {
            name: 'serviceDeliveryPoint',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'serviceDeliveryPoint',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.ServiceDeliveryPointType'
          }, {
            name: 'serviceLocation',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'serviceLocation',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.ServiceLocationType'
          }, {
            name: 'transportInterface',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'transportInterface',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: '.TransportInterfaceType'
          }, {
            name: 'groupID',
            minOccurs: 0,
            collection: true
          }, {
            name: 'groupName',
            minOccurs: 0,
            collection: true
          }, {
            name: 'resourceID',
            minOccurs: 0,
            collection: true
          }, {
            name: 'venID',
            minOccurs: 0,
            collection: true
          }, {
            name: 'partyID',
            minOccurs: 0,
            collection: true
          }]
      }, {
        localName: 'KeyTokenType',
        typeName: {
          namespaceURI: 'http:\/\/oadr.avob.com',
          localPart: 'keyTokenType'
        },
        propertyInfos: [{
            name: 'key',
            required: true,
            elementName: {
              localPart: 'key',
              namespaceURI: 'http:\/\/oadr.avob.com'
            }
          }, {
            name: 'value',
            required: true,
            elementName: {
              localPart: 'value',
              namespaceURI: 'http:\/\/oadr.avob.com'
            }
          }]
      }, {
        localName: 'OadrPayload',
        typeName: null,
        propertyInfos: [{
            name: 'signature',
            elementName: {
              localPart: 'Signature',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.SignatureType'
          }, {
            name: 'oadrSignedObject',
            required: true,
            elementName: {
              localPart: 'oadrSignedObject',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrSignedObject'
          }]
      }, {
        localName: 'OadrProfiles',
        typeName: null,
        propertyInfos: [{
            name: 'oadrProfile',
            required: true,
            collection: true,
            elementName: {
              localPart: 'oadrProfile',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrProfiles.OadrProfile'
          }]
      }, {
        localName: 'EiEventSignalsType',
        typeName: 'eiEventSignalsType',
        propertyInfos: [{
            name: 'eiEventSignal',
            required: true,
            collection: true,
            typeInfo: '.EiEventSignalType'
          }, {
            name: 'eiEventBaseline',
            typeInfo: '.EiEventBaselineType'
          }]
      }, {
        localName: 'OadrCancelReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCancelReportType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'reportRequestID',
            required: true,
            collection: true
          }, {
            name: 'reportToFollow',
            required: true,
            elementName: {
              localPart: 'reportToFollow',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            },
            typeInfo: 'Boolean'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ManifestType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'ManifestType'
        },
        propertyInfos: [{
            name: 'reference',
            required: true,
            collection: true,
            elementName: {
              localPart: 'Reference',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.ReferenceType'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'Intervals',
        typeName: null,
        propertyInfos: [{
            name: 'interval',
            required: true,
            collection: true,
            typeInfo: '.IntervalType'
          }]
      }, {
        localName: 'OadrDistributeEventType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrDistributeEventType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            typeInfo: '.EiResponseType'
          }, {
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'vtnID',
            required: true
          }, {
            name: 'oadrEvent',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrEvent',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrDistributeEventType.OadrEvent'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrRegisteredReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrRegisteredReportType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'oadrReportRequest',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrReportRequest',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrReportRequestType'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrCreatePartyRegistrationType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCreatePartyRegistrationType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'registrationID'
          }, {
            name: 'venID'
          }, {
            name: 'oadrProfileName',
            required: true,
            elementName: {
              localPart: 'oadrProfileName',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Token',
            values: ['2.0a', '2.0b']
          }, {
            name: 'oadrTransportName',
            required: true,
            elementName: {
              localPart: 'oadrTransportName',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrTransportType'
          }, {
            name: 'oadrTransportAddress',
            elementName: {
              localPart: 'oadrTransportAddress',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'oadrReportOnly',
            required: true,
            elementName: {
              localPart: 'oadrReportOnly',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Boolean'
          }, {
            name: 'oadrXmlSignature',
            required: true,
            elementName: {
              localPart: 'oadrXmlSignature',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Boolean'
          }, {
            name: 'oadrVenName',
            elementName: {
              localPart: 'oadrVenName',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'oadrHttpPullModel',
            elementName: {
              localPart: 'oadrHttpPullModel',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Boolean'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'TextType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'textType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'content',
            collection: true,
            allowDom: false,
            type: 'anyElement'
          }, {
            name: 'type',
            values: ['text', 'html', 'xhtml'],
            attributeName: {
              localPart: 'type'
            },
            type: 'attribute'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrPayloadResourceStatusType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrPayloadResourceStatusType'
        },
        baseTypeInfo: '.PayloadBaseType',
        propertyInfos: [{
            name: 'oadrOnline',
            required: true,
            elementName: {
              localPart: 'oadrOnline',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Boolean'
          }, {
            name: 'oadrManualOverride',
            required: true,
            elementName: {
              localPart: 'oadrManualOverride',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Boolean'
          }, {
            name: 'oadrLoadControlState',
            elementName: {
              localPart: 'oadrLoadControlState',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrLoadControlStateType'
          }]
      }, {
        localName: 'NamedCurveType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'NamedCurveType'
        },
        propertyInfos: [{
            name: 'uri',
            required: true,
            attributeName: {
              localPart: 'URI'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ServiceAreaType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06',
          localPart: 'ServiceAreaType'
        },
        propertyInfos: [{
            name: 'featureCollection',
            required: true,
            elementName: {
              localPart: 'FeatureCollection',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            typeInfo: '.FeatureCollection'
          }]
      }, {
        localName: 'OadrTransports',
        typeName: null,
        propertyInfos: [{
            name: 'oadrTransport',
            required: true,
            collection: true,
            elementName: {
              localPart: 'oadrTransport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrTransports.OadrTransport'
          }]
      }, {
        localName: 'IdType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'idType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'value',
            type: 'value'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ItemBaseType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06',
          localPart: 'ItemBaseType'
        }
      }, {
        localName: 'PnBFieldParamsType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'PnBFieldParamsType'
        },
        baseTypeInfo: '.CharTwoFieldParamsType',
        propertyInfos: [{
            name: 'k1',
            required: true,
            elementName: {
              localPart: 'K1',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'PositiveInteger'
          }, {
            name: 'k2',
            required: true,
            elementName: {
              localPart: 'K2',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'PositiveInteger'
          }, {
            name: 'k3',
            required: true,
            elementName: {
              localPart: 'K3',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'PositiveInteger'
          }]
      }, {
        localName: 'X509IssuerSerialType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'X509IssuerSerialType'
        },
        propertyInfos: [{
            name: 'x509IssuerName',
            required: true,
            elementName: {
              localPart: 'X509IssuerName',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            }
          }, {
            name: 'x509SerialNumber',
            required: true,
            elementName: {
              localPart: 'X509SerialNumber',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Integer'
          }]
      }, {
        localName: 'X509DataType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'X509DataType'
        },
        propertyInfos: [{
            name: 'x509IssuerSerialOrX509SKIOrX509SubjectName',
            required: true,
            collection: true,
            mixed: false,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'X509IssuerSerial',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.X509IssuerSerialType'
              }, {
                elementName: {
                  localPart: 'X509CRL',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: 'Base64Binary'
              }, {
                elementName: {
                  localPart: 'X509Certificate',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: 'Base64Binary'
              }, {
                elementName: {
                  localPart: 'X509SKI',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: 'Base64Binary'
              }, {
                elementName: {
                  localPart: 'X509SubjectName',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                }
              }],
            type: 'elementRefs'
          }]
      }, {
        localName: 'OadrPendingReportsType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrPendingReportsType'
        },
        propertyInfos: [{
            name: 'reportRequestID',
            minOccurs: 0,
            collection: true
          }]
      }, {
        localName: 'ThermType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'ThermType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'DSAKeyValueType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'DSAKeyValueType'
        },
        propertyInfos: [{
            name: 'p',
            required: true,
            elementName: {
              localPart: 'P',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'q',
            required: true,
            elementName: {
              localPart: 'Q',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'g',
            elementName: {
              localPart: 'G',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'y',
            required: true,
            elementName: {
              localPart: 'Y',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'j',
            elementName: {
              localPart: 'J',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'seed',
            required: true,
            elementName: {
              localPart: 'Seed',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'pgenCounter',
            required: true,
            elementName: {
              localPart: 'PgenCounter',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }]
      }, {
        localName: 'FeatureCollection.Location',
        typeName: null,
        propertyInfos: [{
            name: 'polygon',
            required: true,
            elementName: {
              localPart: 'Polygon',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            typeInfo: '.FeatureCollection.Location.Polygon'
          }]
      }, {
        localName: 'FeatureCollection.Location.Polygon.Exterior',
        typeName: null,
        propertyInfos: [{
            name: 'linearRing',
            required: true,
            elementName: {
              localPart: 'LinearRing',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            typeInfo: '.FeatureCollection.Location.Polygon.Exterior.LinearRing'
          }]
      }, {
        localName: 'PowerAttributesType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'PowerAttributesType'
        },
        propertyInfos: [{
            name: 'hertz',
            required: true,
            elementName: {
              localPart: 'hertz',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: 'Decimal'
          }, {
            name: 'voltage',
            required: true,
            elementName: {
              localPart: 'voltage',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: 'Decimal'
          }, {
            name: 'ac',
            required: true,
            elementName: {
              localPart: 'ac',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            },
            typeInfo: 'Boolean'
          }]
      }, {
        localName: 'ECKeyValueType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'ECKeyValueType'
        },
        propertyInfos: [{
            name: 'ecParameters',
            required: true,
            elementName: {
              localPart: 'ECParameters',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.ECParametersType'
          }, {
            name: 'namedCurve',
            required: true,
            elementName: {
              localPart: 'NamedCurve',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.NamedCurveType'
          }, {
            name: 'publicKey',
            required: true,
            elementName: {
              localPart: 'PublicKey',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'TimeConfiguration',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'TimeConfiguration'
        },
        baseTypeInfo: '.IdentifiedObject',
        propertyInfos: [{
            name: 'dstEndRule',
            required: true,
            elementName: {
              localPart: 'dstEndRule',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'HexBinary'
          }, {
            name: 'dstOffset',
            required: true,
            elementName: {
              localPart: 'dstOffset',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'dstStartRule',
            required: true,
            elementName: {
              localPart: 'dstStartRule',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'HexBinary'
          }, {
            name: 'tzOffset',
            required: true,
            elementName: {
              localPart: 'tzOffset',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }]
      }, {
        localName: 'IdentifiedObject',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'IdentifiedObject'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'batchItemInfo',
            elementName: {
              localPart: 'batchItemInfo',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.BatchItemInfo'
          }]
      }, {
        localName: 'OadrCancelPartyRegistrationType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCancelPartyRegistrationType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'registrationID',
            required: true
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'EiEventSignalType',
        typeName: 'eiEventSignalType',
        propertyInfos: [{
            name: 'intervals',
            required: true,
            elementName: {
              localPart: 'intervals',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
            },
            typeInfo: '.Intervals'
          }, {
            name: 'eiTarget',
            typeInfo: '.EiTargetType'
          }, {
            name: 'signalName',
            required: true
          }, {
            name: 'signalType',
            required: true,
            typeInfo: '.SignalTypeEnumeratedType'
          }, {
            name: 'signalID',
            required: true
          }, {
            name: 'itemBase',
            mixed: false,
            allowDom: false,
            elementName: {
              localPart: 'itemBase',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            },
            typeInfo: '.ItemBaseType',
            type: 'elementRef'
          }, {
            name: 'currentValue',
            typeInfo: '.CurrentValueType'
          }]
      }, {
        localName: 'IntervalBlock',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'IntervalBlock'
        },
        baseTypeInfo: '.IdentifiedObject',
        propertyInfos: [{
            name: 'interval',
            elementName: {
              localPart: 'interval',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.DateTimeInterval'
          }, {
            name: 'intervalReading',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'IntervalReading',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.IntervalReading'
          }]
      }, {
        localName: 'ServiceDeliveryPoint',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ServiceDeliveryPoint'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'name',
            elementName: {
              localPart: 'name',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'tariffProfile',
            elementName: {
              localPart: 'tariffProfile',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'customerAgreement',
            elementName: {
              localPart: 'customerAgreement',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }]
      }, {
        localName: 'RetrievalMethodType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'RetrievalMethodType'
        },
        propertyInfos: [{
            name: 'transforms',
            elementName: {
              localPart: 'Transforms',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.TransformsType'
          }, {
            name: 'uri',
            attributeName: {
              localPart: 'URI'
            },
            type: 'attribute'
          }, {
            name: 'type',
            attributeName: {
              localPart: 'Type'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrGBStreamPayloadBase',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrGBStreamPayloadBase'
        },
        baseTypeInfo: '.StreamPayloadBaseType',
        propertyInfos: [{
            name: 'feed',
            required: true,
            elementName: {
              localPart: 'feed',
              namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
            },
            typeInfo: '.FeedType'
          }]
      }, {
        localName: 'ServiceDeliveryPointType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'ServiceDeliveryPointType'
        },
        propertyInfos: [{
            name: 'node',
            required: true,
            elementName: {
              localPart: 'node',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }]
      }, {
        localName: 'SignatureMethodType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'SignatureMethodType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            allowDom: false,
            elementName: {
              localPart: 'HMACOutputLength',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Integer',
            type: 'elementRef'
          }, {
            name: 'algorithm',
            required: true,
            attributeName: {
              localPart: 'Algorithm'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'OadrCreatedPartyRegistrationType.OadrExtensions',
        typeName: null,
        propertyInfos: [{
            name: 'oadrExtension',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrExtension',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrCreatedPartyRegistrationType.OadrExtensions.OadrExtension'
          }]
      }, {
        localName: 'TransformsType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'TransformsType'
        },
        propertyInfos: [{
            name: 'transform',
            required: true,
            collection: true,
            elementName: {
              localPart: 'Transform',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.TransformType'
          }]
      }, {
        localName: 'DateTimeInterval',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'DateTimeInterval'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'duration',
            elementName: {
              localPart: 'duration',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'UnsignedInt'
          }, {
            name: 'start',
            elementName: {
              localPart: 'start',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }]
      }, {
        localName: 'PrimeFieldParamsType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'PrimeFieldParamsType'
        },
        propertyInfos: [{
            name: 'p',
            required: true,
            elementName: {
              localPart: 'P',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Base64Binary'
          }]
      }, {
        localName: 'Properties.Tolerance.Tolerate',
        typeName: null,
        propertyInfos: [{
            name: 'startafter',
            elementName: {
              localPart: 'startafter',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            }
          }]
      }, {
        localName: 'OadrLoadControlStateTypeType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrLoadControlStateTypeType'
        },
        propertyInfos: [{
            name: 'oadrMin',
            elementName: {
              localPart: 'oadrMin',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Float'
          }, {
            name: 'oadrMax',
            elementName: {
              localPart: 'oadrMax',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Float'
          }, {
            name: 'oadrCurrent',
            required: true,
            elementName: {
              localPart: 'oadrCurrent',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Float'
          }, {
            name: 'oadrNormal',
            elementName: {
              localPart: 'oadrNormal',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Float'
          }]
      }, {
        localName: 'ArrayofResponses',
        propertyInfos: [{
            name: 'response',
            minOccurs: 0,
            collection: true,
            typeInfo: '.EiResponseType'
          }]
      }, {
        localName: 'EnergyApparentType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'EnergyApparentType'
        },
        baseTypeInfo: '.EnergyItemType'
      }, {
        localName: 'OadrReportPayloadType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrReportPayloadType'
        },
        baseTypeInfo: '.ReportPayloadType',
        propertyInfos: [{
            name: 'oadrDataQuality',
            elementName: {
              localPart: 'oadrDataQuality',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }]
      }, {
        localName: 'AggregatedPnodeType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'AggregatedPnodeType'
        },
        propertyInfos: [{
            name: 'node',
            required: true,
            elementName: {
              localPart: 'node',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }]
      }, {
        localName: 'OadrResponseType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrResponseType'
        },
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'venID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'PulseCountType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'pulseCountType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'pulseFactor',
            required: true,
            elementName: {
              localPart: 'pulseFactor',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Float'
          }]
      }, {
        localName: 'ReportPayloadType',
        baseTypeInfo: '.StreamPayloadBaseType',
        propertyInfos: [{
            name: 'rid',
            required: true,
            elementName: 'rID'
          }, {
            name: 'confidence',
            typeInfo: 'UnsignedInt'
          }, {
            name: 'accuracy',
            typeInfo: 'Float'
          }, {
            name: 'payloadBase',
            required: true,
            mixed: false,
            allowDom: false,
            typeInfo: '.PayloadBaseType',
            type: 'elementRef'
          }]
      }, {
        localName: 'TemperatureType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'temperatureType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.TemperatureUnitType'
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'OadrDistributeEventType.OadrEvent',
        typeName: null,
        propertyInfos: [{
            name: 'eiEvent',
            required: true,
            typeInfo: '.EiEventType'
          }, {
            name: 'oadrResponseRequired',
            required: true,
            elementName: {
              localPart: 'oadrResponseRequired',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.ResponseRequiredType'
          }]
      }, {
        localName: 'OadrCancelOptType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrCancelOptType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'optID',
            required: true
          }, {
            name: 'venID',
            required: true
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'EiCreatedEvent',
        typeName: null,
        propertyInfos: [{
            name: 'eiResponse',
            required: true,
            typeInfo: '.EiResponseType'
          }, {
            name: 'eventResponses',
            typeInfo: '.EventResponses'
          }, {
            name: 'venID',
            required: true
          }]
      }, {
        localName: 'SummaryMeasurement',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'SummaryMeasurement'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'powerOfTenMultiplier',
            elementName: {
              localPart: 'powerOfTenMultiplier',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'timeStamp',
            elementName: {
              localPart: 'timeStamp',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'uom',
            elementName: {
              localPart: 'uom',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'value',
            elementName: {
              localPart: 'value',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }]
      }, {
        localName: 'EventDescriptorType.EiMarketContext',
        typeName: null,
        propertyInfos: [{
            name: 'marketContext',
            required: true,
            elementName: {
              localPart: 'marketContext',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            }
          }]
      }, {
        localName: 'AvailableType',
        typeName: {
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0',
          localPart: 'AvailableType'
        },
        propertyInfos: [{
            name: 'properties',
            required: true,
            elementName: {
              localPart: 'properties',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Properties'
          }]
      }, {
        localName: 'FeatureCollection.Location.Polygon',
        typeName: null,
        propertyInfos: [{
            name: 'exterior',
            required: true,
            elementName: {
              localPart: 'exterior',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            typeInfo: '.FeatureCollection.Location.Polygon.Exterior'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'id',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'PayloadBaseType'
      }, {
        localName: 'IntervalType',
        propertyInfos: [{
            name: 'dtstart',
            elementName: {
              localPart: 'dtstart',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Dtstart'
          }, {
            name: 'duration',
            elementName: {
              localPart: 'duration',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.DurationPropType'
          }, {
            name: 'uid',
            elementName: {
              localPart: 'uid',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Uid'
          }, {
            name: 'streamPayloadBase',
            required: true,
            collection: true,
            mixed: false,
            allowDom: false,
            elementName: {
              localPart: 'streamPayloadBase',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
            },
            typeInfo: '.StreamPayloadBaseType',
            type: 'elementRef'
          }]
      }, {
        localName: 'OadrRegisterReportType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrRegisterReportType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'oadrReport',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrReport',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrReportType'
          }, {
            name: 'venID'
          }, {
            name: 'reportRequestID'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'FeatureCollection',
        typeName: null,
        propertyInfos: [{
            name: 'location',
            required: true,
            elementName: {
              localPart: 'location',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            typeInfo: '.FeatureCollection.Location'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'id',
              namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'FieldIDType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'FieldIDType'
        },
        propertyInfos: [{
            name: 'prime',
            required: true,
            elementName: {
              localPart: 'Prime',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.PrimeFieldParamsType'
          }, {
            name: 'tnB',
            required: true,
            elementName: {
              localPart: 'TnB',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.TnBFieldParamsType'
          }, {
            name: 'pnB',
            required: true,
            elementName: {
              localPart: 'PnB',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.PnBFieldParamsType'
          }, {
            name: 'gnB',
            required: true,
            elementName: {
              localPart: 'GnB',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.CharTwoFieldParamsType'
          }, {
            name: 'any',
            required: true,
            mixed: false,
            type: 'anyElement'
          }]
      }, {
        localName: 'FeedType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'feedType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'authorOrCategoryOrContributor',
            minOccurs: 0,
            collection: true,
            mixed: false,
            allowDom: false,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'contributor',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.PersonType'
              }, {
                elementName: {
                  localPart: 'subtitle',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }, {
                elementName: {
                  localPart: 'category',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.CategoryType'
              }, {
                elementName: {
                  localPart: 'entry',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.EntryType'
              }, {
                elementName: {
                  localPart: 'logo',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.LogoType'
              }, {
                elementName: {
                  localPart: 'author',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.PersonType'
              }, {
                elementName: {
                  localPart: 'updated',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.DateTimeType'
              }, {
                elementName: {
                  localPart: 'id',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.IdType'
              }, {
                elementName: {
                  localPart: 'rights',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }, {
                elementName: {
                  localPart: 'link',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.LinkType'
              }, {
                elementName: {
                  localPart: 'generator',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.GeneratorType'
              }, {
                elementName: {
                  localPart: 'icon',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.IconType'
              }, {
                elementName: {
                  localPart: 'title',
                  namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
                },
                typeInfo: '.TextType'
              }],
            type: 'elementRefs'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'PayloadKeyTokenType',
        typeName: {
          namespaceURI: 'http:\/\/oadr.avob.com',
          localPart: 'payloadKeyTokenType'
        },
        baseTypeInfo: '.PayloadBaseType',
        propertyInfos: [{
            name: 'tokens',
            required: true,
            collection: true,
            elementName: {
              localPart: 'tokens',
              namespaceURI: 'http:\/\/oadr.avob.com'
            },
            typeInfo: '.KeyTokenType'
          }]
      }, {
        localName: 'EnergyReactiveType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'EnergyReactiveType'
        },
        baseTypeInfo: '.EnergyItemType'
      }, {
        localName: 'ReadingInterharmonic',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ReadingInterharmonic'
        },
        propertyInfos: [{
            name: 'numerator',
            elementName: {
              localPart: 'numerator',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Integer'
          }, {
            name: 'denominator',
            elementName: {
              localPart: 'denominator',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'AnyType'
          }]
      }, {
        localName: 'ECParametersType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'ECParametersType'
        },
        propertyInfos: [{
            name: 'fieldID',
            required: true,
            elementName: {
              localPart: 'FieldID',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.FieldIDType'
          }, {
            name: 'curve',
            required: true,
            elementName: {
              localPart: 'Curve',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.CurveType'
          }, {
            name: 'base',
            required: true,
            elementName: {
              localPart: 'Base',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'order',
            required: true,
            elementName: {
              localPart: 'Order',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'coFactor',
            elementName: {
              localPart: 'CoFactor',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Integer'
          }, {
            name: 'validationData',
            elementName: {
              localPart: 'ValidationData',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: '.ECValidationDataType'
          }]
      }, {
        localName: 'Properties',
        typeName: null,
        propertyInfos: [{
            name: 'dtstart',
            required: true,
            elementName: {
              localPart: 'dtstart',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Dtstart'
          }, {
            name: 'duration',
            required: true,
            elementName: {
              localPart: 'duration',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.DurationPropType'
          }, {
            name: 'tolerance',
            elementName: {
              localPart: 'tolerance',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Properties.Tolerance'
          }, {
            name: 'xEiNotification',
            elementName: 'x-eiNotification',
            typeInfo: '.DurationPropType'
          }, {
            name: 'xEiRampUp',
            elementName: 'x-eiRampUp',
            typeInfo: '.DurationPropType'
          }, {
            name: 'xEiRecovery',
            elementName: 'x-eiRecovery',
            typeInfo: '.DurationPropType'
          }]
      }, {
        localName: 'SignalPayloadType',
        typeName: 'signalPayloadType',
        baseTypeInfo: '.StreamPayloadBaseType',
        propertyInfos: [{
            name: 'payloadBase',
            required: true,
            mixed: false,
            allowDom: false,
            typeInfo: '.PayloadBaseType',
            type: 'elementRef'
          }]
      }, {
        localName: 'BatchItemInfo',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'BatchItemInfo'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'name',
            elementName: {
              localPart: 'name',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'HexBinary'
          }, {
            name: 'operation',
            elementName: {
              localPart: 'operation',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'statusCode',
            elementName: {
              localPart: 'statusCode',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'statusReason',
            elementName: {
              localPart: 'statusReason',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }]
      }, {
        localName: 'EndDeviceAssetType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'EndDeviceAssetType'
        },
        propertyInfos: [{
            name: 'mrid',
            required: true,
            elementName: {
              localPart: 'mrid',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }]
      }, {
        localName: 'UriType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'uriType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'value',
            type: 'value'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'GeneratorType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'generatorType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'value',
            type: 'value'
          }, {
            name: 'uri',
            attributeName: {
              localPart: 'uri'
            },
            type: 'attribute'
          }, {
            name: 'version',
            attributeName: {
              localPart: 'version'
            },
            type: 'attribute'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'CurrentType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'CurrentType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'KeyInfoType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'KeyInfoType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            elementTypeInfos: [{
                elementName: {
                  localPart: 'PGPData',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.PGPDataType'
              }, {
                elementName: {
                  localPart: 'KeyValue',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.KeyValueType'
              }, {
                elementName: {
                  localPart: 'RetrievalMethod',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.RetrievalMethodType'
              }, {
                elementName: {
                  localPart: 'SPKIData',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.SPKIDataType'
              }, {
                elementName: {
                  localPart: 'MgmtData',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                }
              }, {
                elementName: {
                  localPart: 'KeyName',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                }
              }, {
                elementName: {
                  localPart: 'X509Data',
                  namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
                },
                typeInfo: '.X509DataType'
              }],
            type: 'elementRefs'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'MeterReading',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'MeterReading'
        },
        baseTypeInfo: '.IdentifiedObject'
      }, {
        localName: 'CurveType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'CurveType'
        },
        propertyInfos: [{
            name: 'a',
            required: true,
            elementName: {
              localPart: 'A',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'b',
            required: true,
            elementName: {
              localPart: 'B',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'Base64Binary'
          }]
      }, {
        localName: 'ReferenceType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'ReferenceType'
        },
        propertyInfos: [{
            name: 'transforms',
            elementName: {
              localPart: 'Transforms',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.TransformsType'
          }, {
            name: 'digestMethod',
            required: true,
            elementName: {
              localPart: 'DigestMethod',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.DigestMethodType'
          }, {
            name: 'digestValue',
            required: true,
            elementName: {
              localPart: 'DigestValue',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }, {
            name: 'uri',
            attributeName: {
              localPart: 'URI'
            },
            type: 'attribute'
          }, {
            name: 'type',
            attributeName: {
              localPart: 'Type'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'CategoryType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'categoryType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'term',
            required: true,
            attributeName: {
              localPart: 'term'
            },
            type: 'attribute'
          }, {
            name: 'scheme',
            attributeName: {
              localPart: 'scheme'
            },
            type: 'attribute'
          }, {
            name: 'label',
            attributeName: {
              localPart: 'label'
            },
            type: 'attribute'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'EiEventType',
        typeName: 'eiEventType',
        propertyInfos: [{
            name: 'eventDescriptor',
            required: true,
            typeInfo: '.EventDescriptorType'
          }, {
            name: 'eiActivePeriod',
            required: true,
            typeInfo: '.EiActivePeriodType'
          }, {
            name: 'eiEventSignals',
            required: true,
            typeInfo: '.EiEventSignalsType'
          }, {
            name: 'eiTarget',
            required: true,
            typeInfo: '.EiTargetType'
          }]
      }, {
        localName: 'ObjectType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'ObjectType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            type: 'anyElement'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }, {
            name: 'mimeType',
            attributeName: {
              localPart: 'MimeType'
            },
            type: 'attribute'
          }, {
            name: 'encoding',
            attributeName: {
              localPart: 'Encoding'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'AvobVenServiceRequestType',
        typeName: {
          namespaceURI: 'http:\/\/oadr.avob.com',
          localPart: 'avobVenServiceRequestType'
        },
        propertyInfos: [{
            name: 'serviceName',
            required: true,
            elementName: {
              localPart: 'serviceName',
              namespaceURI: 'http:\/\/oadr.avob.com'
            }
          }, {
            name: 'requestId',
            required: true,
            elementName: {
              localPart: 'requestId',
              namespaceURI: 'http:\/\/oadr.avob.com'
            }
          }, {
            name: 'command',
            required: true,
            elementName: {
              localPart: 'command',
              namespaceURI: 'http:\/\/oadr.avob.com'
            }
          }, {
            name: 'refresh',
            required: true,
            elementName: {
              localPart: 'refresh',
              namespaceURI: 'http:\/\/oadr.avob.com'
            },
            typeInfo: 'Boolean'
          }]
      }, {
        localName: 'OadrRequestEventType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrRequestEventType'
        },
        propertyInfos: [{
            name: 'eiRequestEvent',
            required: true,
            elementName: {
              localPart: 'eiRequestEvent',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            },
            typeInfo: '.EiRequestEvent'
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'ReadingType',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'ReadingType'
        },
        baseTypeInfo: '.IdentifiedObject',
        propertyInfos: [{
            name: 'accumulationBehaviour',
            elementName: {
              localPart: 'accumulationBehaviour',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'commodity',
            elementName: {
              localPart: 'commodity',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'consumptionTier',
            elementName: {
              localPart: 'consumptionTier',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Short'
          }, {
            name: 'currency',
            elementName: {
              localPart: 'currency',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'dataQualifier',
            elementName: {
              localPart: 'dataQualifier',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'defaultQuality',
            elementName: {
              localPart: 'defaultQuality',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'flowDirection',
            elementName: {
              localPart: 'flowDirection',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'intervalLength',
            elementName: {
              localPart: 'intervalLength',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'UnsignedInt'
          }, {
            name: 'kind',
            elementName: {
              localPart: 'kind',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'phase',
            elementName: {
              localPart: 'phase',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'powerOfTenMultiplier',
            elementName: {
              localPart: 'powerOfTenMultiplier',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'timeAttribute',
            elementName: {
              localPart: 'timeAttribute',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'tou',
            elementName: {
              localPart: 'tou',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Short'
          }, {
            name: 'uom',
            elementName: {
              localPart: 'uom',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'cpp',
            elementName: {
              localPart: 'cpp',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Short'
          }, {
            name: 'interharmonic',
            elementName: {
              localPart: 'interharmonic',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.ReadingInterharmonic'
          }, {
            name: 'measuringPeriod',
            elementName: {
              localPart: 'measuringPeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            }
          }, {
            name: 'argument',
            elementName: {
              localPart: 'argument',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.RationalNumber'
          }]
      }, {
        localName: 'PnodeType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'PnodeType'
        },
        propertyInfos: [{
            name: 'node',
            required: true,
            elementName: {
              localPart: 'node',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }]
      }, {
        localName: 'EiEventBaselineType',
        typeName: 'eiEventBaselineType',
        propertyInfos: [{
            name: 'dtstart',
            required: true,
            elementName: {
              localPart: 'dtstart',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Dtstart'
          }, {
            name: 'duration',
            required: true,
            elementName: {
              localPart: 'duration',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.DurationPropType'
          }, {
            name: 'intervals',
            required: true,
            elementName: {
              localPart: 'intervals',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
            },
            typeInfo: '.Intervals'
          }, {
            name: 'baselineID',
            required: true
          }, {
            name: 'resourceID',
            minOccurs: 0,
            collection: true
          }, {
            name: 'baselineName',
            required: true
          }, {
            name: 'itemBase',
            mixed: false,
            allowDom: false,
            elementName: {
              localPart: 'itemBase',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
            },
            typeInfo: '.ItemBaseType',
            type: 'elementRef'
          }]
      }, {
        localName: 'VoltageType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'VoltageType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'LogoType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom',
          localPart: 'logoType'
        },
        propertyInfos: [{
            name: 'otherAttributes',
            type: 'anyAttribute'
          }, {
            name: 'value',
            type: 'value'
          }, {
            name: 'base',
            attributeName: {
              localPart: 'base',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }, {
            name: 'lang',
            typeInfo: 'Language',
            attributeName: {
              localPart: 'lang',
              namespaceURI: 'http:\/\/www.w3.org\/XML\/1998\/namespace'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'StreamPayloadBaseType',
        typeName: {
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream',
          localPart: 'StreamPayloadBaseType'
        }
      }, {
        localName: 'EnergyItemType',
        typeName: {
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power',
          localPart: 'EnergyItemType'
        },
        baseTypeInfo: '.ItemBaseType',
        propertyInfos: [{
            name: 'itemDescription',
            required: true,
            elementName: {
              localPart: 'itemDescription',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }, {
            name: 'itemUnits',
            required: true,
            elementName: {
              localPart: 'itemUnits',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
            }
          }, {
            name: 'siScaleCode',
            required: true,
            elementName: {
              localPart: 'siScaleCode',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
            },
            values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none']
          }]
      }, {
        localName: 'OadrRequestReregistrationType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrRequestReregistrationType'
        },
        propertyInfos: [{
            name: 'venID',
            required: true
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'SignaturePropertyType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'SignaturePropertyType'
        },
        propertyInfos: [{
            name: 'content',
            collection: true,
            type: 'anyElement'
          }, {
            name: 'target',
            required: true,
            attributeName: {
              localPart: 'Target'
            },
            type: 'attribute'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'CharTwoFieldParamsType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#',
          localPart: 'CharTwoFieldParamsType'
        },
        propertyInfos: [{
            name: 'm',
            required: true,
            elementName: {
              localPart: 'M',
              namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
            },
            typeInfo: 'PositiveInteger'
          }]
      }, {
        localName: 'OadrServiceSpecificInfo',
        typeName: null,
        propertyInfos: [{
            name: 'oadrService',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'oadrService',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: '.OadrServiceSpecificInfo.OadrService'
          }]
      }, {
        localName: 'PayloadFloatType',
        baseTypeInfo: '.PayloadBaseType',
        propertyInfos: [{
            name: 'value',
            required: true,
            typeInfo: 'Float'
          }]
      }, {
        localName: 'IntervalReading',
        typeName: {
          namespaceURI: 'http:\/\/naesb.org\/espi',
          localPart: 'IntervalReading'
        },
        baseTypeInfo: '.Object',
        propertyInfos: [{
            name: 'cost',
            elementName: {
              localPart: 'cost',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }, {
            name: 'readingQuality',
            minOccurs: 0,
            collection: true,
            elementName: {
              localPart: 'ReadingQuality',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.ReadingQuality'
          }, {
            name: 'timePeriod',
            elementName: {
              localPart: 'timePeriod',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: '.DateTimeInterval'
          }, {
            name: 'value',
            elementName: {
              localPart: 'value',
              namespaceURI: 'http:\/\/naesb.org\/espi'
            },
            typeInfo: 'Long'
          }]
      }, {
        localName: 'SignatureValueType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'SignatureValueType'
        },
        propertyInfos: [{
            name: 'value',
            typeInfo: 'Base64Binary',
            type: 'value'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'SignaturePropertiesType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'SignaturePropertiesType'
        },
        propertyInfos: [{
            name: 'signatureProperty',
            required: true,
            collection: true,
            elementName: {
              localPart: 'SignatureProperty',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: '.SignaturePropertyType'
          }, {
            name: 'id',
            typeInfo: 'ID',
            attributeName: {
              localPart: 'Id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'Uid',
        typeName: null,
        propertyInfos: [{
            name: 'text',
            required: true,
            elementName: {
              localPart: 'text',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            }
          }]
      }, {
        localName: 'OadrSamplingRateType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrSamplingRateType'
        },
        propertyInfos: [{
            name: 'oadrMinPeriod',
            required: true,
            elementName: {
              localPart: 'oadrMinPeriod',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'oadrMaxPeriod',
            required: true,
            elementName: {
              localPart: 'oadrMaxPeriod',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            }
          }, {
            name: 'oadrOnChange',
            required: true,
            elementName: {
              localPart: 'oadrOnChange',
              namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
            },
            typeInfo: 'Boolean'
          }]
      }, {
        localName: 'OadrQueryRegistrationType',
        typeName: {
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07',
          localPart: 'oadrQueryRegistrationType'
        },
        propertyInfos: [{
            name: 'requestID',
            required: true,
            elementName: {
              localPart: 'requestID',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
            }
          }, {
            name: 'schemaVersion',
            attributeName: {
              localPart: 'schemaVersion',
              namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'WsCalendarIntervalType',
        typeName: {
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0',
          localPart: 'WsCalendarIntervalType'
        },
        propertyInfos: [{
            name: 'properties',
            required: true,
            elementName: {
              localPart: 'properties',
              namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
            },
            typeInfo: '.Properties'
          }]
      }, {
        localName: 'SPKIDataType',
        typeName: {
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#',
          localPart: 'SPKIDataType'
        },
        propertyInfos: [{
            name: 'spkiSexpAndAny',
            required: true,
            collection: true,
            mixed: false,
            elementName: {
              localPart: 'SPKISexp',
              namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
            },
            typeInfo: 'Base64Binary',
            type: 'elementRef'
          }]
      }, {
        type: 'enumInfo',
        localName: 'SignalTypeEnumeratedType',
        baseTypeInfo: 'Token',
        values: ['delta', 'level', 'multiplier', 'price', 'priceMultiplier', 'priceRelative', 'setpoint', 'x-loadControlCapacity', 'x-loadControlLevelOffset', 'x-loadControlPercentOffset', 'x-loadControlSetpoint']
      }, {
        type: 'enumInfo',
        localName: 'CurrencyItemDescriptionType',
        baseTypeInfo: 'Token',
        values: ['currency', 'currencyPerKW', 'currencyPerKWh']
      }, {
        type: 'enumInfo',
        localName: 'OptTypeType',
        baseTypeInfo: 'Token',
        values: ['optIn', 'optOut']
      }, {
        type: 'enumInfo',
        localName: 'TemperatureUnitType',
        baseTypeInfo: 'Token',
        values: ['celsius', 'fahrenheit']
      }, {
        type: 'enumInfo',
        localName: 'OadrDataQualityType',
        baseTypeInfo: 'Token',
        values: ['No Quality - No Value', 'No New Value - Previous Value Used', 'Quality Bad - Non Specific', 'Quality Bad - Configuration Error', 'Quality Bad - Not Connected', 'Quality Bad - Device Failure', 'Quality Bad - Sensor Failure', 'Quality Bad - Last Known Value', 'Quality Bad - Comm Failure', 'Quality Bad - Out of Service', 'Quality Uncertain - Non Specific', 'Quality Uncertain - Last Usable Value', 'Quality Uncertain - Sensor Not Accurate', 'Quality Uncertain - EU Units Exceeded', 'Quality Uncertain - Sub Normal', 'Quality Good - Non Specific', 'Quality Good - Local Override', 'Quality Limit - Field\/Not', 'Quality Limit - Field\/Low', 'Quality Limit - Field\/High', 'Quality Limit - Field\/Constant']
      }, {
        type: 'enumInfo',
        localName: 'OadrServiceNameType',
        baseTypeInfo: 'Token',
        values: ['EiEvent', 'EiOpt', 'EiReport', 'EiRegisterParty', 'OadrPoll']
      }, {
        type: 'enumInfo',
        localName: 'EventStatusEnumeratedType',
        baseTypeInfo: 'Token',
        values: ['none', 'far', 'near', 'active', 'completed', 'cancelled']
      }, {
        type: 'enumInfo',
        localName: 'ReportEnumeratedType',
        baseTypeInfo: 'Token',
        values: ['reading', 'usage', 'demand', 'setPoint', 'deltaUsage', 'deltaSetPoint', 'deltaDemand', 'baseline', 'deviation', 'avgUsage', 'avgDemand', 'operatingState', 'upRegulationCapacityAvailable', 'downRegulationCapacityAvailable', 'regulationSetpoint', 'storedEnergy', 'targetEnergyStorage', 'availableEnergyStorage', 'price', 'level', 'powerFactor', 'percentUsage', 'percentDemand', 'x-resourceStatus']
      }, {
        type: 'enumInfo',
        localName: 'ISO3AlphaCurrencyCodeContentType',
        baseTypeInfo: 'Token',
        values: ['AED', 'AFN', 'ALL', 'AMD', 'ANG', 'AOA', 'ARS', 'AUD', 'AWG', 'AZN', 'BAM', 'BBD', 'BDT', 'BGN', 'BHD', 'BIF', 'BMD', 'BND', 'BOB', 'BOV', 'BRL', 'BSD', 'BTN', 'BWP', 'BYR', 'BZD', 'CAD', 'CDF', 'CHE', 'CHF', 'CHW', 'CLF', 'CLP', 'CNY', 'COP', 'COU', 'CRC', 'CUC', 'CUP', 'CVE', 'CZK', 'DJF', 'DKK', 'DOP', 'DZD', 'EEK', 'EGP', 'ERN', 'ETB', 'EUR', 'FJD', 'FKP', 'GBP', 'GEL', 'GHS', 'GIP', 'GMD', 'GNF', 'GTQ', 'GWP', 'GYD', 'HKD', 'HNL', 'HRK', 'HTG', 'HUF', 'IDR', 'ILS', 'INR', 'IQD', 'IRR', 'ISK', 'JMD', 'JOD', 'JPY', 'KES', 'KGS', 'KHR', 'KMF', 'KPW', 'KRW', 'KWD', 'KYD', 'KZT', 'LAK', 'LBP', 'LKR', 'LRD', 'LSL', 'LTL', 'LVL', 'LYD', 'MAD', 'MDL', 'MGA', 'MKD', 'MMK', 'MNT', 'MOP', 'MRO', 'MUR', 'MVR', 'MWK', 'MXN', 'MXV', 'MYR', 'MZN', 'NAD', 'NGN', 'NIO', 'NOK', 'NPR', 'NZD', 'OMR', 'PAB', 'PEN', 'PGK', 'PHP', 'PKR', 'PLN', 'PYG', 'QAR', 'RON', 'RSD', 'RUB', 'RWF', 'SAR', 'SBD', 'SCR', 'SDG', 'SEK', 'SGD', 'SHP', 'SLL', 'SOS', 'SRD', 'STD', 'SVC', 'SYP', 'SZL', 'THB', 'TJS', 'TMT', 'TND', 'TOP', 'TRY', 'TTD', 'TWD', 'TZS', 'UAH', 'UGX', 'USD', 'USN', 'USS', 'UYI', 'UYU', 'UZS', 'VEF', 'VND', 'VUV', 'WST', 'XAF', 'XAG', 'XAU', 'XBA', 'XBB', 'XBC', 'XBD', 'XCD', 'XDR', 'XFU', 'XOF', 'XPD', 'XPF', 'XPT', 'XTS', 'XXX', 'YER', 'ZAR', 'ZMK', 'ZWL']
      }, {
        type: 'enumInfo',
        localName: 'ResponseRequiredType',
        values: ['always', 'never']
      }, {
        type: 'enumInfo',
        localName: 'ReportNameEnumeratedType',
        baseTypeInfo: 'Token',
        values: ['METADATA_HISTORY_USAGE', 'HISTORY_USAGE', 'METADATA_HISTORY_GREENBUTTON', 'HISTORY_GREENBUTTON', 'METADATA_TELEMETRY_USAGE', 'TELEMETRY_USAGE', 'METADATA_TELEMETRY_STATUS', 'TELEMETRY_STATUS']
      }, {
        type: 'enumInfo',
        localName: 'ReadingTypeEnumeratedType',
        baseTypeInfo: 'Token',
        values: ['Direct Read', 'Net', 'Allocated', 'Estimated', 'Summed', 'Derived', 'Mean', 'Peak', 'Hybrid', 'Contract', 'Projected', 'x-RMS', 'x-notApplicable']
      }, {
        type: 'enumInfo',
        localName: 'OadrTransportType',
        baseTypeInfo: 'Token',
        values: ['simpleHttp', 'xmpp']
      }, {
        type: 'enumInfo',
        localName: 'OptReasonEnumeratedType',
        baseTypeInfo: 'Token',
        values: ['economic', 'emergency', 'mustRun', 'notParticipating', 'outageRunStatus', 'overrideStatus', 'participating', 'x-schedule']
      }],
    elementInfos: [{
        typeInfo: '.PayloadFloatType',
        elementName: 'payloadFloat',
        substitutionHead: 'payloadBase'
      }, {
        elementName: {
          localPart: 'KeyName',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.SummaryMeasurement',
        elementName: {
          localPart: 'SummaryMeasurement',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.ObjectType',
        elementName: {
          localPart: 'Object',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: 'Base64Binary',
        elementName: {
          localPart: 'PGPKeyID',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.PGPDataType'
      }, {
        typeInfo: 'Base64Binary',
        elementName: {
          localPart: 'PGPKeyPacket',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.PGPDataType'
      }, {
        typeInfo: '.CurrencyType',
        elementName: {
          localPart: 'currencyPerKW',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        elementName: 'readingType'
      }, {
        typeInfo: '.IntervalBlock',
        elementName: {
          localPart: 'IntervalBlock',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        values: ['p', 'n', 'micro', 'm', 'c', 'd', 'k', 'M', 'G', 'T', 'none'],
        elementName: {
          localPart: 'siScaleCode',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/siscale'
        }
      }, {
        typeInfo: '.SignatureMethodType',
        elementName: {
          localPart: 'SignatureMethod',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: 'Base64Binary',
        elementName: {
          localPart: 'SPKISexp',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.SPKIDataType'
      }, {
        typeInfo: '.OadrUpdateReportType',
        elementName: {
          localPart: 'oadrUpdateReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: 'reportRequestID'
      }, {
        typeInfo: '.SPKIDataType',
        elementName: {
          localPart: 'SPKIData',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.OadrCreatedPartyRegistrationType',
        elementName: {
          localPart: 'oadrCreatedPartyRegistration',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.FeedType',
        elementName: {
          localPart: 'feed',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        }
      }, {
        typeInfo: '.CategoryType',
        elementName: {
          localPart: 'category',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.TnBFieldParamsType',
        elementName: {
          localPart: 'TnB',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: '.SignedInfoType',
        elementName: {
          localPart: 'SignedInfo',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: 'NormalizedString',
        elementName: {
          localPart: 'email',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.PersonType'
      }, {
        typeInfo: 'Boolean',
        elementName: {
          localPart: 'oadrReportOnly',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.ReadingTypeEnumeratedType',
        elementName: 'readingTypeEnumerated'
      }, {
        typeInfo: '.ReportEnumeratedType',
        elementName: 'reportEnumerated'
      }, {
        typeInfo: '.Uid',
        elementName: {
          localPart: 'uid',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: 'AnyType',
        elementName: {
          localPart: 'components',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.ThermType',
        elementName: {
          localPart: 'Therm',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.PersonType',
        elementName: {
          localPart: 'contributor',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.OadrCreatePartyRegistrationType',
        elementName: {
          localPart: 'oadrCreatePartyRegistration',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.SignaturePropertiesType',
        elementName: {
          localPart: 'SignatureProperties',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.PulseCountType',
        elementName: {
          localPart: 'pulseCount',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.OadrRegisteredReportType',
        elementName: {
          localPart: 'oadrRegisteredReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.X509DigestType',
        elementName: {
          localPart: 'X509Digest',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: '.PnBFieldParamsType',
        elementName: {
          localPart: 'PnB',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: '.EntryType',
        elementName: {
          localPart: 'entry',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.SpecifierPayloadType',
        elementName: 'specifierPayload'
      }, {
        typeInfo: '.CharTwoFieldParamsType',
        elementName: {
          localPart: 'GnB',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: '.LinkType',
        elementName: {
          localPart: 'link',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.OadrPayloadResourceStatusType',
        elementName: {
          localPart: 'oadrPayloadResourceStatus',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: 'payloadBase'
      }, {
        typeInfo: '.UsagePoint',
        elementName: {
          localPart: 'UsagePoint',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.OadrCanceledPartyRegistrationType',
        elementName: {
          localPart: 'oadrCanceledPartyRegistration',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.RetrievalMethodType',
        elementName: {
          localPart: 'RetrievalMethod',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.ElectricPowerQualitySummary',
        elementName: {
          localPart: 'ElectricPowerQualitySummary',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        elementName: 'venID'
      }, {
        typeInfo: '.OadrRegisterReportType',
        elementName: {
          localPart: 'oadrRegisterReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: 'Base64Binary',
        elementName: {
          localPart: 'X509SKI',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.X509DataType'
      }, {
        typeInfo: '.EiTargetType',
        elementName: {
          localPart: 'oadrDeviceClass',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.DateTimeType',
        elementName: {
          localPart: 'updated',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.Dtend',
        elementName: {
          localPart: 'dtend',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.OadrCreateReportType',
        elementName: {
          localPart: 'oadrCreateReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.PowerRealType',
        elementName: {
          localPart: 'powerReal',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'powerItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        elementName: 'reportSpecifierID'
      }, {
        typeInfo: '.TransformType',
        elementName: {
          localPart: 'Transform',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.DateTimeType',
        elementName: {
          localPart: 'published',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.ArrayofResponses',
        elementName: 'responses'
      }, {
        typeInfo: '.EnergyApparentType',
        elementName: {
          localPart: 'energyApparent',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'energyItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.IdType',
        elementName: {
          localPart: 'id',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.EiTargetType',
        elementName: 'reportDataSource'
      }, {
        typeInfo: '.Object',
        elementName: {
          localPart: 'Object',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.OadrCancelOptType',
        elementName: {
          localPart: 'oadrCancelOpt',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.OadrReportRequestType',
        elementName: {
          localPart: 'oadrReportRequest',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.EiTargetType',
        elementName: 'eiTarget'
      }, {
        typeInfo: '.CurrencyType',
        elementName: {
          localPart: 'currency',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.FeatureCollection',
        elementName: {
          localPart: 'FeatureCollection',
          namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
        }
      }, {
        typeInfo: '.PnodeType',
        elementName: {
          localPart: 'pnode',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.AggregatedPnodeType',
        elementName: {
          localPart: 'aggregatedPnode',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.OptReasonEnumeratedType',
        elementName: 'optReasonEnumerated'
      }, {
        typeInfo: '.PayloadAvobVenServiceRequestType',
        elementName: {
          localPart: 'payloadAvobVenServiceRequest',
          namespaceURI: 'http:\/\/oadr.avob.com'
        },
        substitutionHead: 'payloadBase'
      }, {
        typeInfo: '.VavailabilityType',
        elementName: {
          localPart: 'vavailability',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        elementName: {
          localPart: 'name',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.PersonType'
      }, {
        typeInfo: '.IntervalReading',
        elementName: {
          localPart: 'IntervalReading',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.EventResponses',
        elementName: 'eventResponses'
      }, {
        typeInfo: '.EiRequestEvent',
        elementName: {
          localPart: 'eiRequestEvent',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
        }
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'subtitle',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.EiActivePeriodType',
        elementName: 'eiActivePeriod'
      }, {
        elementName: {
          localPart: 'mrid',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.PowerApparentType',
        elementName: {
          localPart: 'powerApparent',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'powerItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.OadrServiceSpecificInfo',
        elementName: {
          localPart: 'oadrServiceSpecificInfo',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.DurationPropType',
        elementName: {
          localPart: 'granularity',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        elementName: 'reportType'
      }, {
        typeInfo: '.ServiceStatus',
        elementName: {
          localPart: 'ServiceStatus',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.RSAKeyValueType',
        elementName: {
          localPart: 'RSAKeyValue',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: 'Base64Binary',
        elementName: {
          localPart: 'X509Certificate',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.X509DataType'
      }, {
        typeInfo: '.OadrCreatedEventType',
        elementName: {
          localPart: 'oadrCreatedEvent',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.Intervals',
        elementName: {
          localPart: 'intervals',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
        }
      }, {
        typeInfo: '.PersonType',
        elementName: {
          localPart: 'contributor',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.DigestMethodType',
        elementName: {
          localPart: 'DigestMethod',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.OadrReportPayloadType',
        elementName: {
          localPart: 'oadrReportPayload',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'streamPayloadBase',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
        }
      }, {
        typeInfo: '.OadrServiceNameType',
        elementName: {
          localPart: 'oadrServiceName',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: 'DateTime',
        elementName: 'createdDateTime'
      }, {
        typeInfo: '.ContentType',
        elementName: {
          localPart: 'content',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.ECKeyValueType',
        elementName: {
          localPart: 'ECKeyValue',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: 'Float',
        elementName: {
          localPart: 'pulseFactor',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.LinkType',
        elementName: {
          localPart: 'link',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.ReferenceType',
        elementName: {
          localPart: 'Reference',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        elementName: 'optID'
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'title',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.EiCreatedEvent',
        elementName: {
          localPart: 'eiCreatedEvent',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
        }
      }, {
        typeInfo: '.ResponseRequiredType',
        elementName: {
          localPart: 'oadrResponseRequired',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.PowerAttributesType',
        elementName: {
          localPart: 'powerAttributes',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.OadrInfo',
        elementName: {
          localPart: 'oadrInfo',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: 'UnsignedInt',
        elementName: 'numDataSources'
      }, {
        typeInfo: '.PayloadBaseType',
        elementName: 'payloadBase'
      }, {
        elementName: 'responseCode'
      }, {
        typeInfo: '.EiTargetType',
        elementName: 'reportSubject'
      }, {
        typeInfo: '.OadrTransports',
        elementName: {
          localPart: 'oadrTransports',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.OadrCreatedReportType',
        elementName: {
          localPart: 'oadrCreatedReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.OadrGBStreamPayloadBase',
        elementName: {
          localPart: 'oadrGBPayload',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'streamPayloadBase',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
        }
      }, {
        typeInfo: '.OadrPendingReportsType',
        elementName: {
          localPart: 'oadrPendingReports',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: 'reportName'
      }, {
        elementName: {
          localPart: 'oadrVenName',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.StreamPayloadBaseType',
        elementName: {
          localPart: 'streamPayloadBase',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
        }
      }, {
        typeInfo: '.OadrProfiles',
        elementName: {
          localPart: 'oadrProfiles',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: 'UnsignedInt',
        elementName: {
          localPart: 'replyLimit',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
        }
      }, {
        typeInfo: 'UnsignedInt',
        elementName: 'confidence'
      }, {
        elementName: {
          localPart: 'requestID',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
        }
      }, {
        elementName: 'groupName'
      }, {
        typeInfo: '.ManifestType',
        elementName: {
          localPart: 'Manifest',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.PayloadKeyTokenType',
        elementName: {
          localPart: 'payloadKeyToken',
          namespaceURI: 'http:\/\/oadr.avob.com'
        },
        substitutionHead: 'payloadBase'
      }, {
        typeInfo: '.AvailableType',
        elementName: {
          localPart: 'available',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.ItemBaseType',
        elementName: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        elementName: 'refID',
        substitutionHead: 'uid'
      }, {
        typeInfo: '.OadrReportType',
        elementName: {
          localPart: 'oadrReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: {
          localPart: 'XPath',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.TransformType'
      }, {
        elementName: {
          localPart: 'oadrTransportAddress',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: 'Token',
        values: ['2.0a', '2.0b'],
        elementName: {
          localPart: 'oadrProfileName',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.ServiceLocationType',
        elementName: {
          localPart: 'serviceLocation',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: {
          type: 'list',
          baseTypeInfo: 'Double'
        },
        elementName: {
          localPart: 'posList',
          namespaceURI: 'http:\/\/www.opengis.net\/gml\/3.2'
        }
      }, {
        typeInfo: '.ReadingType',
        elementName: {
          localPart: 'ReadingType',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.TemperatureType',
        elementName: {
          localPart: 'temperature',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.ReportSpecifierType',
        elementName: 'reportSpecifier'
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'title',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.OadrDistributeEventType',
        elementName: {
          localPart: 'oadrDistributeEvent',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'rights',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.DurationPropType',
        elementName: {
          localPart: 'oadrRequestedOadrPollFreq',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: 'Base64Binary',
        elementName: {
          localPart: 'X509CRL',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.X509DataType'
      }, {
        typeInfo: '.PersonType',
        elementName: {
          localPart: 'author',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.OadrCancelPartyRegistrationType',
        elementName: {
          localPart: 'oadrCancelPartyRegistration',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: {
          localPart: 'MgmtData',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: 'Base64Binary',
        elementName: {
          localPart: 'DigestValue',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.PersonType',
        elementName: {
          localPart: 'author',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.VoltageType',
        elementName: {
          localPart: 'voltage',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.OptTypeType',
        elementName: 'optType'
      }, {
        typeInfo: '.MeterAssetType',
        elementName: {
          localPart: 'meterAsset',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.X509IssuerSerialType',
        elementName: {
          localPart: 'X509IssuerSerial',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.X509DataType'
      }, {
        typeInfo: '.EiEventType',
        elementName: 'eiEvent'
      }, {
        elementName: 'registrationID',
        substitutionHead: 'refID'
      }, {
        typeInfo: 'Boolean',
        elementName: {
          localPart: 'reportToFollow',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/energyinterop\/201110\/payloads'
        }
      }, {
        typeInfo: '.DEREncodedKeyValueType',
        elementName: {
          localPart: 'DEREncodedKeyValue',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: '.OadrResponseType',
        elementName: {
          localPart: 'oadrResponse',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.DurationPropType',
        elementName: 'x-eiRampUp'
      }, {
        typeInfo: 'DateTime',
        elementName: 'statusDateTime'
      }, {
        typeInfo: '.PersonType',
        elementName: {
          localPart: 'author',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.EndDeviceAssetType',
        elementName: {
          localPart: 'endDeviceAsset',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.UriType',
        elementName: {
          localPart: 'uri',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.PersonType'
      }, {
        typeInfo: '.EiResponseType',
        elementName: 'eiResponse'
      }, {
        typeInfo: '.DateTimeInterval',
        elementName: {
          localPart: 'DateTimeInterval',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.LinkType',
        elementName: {
          localPart: 'link',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: 'Integer',
        elementName: {
          localPart: 'HMACOutputLength',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.SignatureMethodType'
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'summary',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.IconType',
        elementName: {
          localPart: 'icon',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        elementName: 'signalName'
      }, {
        typeInfo: '.DurationPropType',
        elementName: 'x-eiRecovery'
      }, {
        typeInfo: '.CategoryType',
        elementName: {
          localPart: 'category',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        elementName: 'rID'
      }, {
        typeInfo: '.EntryType',
        elementName: {
          localPart: 'entry',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        }
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'title',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.ServiceAreaType',
        elementName: {
          localPart: 'serviceArea',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.CurrentType',
        elementName: {
          localPart: 'current',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.PowerItemType',
        elementName: {
          localPart: 'powerItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        elementName: 'responseDescription'
      }, {
        typeInfo: '.SignatureValueType',
        elementName: {
          localPart: 'SignatureValue',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: 'Boolean',
        elementName: {
          localPart: 'oadrHttpPullModel',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: 'partyID'
      }, {
        typeInfo: '.Dtstart',
        elementName: {
          localPart: 'dtstart',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.KeyValueType',
        elementName: {
          localPart: 'KeyValue',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        elementName: 'optReason'
      }, {
        typeInfo: '.CanonicalizationMethodType',
        elementName: {
          localPart: 'CanonicalizationMethod',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.DurationPropType',
        elementName: 'x-eiNotification'
      }, {
        typeInfo: 'Token',
        values: ['SIMPLE', 'simple', 'ELECTRICITY_PRICE', 'ENERGY_PRICE', 'DEMAND_CHARGE', 'BID_PRICE', 'BID_LOAD', 'BID_ENERGY', 'CHARGE_STATE', 'LOAD_DISPATCH', 'LOAD_CONTROL'],
        elementName: 'SignalNameEnumerated'
      }, {
        typeInfo: '.MeterReading',
        elementName: {
          localPart: 'MeterReading',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.EiEventSignalType',
        elementName: 'eiEventSignal'
      }, {
        typeInfo: '.OadrGBItemBase',
        elementName: {
          localPart: 'oadrGBDataDescription',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.TransformsType',
        elementName: {
          localPart: 'Transforms',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: 'DateTime',
        elementName: {
          localPart: 'date-time',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.OadrSamplingRateType',
        elementName: {
          localPart: 'oadrSamplingRate',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.IdentifiedObject',
        elementName: {
          localPart: 'IdentifiedObject',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.OadrSignedObject',
        elementName: {
          localPart: 'oadrSignedObject',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.Properties',
        elementName: {
          localPart: 'properties',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.IconType',
        elementName: {
          localPart: 'icon',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: 'Boolean',
        elementName: {
          localPart: 'oadrXmlSignature',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: {
          localPart: 'oadrDataQuality',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.SignalPayloadType',
        elementName: 'signalPayload',
        substitutionHead: {
          localPart: 'streamPayloadBase',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
        }
      }, {
        typeInfo: '.X509DataType',
        elementName: {
          localPart: 'X509Data',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.ElectricPowerUsageSummary',
        elementName: {
          localPart: 'ElectricPowerUsageSummary',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        elementName: 'eventID'
      }, {
        typeInfo: '.FrequencyType',
        elementName: {
          localPart: 'frequency',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'rights',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.OadrCreatedOptType',
        elementName: {
          localPart: 'oadrCreatedOpt',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: 'vtnID'
      }, {
        typeInfo: '.CurrentValueType',
        elementName: 'currentValue'
      }, {
        typeInfo: '.KeyInfoReferenceType',
        elementName: {
          localPart: 'KeyInfoReference',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: '.OadrPayload',
        elementName: {
          localPart: 'oadrPayload',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.DSAKeyValueType',
        elementName: {
          localPart: 'DSAKeyValue',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.CurrencyType',
        elementName: {
          localPart: 'currencyPerThm',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.OadrCanceledReportType',
        elementName: {
          localPart: 'oadrCanceledReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        elementName: {
          localPart: 'text',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.GeneratorType',
        elementName: {
          localPart: 'generator',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.DateTimeType',
        elementName: {
          localPart: 'updated',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.StreamBaseType',
        elementName: {
          localPart: 'streamBase',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0:stream'
        }
      }, {
        typeInfo: '.OadrRequestEventType',
        elementName: {
          localPart: 'oadrRequestEvent',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.IdType',
        elementName: {
          localPart: 'id',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.PGPDataType',
        elementName: {
          localPart: 'PGPData',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.ISO3AlphaCurrencyCodeContentType',
        elementName: {
          localPart: 'ISO3AlphaCurrencyCode',
          namespaceURI: 'urn:un:unece:uncefact:codelist:standard:5:ISO42173A:2010-04-07'
        }
      }, {
        typeInfo: '.LogoType',
        elementName: {
          localPart: 'logo',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.IntervalType',
        elementName: 'interval'
      }, {
        typeInfo: '.SignalTypeEnumeratedType',
        elementName: 'signalType'
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'rights',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.OadrCancelReportType',
        elementName: {
          localPart: 'oadrCancelReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.QualifiedEventIDType',
        elementName: 'qualifiedEventID'
      }, {
        typeInfo: '.OadrUpdatedReportType',
        elementName: {
          localPart: 'oadrUpdatedReport',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.WsCalendarIntervalType',
        elementName: {
          localPart: 'interval',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        typeInfo: '.BatchItemInfo',
        elementName: {
          localPart: 'BatchItemInfo',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.CurrencyType',
        elementName: {
          localPart: 'currencyPerKWh',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: 'UnsignedInt',
        elementName: 'modificationNumber'
      }, {
        typeInfo: '.BaseUnitType',
        elementName: {
          localPart: 'customUnit',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.SignatureType',
        elementName: {
          localPart: 'Signature',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.CategoryType',
        elementName: {
          localPart: 'category',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        elementName: 'eiReportID'
      }, {
        typeInfo: '.EventStatusEnumeratedType',
        elementName: 'eventStatus'
      }, {
        typeInfo: '.ServiceDeliveryPointType',
        elementName: {
          localPart: 'serviceDeliveryPoint',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.SignaturePropertyType',
        elementName: {
          localPart: 'SignatureProperty',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        elementName: 'groupID'
      }, {
        typeInfo: '.LogoType',
        elementName: {
          localPart: 'logo',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.PersonType',
        elementName: {
          localPart: 'contributor',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'source',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.EntryType'
      }, {
        typeInfo: '.DurationPropType',
        elementName: {
          localPart: 'duration',
          namespaceURI: 'urn:ietf:params:xml:ns:icalendar-2.0'
        }
      }, {
        elementName: 'resourceID'
      }, {
        typeInfo: '.IdType',
        elementName: {
          localPart: 'id',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.ReadingQuality',
        elementName: {
          localPart: 'ReadingQuality',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.OadrRequestReregistrationType',
        elementName: {
          localPart: 'oadrRequestReregistration',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.PrimeFieldParamsType',
        elementName: {
          localPart: 'Prime',
          namespaceURI: 'http:\/\/www.w3.org\/2009\/xmldsig11#'
        }
      }, {
        typeInfo: '.EiEventSignalsType',
        elementName: 'eiEventSignals'
      }, {
        typeInfo: 'Float',
        elementName: 'accuracy'
      }, {
        typeInfo: '.EnergyReactiveType',
        elementName: {
          localPart: 'energyReactive',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'energyItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        elementName: 'uid'
      }, {
        typeInfo: '.GeneratorType',
        elementName: {
          localPart: 'generator',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.OadrCreateOptType',
        elementName: {
          localPart: 'oadrCreateOpt',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.PowerReactiveType',
        elementName: {
          localPart: 'powerReactive',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'powerItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.OadrQueryRegistrationType',
        elementName: {
          localPart: 'oadrQueryRegistration',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.EnergyItemType',
        elementName: {
          localPart: 'energyItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'itemBase',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.DateTimeType',
        elementName: {
          localPart: 'updated',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.FeedType'
      }, {
        typeInfo: '.EiEventBaselineType',
        elementName: 'eiEventBaseline'
      }, {
        typeInfo: '.OadrCanceledOptType',
        elementName: {
          localPart: 'oadrCanceledOpt',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.OadrPollType',
        elementName: {
          localPart: 'oadrPoll',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.KeyInfoType',
        elementName: {
          localPart: 'KeyInfo',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        }
      }, {
        typeInfo: '.TimeConfiguration',
        elementName: {
          localPart: 'LocalTimeParameters',
          namespaceURI: 'http:\/\/naesb.org\/espi'
        }
      }, {
        typeInfo: '.OadrLoadControlStateType',
        elementName: {
          localPart: 'oadrLoadControlState',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.EventDescriptorType',
        elementName: 'eventDescriptor'
      }, {
        elementName: {
          localPart: 'marketContext',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06'
        }
      }, {
        typeInfo: '.OadrReportDescriptionType',
        elementName: {
          localPart: 'oadrReportDescription',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.OadrTransportType',
        elementName: {
          localPart: 'oadrTransportName',
          namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
        }
      }, {
        typeInfo: '.TransportInterfaceType',
        elementName: {
          localPart: 'transportInterface',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        typeInfo: '.TextType',
        elementName: {
          localPart: 'subtitle',
          namespaceURI: 'http:\/\/www.w3.org\/2005\/Atom'
        },
        scope: '.SourceType'
      }, {
        typeInfo: '.EnergyRealType',
        elementName: {
          localPart: 'energyReal',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        },
        substitutionHead: {
          localPart: 'energyItem',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }, {
        elementName: {
          localPart: 'X509SubjectName',
          namespaceURI: 'http:\/\/www.w3.org\/2000\/09\/xmldsig#'
        },
        scope: '.X509DataType'
      }, {
        elementName: {
          localPart: 'node',
          namespaceURI: 'http:\/\/docs.oasis-open.org\/ns\/emix\/2011\/06\/power'
        }
      }]
  };
  return {
    PO: PO
  };
};
if (typeof define === 'function' && define.amd) {
  define([], PO_Module_Factory);
}
else {
  var PO_Module = PO_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.PO = PO_Module.PO;
  }
  else {
    var PO = PO_Module.PO;
  }
}