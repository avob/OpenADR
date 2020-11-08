# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import com.avob.server.oadrvtn20b.handler.*;
import com.avob.server.oadrvtn20b.handler.auth.*;
import com.avob.server.oadrvtn20b.model.*;
import com.avob.server.oadrvtn20b.api.AccountControllerApi;

import java.io.File;
import java.util.*;

public class AccountControllerApiExample {

    public static void main(String[] args) {
        
        AccountControllerApi apiInstance = new AccountControllerApi();
        OadrAppCreateDto dto = new OadrAppCreateDto(); // OadrAppCreateDto | dto
        try {
            InputStreamResource result = apiInstance.createAppUsingPOST(dto);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountControllerApi#createAppUsingPOST");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://vtn.oadr.com:8181/testvtn*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AccountControllerApi* | [**createAppUsingPOST**](docs/AccountControllerApi.md#createAppUsingPOST) | **POST** /Account/app | createApp
*AccountControllerApi* | [**createUserUsingPOST**](docs/AccountControllerApi.md#createUserUsingPOST) | **POST** /Account/user | createUser
*AccountControllerApi* | [**deleteAppUsingDELETE**](docs/AccountControllerApi.md#deleteAppUsingDELETE) | **DELETE** /Account/app/{username} | deleteApp
*AccountControllerApi* | [**deleteUserUsingDELETE**](docs/AccountControllerApi.md#deleteUserUsingDELETE) | **DELETE** /Account/user/{username} | deleteUser
*AccountControllerApi* | [**listAppUsingGET**](docs/AccountControllerApi.md#listAppUsingGET) | **GET** /Account/app | listApp
*AccountControllerApi* | [**listUserUsingGET**](docs/AccountControllerApi.md#listUserUsingGET) | **GET** /Account/user | listUser
*AccountControllerApi* | [**registeredUserUsingGET**](docs/AccountControllerApi.md#registeredUserUsingGET) | **GET** /Account/ | registeredUser
*DemandResponseControllerApi* | [**activeUsingPOST**](docs/DemandResponseControllerApi.md#activeUsingPOST) | **POST** /DemandResponseEvent/{id}/active | active
*DemandResponseControllerApi* | [**cancelUsingPOST**](docs/DemandResponseControllerApi.md#cancelUsingPOST) | **POST** /DemandResponseEvent/{id}/cancel | cancel
*DemandResponseControllerApi* | [**createUsingPOST**](docs/DemandResponseControllerApi.md#createUsingPOST) | **POST** /DemandResponseEvent/ | create
*DemandResponseControllerApi* | [**deleteUsingDELETE**](docs/DemandResponseControllerApi.md#deleteUsingDELETE) | **DELETE** /DemandResponseEvent/{id} | delete
*DemandResponseControllerApi* | [**publishUsingPOST**](docs/DemandResponseControllerApi.md#publishUsingPOST) | **POST** /DemandResponseEvent/{id}/publish | publish
*DemandResponseControllerApi* | [**readUsingGET**](docs/DemandResponseControllerApi.md#readUsingGET) | **GET** /DemandResponseEvent/{id} | read
*DemandResponseControllerApi* | [**readVenDemandResponseEventUsingGET**](docs/DemandResponseControllerApi.md#readVenDemandResponseEventUsingGET) | **GET** /DemandResponseEvent/{id}/venResponse/{username} | readVenDemandResponseEvent
*DemandResponseControllerApi* | [**readVenDemandResponseEventUsingGET1**](docs/DemandResponseControllerApi.md#readVenDemandResponseEventUsingGET1) | **GET** /DemandResponseEvent/{id}/venResponse | readVenDemandResponseEvent
*DemandResponseControllerApi* | [**searchUsingPOST**](docs/DemandResponseControllerApi.md#searchUsingPOST) | **POST** /DemandResponseEvent/search | search
*DemandResponseControllerApi* | [**updateUsingPUT**](docs/DemandResponseControllerApi.md#updateUsingPUT) | **PUT** /DemandResponseEvent/{id} | update
*GroupControllerApi* | [**createGroupUsingPOST**](docs/GroupControllerApi.md#createGroupUsingPOST) | **POST** /Group/ | createGroup
*GroupControllerApi* | [**deleteGroupByIdUsingDELETE**](docs/GroupControllerApi.md#deleteGroupByIdUsingDELETE) | **DELETE** /Group/{groupId} | deleteGroupById
*GroupControllerApi* | [**findGroupByNameUsingGET**](docs/GroupControllerApi.md#findGroupByNameUsingGET) | **GET** /Group/{groupName} | findGroupByName
*GroupControllerApi* | [**listGroupUsingGET**](docs/GroupControllerApi.md#listGroupUsingGET) | **GET** /Group/ | listGroup
*GroupControllerApi* | [**updateGroupUsingPUT**](docs/GroupControllerApi.md#updateGroupUsingPUT) | **PUT** /Group/ | updateGroup
*MarketContextControllerApi* | [**createMarketContextUsingPOST**](docs/MarketContextControllerApi.md#createMarketContextUsingPOST) | **POST** /MarketContext/ | createMarketContext
*MarketContextControllerApi* | [**deleteMarketContextByIdUsingDELETE**](docs/MarketContextControllerApi.md#deleteMarketContextByIdUsingDELETE) | **DELETE** /MarketContext/{marketContextId} | deleteMarketContextById
*MarketContextControllerApi* | [**findMarketContextByNameUsingGET**](docs/MarketContextControllerApi.md#findMarketContextByNameUsingGET) | **GET** /MarketContext/{marketContextName} | findMarketContextByName
*MarketContextControllerApi* | [**listMarketContextUsingGET**](docs/MarketContextControllerApi.md#listMarketContextUsingGET) | **GET** /MarketContext/ | listMarketContext
*MarketContextControllerApi* | [**updateMarketContextUsingPUT**](docs/MarketContextControllerApi.md#updateMarketContextUsingPUT) | **PUT** /MarketContext/ | updateMarketContext
*Oadr20bVenControllerApi* | [**cancelReportUsingPOST**](docs/Oadr20bVenControllerApi.md#cancelReportUsingPOST) | **POST** /Ven/{venID}/report_action/cancel | cancelReport
*Oadr20bVenControllerApi* | [**cancelSubscriptionReportRequestUsingPOST**](docs/Oadr20bVenControllerApi.md#cancelSubscriptionReportRequestUsingPOST) | **POST** /Ven/{venID}/report/requested/cancelSubscription | cancelSubscriptionReportRequest
*Oadr20bVenControllerApi* | [**pageOtherReportCapabilityUsingGET**](docs/Oadr20bVenControllerApi.md#pageOtherReportCapabilityUsingGET) | **GET** /Ven/{venID}/report/available/search | pageOtherReportCapability
*Oadr20bVenControllerApi* | [**pageReportRequestUsingGET**](docs/Oadr20bVenControllerApi.md#pageReportRequestUsingGET) | **GET** /Ven/{venID}/report/requested/search | pageReportRequest
*Oadr20bVenControllerApi* | [**registerPartyCancelPartyRegistrationUsingPOST**](docs/Oadr20bVenControllerApi.md#registerPartyCancelPartyRegistrationUsingPOST) | **POST** /Ven/{venID}/registerparty/cancelPartyRegistration | registerPartyCancelPartyRegistration
*Oadr20bVenControllerApi* | [**registerPartyRequestReregistrationUsingPOST**](docs/Oadr20bVenControllerApi.md#registerPartyRequestReregistrationUsingPOST) | **POST** /Ven/{venID}/registerparty/requestReregistration | registerPartyRequestReregistration
*Oadr20bVenControllerApi* | [**requestOtherReportCapabilityDescriptionRidUsingPOST**](docs/Oadr20bVenControllerApi.md#requestOtherReportCapabilityDescriptionRidUsingPOST) | **POST** /Ven/{venID}/report/available/description/request | requestOtherReportCapabilityDescriptionRid
*Oadr20bVenControllerApi* | [**requestRegisterReportUsingPOST**](docs/Oadr20bVenControllerApi.md#requestRegisterReportUsingPOST) | **POST** /Ven/{venID}/report_action/requestRegister | requestRegisterReport
*Oadr20bVenControllerApi* | [**sendRegisterReportUsingPOST**](docs/Oadr20bVenControllerApi.md#sendRegisterReportUsingPOST) | **POST** /Ven/{venID}/report_action/sendRegister | sendRegisterReport
*Oadr20bVenControllerApi* | [**subscribeOtherReportCapabilityDescriptionRidUsingPOST**](docs/Oadr20bVenControllerApi.md#subscribeOtherReportCapabilityDescriptionRidUsingPOST) | **POST** /Ven/{venID}/report/available/description/subscribe | subscribeOtherReportCapabilityDescriptionRid
*Oadr20bVenControllerApi* | [**venOptUsingGET**](docs/Oadr20bVenControllerApi.md#venOptUsingGET) | **GET** /Ven/{venID}/opt | venOpt
*Oadr20bVenControllerApi* | [**venResourceOptUsingGET**](docs/Oadr20bVenControllerApi.md#venResourceOptUsingGET) | **GET** /Ven/{venID}/opt/resource/{resourceName} | venResourceOpt
*Oadr20bVenControllerApi* | [**viewOtherReportCapabilityDescriptionUsingGET**](docs/Oadr20bVenControllerApi.md#viewOtherReportCapabilityDescriptionUsingGET) | **GET** /Ven/{venID}/report/available/description | viewOtherReportCapabilityDescription
*Oadr20bVenControllerApi* | [**viewOtherReportCapabilityUsingGET**](docs/Oadr20bVenControllerApi.md#viewOtherReportCapabilityUsingGET) | **GET** /Ven/{venID}/report/available | viewOtherReportCapability
*Oadr20bVenControllerApi* | [**viewReportRequestSpecifierUsingPOST**](docs/Oadr20bVenControllerApi.md#viewReportRequestSpecifierUsingPOST) | **POST** /Ven/{venID}/report/requested/specifier | viewReportRequestSpecifier
*Oadr20bVenControllerApi* | [**viewReportRequestUsingGET**](docs/Oadr20bVenControllerApi.md#viewReportRequestUsingGET) | **GET** /Ven/{venID}/report/requested | viewReportRequest
*Oadr20bVenControllerApi* | [**viewsFloatReportDataUsingGET**](docs/Oadr20bVenControllerApi.md#viewsFloatReportDataUsingGET) | **GET** /Ven/{venID}/report/data/float/{reportSpecifierId}/rid/{rid} | viewsFloatReportData
*Oadr20bVenControllerApi* | [**viewsFloatReportDataUsingGET1**](docs/Oadr20bVenControllerApi.md#viewsFloatReportDataUsingGET1) | **GET** /Ven/{venID}/report/data/float/{reportSpecifierId} | viewsFloatReportData
*Oadr20bVenControllerApi* | [**viewsKeyTokenReportDataUsingGET**](docs/Oadr20bVenControllerApi.md#viewsKeyTokenReportDataUsingGET) | **GET** /Ven/{venID}/report/data/keytoken/{reportSpecifierId}/rid/{rid} | viewsKeyTokenReportData
*Oadr20bVenControllerApi* | [**viewsKeyTokenReportDataUsingGET1**](docs/Oadr20bVenControllerApi.md#viewsKeyTokenReportDataUsingGET1) | **GET** /Ven/{venID}/report/data/keytoken/{reportSpecifierId} | viewsKeyTokenReportData
*Oadr20bVenControllerApi* | [**viewsResourceStatusReportDataUsingGET**](docs/Oadr20bVenControllerApi.md#viewsResourceStatusReportDataUsingGET) | **GET** /Ven/{venID}/report/data/resourcestatus/{reportSpecifierId}/rid/{rid} | viewsResourceStatusReportData
*Oadr20bVenControllerApi* | [**viewsResourceStatusReportDataUsingGET1**](docs/Oadr20bVenControllerApi.md#viewsResourceStatusReportDataUsingGET1) | **GET** /Ven/{venID}/report/data/resourcestatus/{reportSpecifierId} | viewsResourceStatusReportData
*Oadr20bVtnControllerApi* | [**viewConfUsingGET**](docs/Oadr20bVtnControllerApi.md#viewConfUsingGET) | **GET** /Vtn/configuration | viewConf
*Oadr20bVtnControllerApi* | [**viewOtherReportCapabilityDescriptionUsingGET1**](docs/Oadr20bVtnControllerApi.md#viewOtherReportCapabilityDescriptionUsingGET1) | **GET** /Vtn/report/available/{reportSpecifierId} | viewOtherReportCapabilityDescription
*Oadr20bVtnControllerApi* | [**viewReportRequestUsingGET1**](docs/Oadr20bVtnControllerApi.md#viewReportRequestUsingGET1) | **GET** /Vtn/report/requested | viewReportRequest
*Oadr20bVtnControllerApi* | [**viewSelfReportCapabilityUsingGET**](docs/Oadr20bVtnControllerApi.md#viewSelfReportCapabilityUsingGET) | **GET** /Vtn/report/available | viewSelfReportCapability
*RabbitmqHttpAuthControllerApi* | [**resourceUsingDELETE**](docs/RabbitmqHttpAuthControllerApi.md#resourceUsingDELETE) | **DELETE** /auth/resource | resource
*RabbitmqHttpAuthControllerApi* | [**resourceUsingGET**](docs/RabbitmqHttpAuthControllerApi.md#resourceUsingGET) | **GET** /auth/resource | resource
*RabbitmqHttpAuthControllerApi* | [**resourceUsingHEAD**](docs/RabbitmqHttpAuthControllerApi.md#resourceUsingHEAD) | **HEAD** /auth/resource | resource
*RabbitmqHttpAuthControllerApi* | [**resourceUsingOPTIONS**](docs/RabbitmqHttpAuthControllerApi.md#resourceUsingOPTIONS) | **OPTIONS** /auth/resource | resource
*RabbitmqHttpAuthControllerApi* | [**resourceUsingPATCH**](docs/RabbitmqHttpAuthControllerApi.md#resourceUsingPATCH) | **PATCH** /auth/resource | resource
*RabbitmqHttpAuthControllerApi* | [**resourceUsingPOST**](docs/RabbitmqHttpAuthControllerApi.md#resourceUsingPOST) | **POST** /auth/resource | resource
*RabbitmqHttpAuthControllerApi* | [**resourceUsingPUT**](docs/RabbitmqHttpAuthControllerApi.md#resourceUsingPUT) | **PUT** /auth/resource | resource
*RabbitmqHttpAuthControllerApi* | [**topicUsingDELETE**](docs/RabbitmqHttpAuthControllerApi.md#topicUsingDELETE) | **DELETE** /auth/topic | topic
*RabbitmqHttpAuthControllerApi* | [**topicUsingGET**](docs/RabbitmqHttpAuthControllerApi.md#topicUsingGET) | **GET** /auth/topic | topic
*RabbitmqHttpAuthControllerApi* | [**topicUsingHEAD**](docs/RabbitmqHttpAuthControllerApi.md#topicUsingHEAD) | **HEAD** /auth/topic | topic
*RabbitmqHttpAuthControllerApi* | [**topicUsingOPTIONS**](docs/RabbitmqHttpAuthControllerApi.md#topicUsingOPTIONS) | **OPTIONS** /auth/topic | topic
*RabbitmqHttpAuthControllerApi* | [**topicUsingPATCH**](docs/RabbitmqHttpAuthControllerApi.md#topicUsingPATCH) | **PATCH** /auth/topic | topic
*RabbitmqHttpAuthControllerApi* | [**topicUsingPOST**](docs/RabbitmqHttpAuthControllerApi.md#topicUsingPOST) | **POST** /auth/topic | topic
*RabbitmqHttpAuthControllerApi* | [**topicUsingPUT**](docs/RabbitmqHttpAuthControllerApi.md#topicUsingPUT) | **PUT** /auth/topic | topic
*RabbitmqHttpAuthControllerApi* | [**userUsingDELETE**](docs/RabbitmqHttpAuthControllerApi.md#userUsingDELETE) | **DELETE** /auth/user | user
*RabbitmqHttpAuthControllerApi* | [**userUsingGET**](docs/RabbitmqHttpAuthControllerApi.md#userUsingGET) | **GET** /auth/user | user
*RabbitmqHttpAuthControllerApi* | [**userUsingHEAD**](docs/RabbitmqHttpAuthControllerApi.md#userUsingHEAD) | **HEAD** /auth/user | user
*RabbitmqHttpAuthControllerApi* | [**userUsingOPTIONS**](docs/RabbitmqHttpAuthControllerApi.md#userUsingOPTIONS) | **OPTIONS** /auth/user | user
*RabbitmqHttpAuthControllerApi* | [**userUsingPATCH**](docs/RabbitmqHttpAuthControllerApi.md#userUsingPATCH) | **PATCH** /auth/user | user
*RabbitmqHttpAuthControllerApi* | [**userUsingPOST**](docs/RabbitmqHttpAuthControllerApi.md#userUsingPOST) | **POST** /auth/user | user
*RabbitmqHttpAuthControllerApi* | [**userUsingPUT**](docs/RabbitmqHttpAuthControllerApi.md#userUsingPUT) | **PUT** /auth/user | user
*RabbitmqHttpAuthControllerApi* | [**vhostUsingDELETE**](docs/RabbitmqHttpAuthControllerApi.md#vhostUsingDELETE) | **DELETE** /auth/vhost | vhost
*RabbitmqHttpAuthControllerApi* | [**vhostUsingGET**](docs/RabbitmqHttpAuthControllerApi.md#vhostUsingGET) | **GET** /auth/vhost | vhost
*RabbitmqHttpAuthControllerApi* | [**vhostUsingHEAD**](docs/RabbitmqHttpAuthControllerApi.md#vhostUsingHEAD) | **HEAD** /auth/vhost | vhost
*RabbitmqHttpAuthControllerApi* | [**vhostUsingOPTIONS**](docs/RabbitmqHttpAuthControllerApi.md#vhostUsingOPTIONS) | **OPTIONS** /auth/vhost | vhost
*RabbitmqHttpAuthControllerApi* | [**vhostUsingPATCH**](docs/RabbitmqHttpAuthControllerApi.md#vhostUsingPATCH) | **PATCH** /auth/vhost | vhost
*RabbitmqHttpAuthControllerApi* | [**vhostUsingPOST**](docs/RabbitmqHttpAuthControllerApi.md#vhostUsingPOST) | **POST** /auth/vhost | vhost
*RabbitmqHttpAuthControllerApi* | [**vhostUsingPUT**](docs/RabbitmqHttpAuthControllerApi.md#vhostUsingPUT) | **PUT** /auth/vhost | vhost
*ReportControllerApi* | [**searchOtherReportCapabilityDescriptionUsingGET**](docs/ReportControllerApi.md#searchOtherReportCapabilityDescriptionUsingGET) | **GET** /Report/available/description/search | searchOtherReportCapabilityDescription
*ReportControllerApi* | [**viewOtherReportCapabilityUsingGET1**](docs/ReportControllerApi.md#viewOtherReportCapabilityUsingGET1) | **GET** /Report/available/search | viewOtherReportCapability
*RoleControllerApi* | [**getUserRoleUsingPOST**](docs/RoleControllerApi.md#getUserRoleUsingPOST) | **POST** /Role/{username} | getUserRole
*VenControllerApi* | [**addGroupToVenUsingPOST**](docs/VenControllerApi.md#addGroupToVenUsingPOST) | **POST** /Ven/{venID}/group | addGroupToVen
*VenControllerApi* | [**addMarketContextToVenUsingPOST**](docs/VenControllerApi.md#addMarketContextToVenUsingPOST) | **POST** /Ven/{venID}/marketContext | addMarketContextToVen
*VenControllerApi* | [**cleanRegistrationUsingPOST**](docs/VenControllerApi.md#cleanRegistrationUsingPOST) | **POST** /Ven/{venID}/cleanRegistration | cleanRegistration
*VenControllerApi* | [**createVenResourceUsingPOST**](docs/VenControllerApi.md#createVenResourceUsingPOST) | **POST** /Ven/{venID}/resource | createVenResource
*VenControllerApi* | [**createVenUsingPOST**](docs/VenControllerApi.md#createVenUsingPOST) | **POST** /Ven/ | createVen
*VenControllerApi* | [**deleteVenByUsernameUsingDELETE**](docs/VenControllerApi.md#deleteVenByUsernameUsingDELETE) | **DELETE** /Ven/{venID} | deleteVenByUsername
*VenControllerApi* | [**deleteVenGroupUsingPOST**](docs/VenControllerApi.md#deleteVenGroupUsingPOST) | **POST** /Ven/{venID}/group/remove | deleteVenGroup
*VenControllerApi* | [**deleteVenMarketContextUsingPOST**](docs/VenControllerApi.md#deleteVenMarketContextUsingPOST) | **POST** /Ven/{venID}/marketContext/remove | deleteVenMarketContext
*VenControllerApi* | [**deleteVenResourceUsingDELETE**](docs/VenControllerApi.md#deleteVenResourceUsingDELETE) | **DELETE** /Ven/{venID}/resource/{resourceName} | deleteVenResource
*VenControllerApi* | [**findVenByUsernameUsingGET**](docs/VenControllerApi.md#findVenByUsernameUsingGET) | **GET** /Ven/{venID} | findVenByUsername
*VenControllerApi* | [**listVenGroupUsingGET**](docs/VenControllerApi.md#listVenGroupUsingGET) | **GET** /Ven/{venID}/group | listVenGroup
*VenControllerApi* | [**listVenMarketContextUsingGET**](docs/VenControllerApi.md#listVenMarketContextUsingGET) | **GET** /Ven/{venID}/marketContext | listVenMarketContext
*VenControllerApi* | [**listVenResourceUsingGET**](docs/VenControllerApi.md#listVenResourceUsingGET) | **GET** /Ven/{venID}/resource | listVenResource
*VenControllerApi* | [**listVenUsingGET**](docs/VenControllerApi.md#listVenUsingGET) | **GET** /Ven/ | listVen
*VenControllerApi* | [**searchVenUsingPOST**](docs/VenControllerApi.md#searchVenUsingPOST) | **POST** /Ven/search | searchVen
*VenControllerApi* | [**updateVenUsingPUT**](docs/VenControllerApi.md#updateVenUsingPUT) | **PUT** /Ven/{venID} | updateVen


## Documentation for Models

 - [AbstractUserWithRoleDto](docs/AbstractUserWithRoleDto.md)
 - [DemandResponseEventActivePeriodDto](docs/DemandResponseEventActivePeriodDto.md)
 - [DemandResponseEventCreateDto](docs/DemandResponseEventCreateDto.md)
 - [DemandResponseEventDescriptorDto](docs/DemandResponseEventDescriptorDto.md)
 - [DemandResponseEventFilter](docs/DemandResponseEventFilter.md)
 - [DemandResponseEventReadDto](docs/DemandResponseEventReadDto.md)
 - [DemandResponseEventSignalDto](docs/DemandResponseEventSignalDto.md)
 - [DemandResponseEventSignalIntervalDto](docs/DemandResponseEventSignalIntervalDto.md)
 - [DemandResponseEventTargetDto](docs/DemandResponseEventTargetDto.md)
 - [DemandResponseEventUpdateDto](docs/DemandResponseEventUpdateDto.md)
 - [InputStream](docs/InputStream.md)
 - [InputStreamResource](docs/InputStreamResource.md)
 - [KeyTokenType](docs/KeyTokenType.md)
 - [OadrAppCreateDto](docs/OadrAppCreateDto.md)
 - [OadrAppDto](docs/OadrAppDto.md)
 - [OadrUserCreateDto](docs/OadrUserCreateDto.md)
 - [OadrUserDto](docs/OadrUserDto.md)
 - [OtherReportCapabilityDescriptionDto](docs/OtherReportCapabilityDescriptionDto.md)
 - [OtherReportCapabilityDto](docs/OtherReportCapabilityDto.md)
 - [OtherReportDataFloatDto](docs/OtherReportDataFloatDto.md)
 - [OtherReportDataKeyTokenDto](docs/OtherReportDataKeyTokenDto.md)
 - [OtherReportDataPayloadResourceStatusDto](docs/OtherReportDataPayloadResourceStatusDto.md)
 - [OtherReportRequestDto](docs/OtherReportRequestDto.md)
 - [OtherReportRequestDtoCreateRequestDto](docs/OtherReportRequestDtoCreateRequestDto.md)
 - [OtherReportRequestDtoCreateSubscriptionDto](docs/OtherReportRequestDtoCreateSubscriptionDto.md)
 - [OtherReportRequestSpecifierDto](docs/OtherReportRequestSpecifierDto.md)
 - [OtherReportRequestSpecifierSearchCriteria](docs/OtherReportRequestSpecifierSearchCriteria.md)
 - [ReportCapabilityDescriptionDto](docs/ReportCapabilityDescriptionDto.md)
 - [ReportCapabilityDto](docs/ReportCapabilityDto.md)
 - [ReportRequestDto](docs/ReportRequestDto.md)
 - [URI](docs/URI.md)
 - [URL](docs/URL.md)
 - [VenCreateDto](docs/VenCreateDto.md)
 - [VenDemandResponseEventDto](docs/VenDemandResponseEventDto.md)
 - [VenDto](docs/VenDto.md)
 - [VenFilter](docs/VenFilter.md)
 - [VenGroupDto](docs/VenGroupDto.md)
 - [VenMarketContextDto](docs/VenMarketContextDto.md)
 - [VenOptDto](docs/VenOptDto.md)
 - [VenReportCapabilityDto](docs/VenReportCapabilityDto.md)
 - [VenReportDto](docs/VenReportDto.md)
 - [VenResourceDto](docs/VenResourceDto.md)
 - [VenUpdateDto](docs/VenUpdateDto.md)
 - [VtnConfigurationDto](docs/VtnConfigurationDto.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

bzanni@avob.com

