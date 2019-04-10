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
OpenADRServerVTN20bUI | OADR 2.0b VTN Control UI

## Oadr Prototyping Workbench VM

### Architecture

<p align="center">
  <img src="https://github.com/avob/OpenADR/raw/master/oadr_proto_archi.png?raw=true" alt="Sublime's custom image"/>
</p>

### Requirements

- Install [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#installing-the-control-machine)
- Install [Vagrant](https://www.vagrantup.com/) and [Vagrant ansible provisioner plugin](https://www.vagrantup.com/docs/provisioning/ansible.html)
- Install [Virtualbox](https://www.virtualbox.org/)
- Install Backend build dependencies: Java 8 / Maven 3
- Install Frontend build dependencies: NodeJS 8.15.0 / NPM 6.4.1
- Install x509 admin certificate in your browser: cert/admin.oadr.crt
- Install self-signed certificate authority in your browser: cert/oadr.com.crt
- Add "127.0.0.1 vtn.oadr.com" to your local "/etc/hosts"
- Ports 8181, 8080 and 1880 need to be free on your host

### Endpoints

- VTN RootURL: https://vtn.oadr.com:8181/testvtn
- VTN Control Swagger UI: https://vtn.oadr.com:8181/testvtn/swagger-ui.html
- VTN RabbitMQ Management UI: http://vtn.oadr.com:15672
- VTN Control UI: https://vtn.oadr.com:8080/oadr-vtn20b-ui
- Nodered Terminal: http://vtn.oadr.com:1880 
- Ven1 Nodered Dashboard: http://vtn.oadr.com:1880/ui/#!/0

### Build apps and start VM
```shell
	# build backend with external profile
	mvn clean package install -P external -DskipTests=true

	# build frontend
	export PUBLIC_PATH=/oadr-vtn20b-ui/; npm run headless-build --prefix OpenADRServerVTN20bUI/

	# create and provision VM
	cd devops/vtn20b_postgres
	vagrant up
```

## Oadr Prototyping Workbench VM DEV MODE

## Architecture
start a VM containing provisionned Node-red platform and node-red-contrib-oadr-ven module installed.
Useful when developping on VTN or VTN Control UI

### Requirements

- Install [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#installing-the-control-machine)
- Install [Vagrant](https://www.vagrantup.com/) and [Vagrant ansible provisioner plugin](https://www.vagrantup.com/docs/provisioning/ansible.html)
- Install [Virtualbox](https://www.virtualbox.org/)
- Install Backend build dependencies: Java 8 / Maven 3
- Install Frontend build dependencies: NodeJS 8.15.0 / NPM 6.4.1
- Install x509 admin certificate in your browser: cert/admin.oadr.crt
- Install self-signed certificate authority in your browser: cert/oadr.com.crt
- Add "127.0.0.1 vtn.oadr.com" to your local "/etc/hosts"
- Add "{VM public ip} ven1.oadr.com" to your local "/etc/hosts" (required for ven Push Mode)
- Ports 8181, 8080 and 1880 need to be free on your host
- Port 8843 need to be free (required for ven Push Mode)

### Endpoints

- VTN 2.0b Endpoint: https://vtn.oadr.com:8181/testvtn
- VTN Control UI: https://vtn.oadr.com:8080/oadr-vtn20b-ui
- VTN Control Swagger UI: https://vtn.oadr.com:8181/testvtn/swagger-ui.html
- Nodered Terminal: http://vtn.oadr.com:1880 
- Ven1 Nodered Dashboard: http://vtn.oadr.com:1880/ui/#!/0
 
### Start VM
```shell
    # create and provision VM
	cd {root}/devops/nodered
	vagrant up
	
	# in-memory middleware build profile + tests
	mvn clean package install
	
	# start backend (in your IDE) using following args:
	--spring.profiles.active=test-functional,custom-cert,swagger
	--vtn.cors=http://vtn.oadr.com:3000 
	--vtn.custom-cert-folder=../cert

	# start frontend
	cd {root}/OpenADRServerVTN20bUI
	npm run start -s
```

## Links


- [OpenADR 2.0b Spec](https://cimug.ucaiug.org/Projects/CIM-OpenADR/Shared%20Documents/Source%20Documents/OpenADR%20Alliance/OpenADR_2_0b_Profile_Specification_v1.0.pdf)
- [DRProgram Guide v1.0](https://www.openadr.org/assets/openadr_drprogramguide_v1.0.pdf)

