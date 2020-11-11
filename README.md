# OpenADR  [![Build Status](https://travis-ci.org/avob/OpenADR.svg?branch=master)](https://travis-ci.org/avob/OpenADR)  [![codecov](https://codecov.io/gh/avob/OpenADR/branch/master/graph/badge.svg)](https://codecov.io/gh/avob/OpenADR)


OpenADR protocol java implementation: *https://www.openadr.org/*

Provides a standalone VTN 2.0b implementation and APIs to implements demand-response program and device management.
Provides a VEN 2.0b library which can be used to implements a VEN 2.0b.



Module | Description
------------- | ------------- 
OpenADRSecurity | OADR security framework (PKI RSA/ECC, XmlSignature)
OpenADRModel20b | OADR 2.0b model java classes generated from XSDL definition file
OpenADRServerVEN20b | OADR 2.0b VEN library
OpenADRServerVTN20b | OADR 2.0b VTN implementation + Control API / UI
DummyVEN20b | Dummy OADR 2.0b VEN implementation based on OpenADRServerVEN20b
DummyVTN20bController | Dummy OADR 2.0b VTN controller implementation acting DRProgram and device manager. Leverage OpenADRServerVTN20b APIs.


## Certificates

Tests certificates are required to test project or run demo:
```shell
	./generate_test_cert.sh
```

This command will create several VTN / VEN / User / App certificates upon a self-signed generated authority. This authority has to be installed to disable insecure https warning when using VTN Control API / UI in a browser. VTN certificate uses the following Common Name, which need to be added to the local DNS configuration.

- Install self-signed vtn certificate in your browser: cert/vtn.oadr.com-rsa.crt
- Add "127.0.0.1 vtn.oadr.com" to your local "/etc/hosts"

 An admin trusted client certificate is also generated and can be installed to to perform client authentication.

- Install x509 admin certificate in your browser: cert/admin.oadr.com.crt (optional, authentication can be performed using login/pass: admin/admin)

## Oadr demo

Run a demo of a full 2.0b OADR stack infrastructure interacting with a dummy VEN and a dummy VTN controller. The VTN controller is acting as both a device manager by creating VEN authorization / subscribing to VEN reports, and a DemandResponseProgram manager by creating DREvents. The dummy VEN implementation will simulate data reading using information gathered in DREvents and continuously push them to the VTN. The VTN controller is notified by VTN using AMPQ when VTN receive payload from VEN (createRegistrationParty, registerReport, updateReports).

### Requirement

- Generate certificates
- Docker 19.03.13

### Run

```shell
	docker-compose up
```

### Endpoints

- VTN Control Swagger UI: https://vtn.oadr.com:8181/testvtn/swagger-ui.html
- VTN Control UI: https://vtn.oadr.com:8181/testvtn/
- VTN RabbitMQ Management UI: http://localhost:15672
- VTN Openfire Management UI: http://localhost:9090
- Nodered Terminal: http://localhost:1880 
- Ven1 Nodered Dashboard: http://localhost:1880/ui/#!/0

### Components diagram

<div hidden>

```
@startuml demo_component_diagram

package "Demand / Production" {
    rectangle "dummy-ven20b" as dummyVen #FFF
}

package "OADR Provider" {
    rectangle "vtn20b" as vtn #FFF
    database postgres
    node rabbitmq
    node openfire
}

package "DemandResponseProgram" {
    rectangle "dummy-vtn20b-controller" as dummyVtnController #FFF
}


vtn <-up-> openfire #line:red;line.bold;text:red  : OADR(XMPP)
openfire -> vtn #green;line.bold;text:green : AUTH(HTTP)
vtn -down-> rabbitmq #blue;line.bold;text:blue   : DATA(AMPQ)
dummyVen <--> vtn #green;line.bold;text:green : OADR(HTTP)
dummyVen <-> openfire #line:red;line.bold;text:red  : OADR(XMPP)
openfire -> postgres #black;line.dotted;text:black
vtn -> postgres #black;line.dotted;text:black
rabbitmq -down-> vtn #green;line.bold;text:green : AUTH(HTTP)
dummyVtnController -up-> vtn #green;line.bold;text:green : DATA(HTTP)
dummyVtnController <-- rabbitmq #blue;line.bold;text:blue   : DATA(AMPQ)

@enduml
```

</div>

![](demo_component_diagram.svg)

### Sequence diagram

<div hidden>

```
@startuml demo_sequence_diagram

participant "dummy-ven20b" as dummyVen
participant "vtn20b" as vtn 
participant "dummy-vtn20b-controller" as dummyVtnController

group Device provisionning
dummyVtnController --> vtn: Creates MarketContext / VEN
dummyVtnController --> vtn: Enrolls VEN to MarketContext
end 

group Device registration
dummyVen --> vtn: Creates registration party
vtn --> dummyVtnController: Notify registration



dummyVen --> vtn: Registers reports
vtn --> dummyVtnController: Notify register reports
dummyVtnController--> vtn: Subscribes reports
vtn --> dummyVen: Creates reports subscription
end

group Normal workflow
group DRProgram
dummyVtnController --> vtn: Creates DREvents in MarketContext
dummyVen <-- vtn: Send DREvents
end
group Data reading
dummyVen --> dummyVen: Simulate data readings\n based on received DREvents
dummyVen --> vtn: Updates reports
vtn --> dummyVtnController: Notify data update
end

end

@enduml
```

</div>

![](demo_sequence_diagram.svg)


## Test project

### Requirements
- Backend build dependencies: Java 11 / Maven 3
- Frontend build dependencies: NodeJS 8.15.0 / NPM 6.4.1

### Run

```shell
	mvn clean verify
```

## Links

- [OpenADR 2.0b Spec](https://cimug.ucaiug.org/Projects/CIM-OpenADR/Shared%20Documents/Source%20Documents/OpenADR%20Alliance/OpenADR_2_0b_Profile_Specification_v1.0.pdf)
- [DRProgram Guide v1.0](https://www.openadr.org/assets/openadr_drprogramguide_v1.0.pdf)

