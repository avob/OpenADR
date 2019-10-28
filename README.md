# OpenADR  [![Build Status](https://travis-ci.org/avob/OpenADR.svg?branch=master)](https://travis-ci.org/avob/OpenADR)  [![codecov](https://codecov.io/gh/avob/OpenADR/branch/master/graph/badge.svg)](https://codecov.io/gh/avob/OpenADR)


OpenADR protocol java implementation: *https://www.openadr.org/*

This project aims to provide minimal VEN / VTN 2.0a / 2.0b skeleton implementations. Relevant projects has to be extended to implement specific business logic: data source integration for a VEN stack, aggregation mechanism for VTN stack etc...

Module | Description
------------- | ------------- 
OpenADRSecurity | OADR security framework (authentication, xmlSignature)
OpenADRModel20b | OADR 2.0b model java classes generated from XSDL definition file
OpenADRServerVEN20b | OADR 2.0b VEN skeleton implementation
OpenADRServerVTN20b | OADR 2.0b VTN skeleton implementation

## Oadr Prototyping Workbench VM

### Architecture

<p align="center">
  <img src="https://github.com/avob/OpenADR/raw/master/oadr_workbench_integration_infra.png?raw=true" alt="Sublime's custom image"/>
</p>

### Requirements

- Install [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#installing-the-control-machine)
- Install [Vagrant](https://www.vagrantup.com/) and [Vagrant ansible provisioner plugin](https://www.vagrantup.com/docs/provisioning/ansible.html)
- Install [Virtualbox](https://www.virtualbox.org/)
- Install Backend build dependencies: Java 8 / Maven 3
- Install Frontend build dependencies: NodeJS 8.15.0 / NPM 6.4.1
- Install x509 admin certificate in your browser: cert/admin.oadr.crt
- Install self-signed certificate authority in your browser: cert/oadr.com.crt
- Add "192.168.33.2 vtn.oadr.com" to your local "/etc/hosts"

### Endpoints

- VTN Control Swagger UI: https://vtn.oadr.com:8181/testvtn/swagger-ui.html
- VTN Control UI: https://vtn.oadr.com:8181/testvtn/
- VTN RabbitMQ Management UI: http://192.168.33.2:15672
- VTN Openfire Management UI: http://192.168.33.2:9090
- Nodered Terminal: http://192.168.33.2:1880 
- Ven1 Nodered Dashboard: http://192.168.33.2:1880/ui/#!/0

### Build apps and start VM
```shell
	# build backend with external profile
	mvn clean package install -P external,frontend -DskipTests=true
	# create and provision VM
	cd devops/vtn20b_postgres
	vagrant up
```

## Oadr Prototyping Workbench VM DEV MODE

## Architecture

<p align="center">
  <img src="https://github.com/avob/OpenADR/raw/master/oadr_workbench_dev_infra.png?raw=true" alt="Sublime's custom image"/>
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
- Add "192.168.33.2 ven1.oadr.com" to your local "/etc/hosts"
### Endpoints

- VTN Control UI: https://vtn.oadr.com:8181/testvtn/
- VTN Control Swagger UI: https://vtn.oadr.com:8181/testvtn/swagger-ui.html
- Nodered Terminal: http://192.168.33.2:1880 
- Ven1 Nodered Dashboard: http://192.168.33.2:1880/ui/#!/0
 
### Start VM
```shell
    # create and provision VM
	cd {root}/devops/nodered
	vagrant up
```

## Links

- [OpenADR 2.0b Spec](https://cimug.ucaiug.org/Projects/CIM-OpenADR/Shared%20Documents/Source%20Documents/OpenADR%20Alliance/OpenADR_2_0b_Profile_Specification_v1.0.pdf)
- [DRProgram Guide v1.0](https://www.openadr.org/assets/openadr_drprogramguide_v1.0.pdf)

