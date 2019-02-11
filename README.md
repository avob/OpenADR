# OpenADR  [![Build Status](https://travis-ci.org/avob/OpenADR.svg?branch=master)](https://travis-ci.org/avob/OpenADR)  [![codecov](https://codecov.io/gh/avob/OpenADR/branch/master/graph/badge.svg)](https://codecov.io/gh/avob/OpenADR)



OpenADR protocol java implementation: *https://www.openadr.org/*

This project aims to provide minimal VEN / VTN 2.0a / 2.0b squeleton implementations. Relevant projects has to be extended to implement specific business logic: data source integration for a VEN stack, aggregation mechanism for VTN stack etc...

Module | Description
------------- | ------------- 
OpenADRSecurity | OADR security framework (authentication, xmlSignature)
OpenADRModel20a | OADR 2.0a model java classes generated from XSDL definition file
OpenADRModel20b | OADR 2.0b model java classes generated from XSDL definition file
OpenADRHttpClient | OADR http transport client framework
OpenADRHttpClient20a | OADR 2.0a simple http client
OpenADRHttpClient20b | OADR 2.0b simple http client
OpenADRServerVEN20a | OADR 2.0a VEN squeleton implementation
OpenADRServerVEN20b | OADR 2.0b VEN squeleton implementation
OpenADRServerVTN20a | OADR 2.0a VTN squeleton implementation
OpenADRServerVTN20b | OADR 2.0b VTN squeleton implementation

## Dependencies
- Java 8
- Maven 3 (build)
- Spring Boot 1.5.6
- Jetty 9
- VEN/VTN server implementation uses SQL Database

## Build
```shell
    mvn clean package
```
## Functional testing
- use in-memory h2 SQL bdd / activemq broker
- use RSA/ECC test certificates from *https://portal.kyrio.com/*
- configuration: *https://github.com/avob/OpenADR/blob/master/OpenADRServerVTN20b/src/main/resources/application-test-functional.properties*
```shell
    java -jar target/OpenADRServerVTN20b*.jar --spring.profiles.active=test-functional
```
