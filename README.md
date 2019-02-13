# OpenADR  [![Build Status](https://travis-ci.org/avob/OpenADR.svg?branch=master)](https://travis-ci.org/avob/OpenADR)  [![codecov](https://codecov.io/gh/avob/OpenADR/branch/master/graph/badge.svg)](https://codecov.io/gh/avob/OpenADR)



OpenADR protocol java implementation: *https://www.openadr.org/*

This project aims to provide minimal VEN / VTN 2.0a / 2.0b skeleton implementations. Relevant projects has to be extended to implement specific business logic: data source integration for a VEN stack, aggregation mechanism for VTN stack etc...

Module | Description
------------- | ------------- 
OpenADRSecurity | OADR security framework (authentication, xmlSignature)
OpenADRModel20a | OADR 2.0a model java classes generated from XSDL definition file
OpenADRModel20b | OADR 2.0b model java classes generated from XSDL definition file
OpenADRHttpClient | OADR http transport client framework
OpenADRHttpClient20a | OADR 2.0a simple http client
OpenADRHttpClient20b | OADR 2.0b simple http client
OpenADRServerVEN20a | OADR 2.0a VEN skeleton implementation
OpenADRServerVEN20b | OADR 2.0b VEN skeleton implementation
OpenADRServerVTN20a | OADR 2.0a VTN skeleton implementation
OpenADRServerVTN20b | OADR 2.0b VTN skeleton implementation

## Dependencies
- Java 8
- Maven 3 

## Functional Testing
- use in-memory h2 SQL db / activemq broker
- use RSA/ECC test certificates from *https://portal.kyrio.com/*
- VTN 2.0b @ https://localhost:8181/testvtn
- VEN 2.0b x509 ECC auth @ https://localhost:8081/
- VEN 2.0b x509 RSA auth @ https://localhost:8082/
- VENs create registrationParty at application startup

```shell
	# build and install
	mvn clean package install

	# Launch VTN 2.0b
	cd OpenADRServerVTN20b
    mvn spring-boot:run \
        -Dspring.profiles.active=test-functional,oadr-cert


    cd OpenADRServerVEN20b
    # Launch VEN 2.0b RSA
    mvn spring-boot:run \
        -Dspring.profiles.active=test-functional,oadr-cert-rsa

    # Launch VEN 2.0b ECC
    mvn spring-boot:run \
        -Dspring.profiles.active=test-functional,oadr-cert-ecc
```

## Custom PKI Architecture
- use in-memory h2 SQL db / activemq broker
- Custom RSA/ECC keys/certs
- VTN 2.0b @ https://vtn.oadr.com:8181/testvtn
- VEN1 2.0b x509 ECC 256 auth @ https://ven1.oadr.com:8081/
- VEN2 2.0b x509 RSA auth @ https://ven2.oadr.com:8082/
- User admin RSA auth
- VENs create registrationParty at application startup
- You need to DNS to match urls with machine ip address
```shell
	# build and install
	mvn clean package install

	# Generate keys and certificates
	cd cert
	./gen.sh

	# Launch VTN 2.0b
	cd OpenADRServerVTN20b
    mvn spring-boot:run \
        -Dspring.profiles.active=test-functional,custom-cert \
        -Doadr.security.admin.username={client.oadr.com.fingerprint}

    # Create Ven1 / Ven2 on VTN
    cd cert
    curl --key client.oadr.com.key --cert client.oadr.com.crt \ 
        --cacert oadr.com.crt \
        -d '{"username": "'.$(cat ven1.oadr.com.fingerprint).'"}' \
        -H "Content-Type: application/json" \
        -X POST https://vtn.oadr.com:8181/testvtn/Ven/
    curl --key client.oadr.com.key --cert client.oadr.com.crt \
        --cacert oadr.com.crt  \
        -d '{"username": "'.$(cat ven2.oadr.com.fingerprint).'"}' \
        -H "Content-Type: application/json" \
        -X POST https://vtn.oadr.com:8181/testvtn/Ven/

    # Launch VEN1
    cd OpenADRServerVEN20b
    mvn spring-boot:run \
        -Dspring.profiles.active=test-functional,custom-cert-ven1 \
        -Doadr.venid={ven1.oadr.com.fingerprint}

    # Launch VEN2
    cd OpenADRServerVEN20b
    mvn spring-boot:run \
        -Dspring.profiles.active=test-functional,custom-cert-ven2 \
        -Doadr.venid={ven2.oadr.com.fingerprint}

    
```
