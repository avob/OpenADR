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

## Backend Build Dependencies
- Java 8
- Maven 3

## Frontend Build Dependencies
- NodeJS 8.15.0
- NPM 6.4.1

## VTN20b/Postgres VM using vagrant/ansible/virtualbox
- Install [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#installing-the-control-machine)
- Install [Vagrant](https://www.vagrantup.com/) and [Vagrant ansible provisioner plugin](https://www.vagrantup.com/docs/provisioning/ansible.html)
- Install [Virtualbox](https://www.virtualbox.org/)
- use "cert" folder certificates for CA, VTN, admin client
- VTN port (8181) is forwarded on host machine. 
- You need to configure your DNS to match urls with machine ip address (for example by adding "127.0.0.1 vtn.oadr.com" to your local hosts file)
```shell
	# build and install
	mvn clean package install

	# create and provision VM
	cd devops/vtn20b_postgres
	vagrant up
```

