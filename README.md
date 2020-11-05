# OpenADR  [![Build Status](https://travis-ci.org/avob/OpenADR.svg?branch=master)](https://travis-ci.org/avob/OpenADR)  [![codecov](https://codecov.io/gh/avob/OpenADR/branch/master/graph/badge.svg)](https://codecov.io/gh/avob/OpenADR)


OpenADR protocol java implementation: *https://www.openadr.org/*

This project aims to provide minimal VEN / VTN 2.0a / 2.0b skeleton implementations. Relevant projects has to be extended to implement specific business logic: data source integration for a VEN stack, aggregation mechanism for VTN stack etc...

Module | Description
------------- | ------------- 
OpenADRSecurity | OADR security framework (authentication, xmlSignature)
OpenADRModel20b | OADR 2.0b model java classes generated from XSDL definition file
OpenADRServerVEN20b | OADR 2.0b VEN skeleton implementation
OpenADRServerVTN20b | OADR 2.0b VTN skeleton implementation

## Certificates

Tests certificates are required to test project or build docker images:
```shell
	# generate certificates test suite
	# mandatory passphare for devops purpose: changeme
	./generate_test_cert.sh
```

This command will create several VTN / VEN certificates upon a self-signed generated authority. This authority has to be installed and trusted. An admin trusted client certificate is also generated and can be installed to to perform client authentication:

- Install self-signed vtn certificate in your browser: cert/vtn.oadr.com-rsa.crt
- Install x509 admin certificate in your browser: cert/admin.oadr.com.crt (optional, authentication can be performed using login/pass: admin/admin)
- Add "127.0.0.1 vtn.oadr.com" to your local "/etc/hosts"

## Build docker images

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

