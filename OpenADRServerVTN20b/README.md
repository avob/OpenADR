# OpenADRServerVTN20b

VTN 2.0b Spring-boot app:
- Oadr profiles 2.0a / 2.0b
- HTTP Pull and Push mode
- HTTP / XMPP transports
- HTTP Json Control API
- HTTP ReactJS frontend application
## Configuration example
```sh
oadr.server.port 8181
oadr.server.context_path /testvtn
oadr.vtnid AVOB_TEST_VTN_20b
oadr.supportPush true
oadr.supportUnsecuredHttpPush true
oadr.pullFrequencySeconds 10
oadr.validateOadrPayloadAgainstXsd true
oadr.security.replayProtectAcceptedDelaySecond 1200
oadr.security.digest.realm oadr.avob.com


oadr.security.vtn.key /opt/oadr-vtn20b/cert/vtn.oadr.com-rsa.key
oadr.security.vtn.cert /opt/oadr-vtn20b/cert/vtn.oadr.com-rsa.crt

oadr.security.vtn.xmpp.key /opt/oadr-vtn20b/cert/xmpp.vtn.oadr.com-rsa.key
oadr.security.vtn.xmpp.cert /opt/oadr-vtn20b/cert/xmpp.vtn.oadr.com-rsa.crt

oadr.security.ca.key /opt/oadr-vtn20b/cert/oadr.com.key
oadr.security.ca.cert /opt/oadr-vtn20b/cert/oadr.com.crt

oadr.security.ven.trustcertificate /opt/oadr-vtn20b/cert/oadr.com.crt

oadr.broker.host rabbitmq
oadr.broker.port 5672
oadr.broker.user admin
oadr.broker.password admin

spring.datasource.url jdbc:postgresql://postgres:5432/oadr-vtn20b 
spring.datasource.username oadr-vtn20b
spring.datasource.password supersecure
spring.jpa.hibernate.ddl-auto create-drop

vtn.swagger true
vtn.cors https://vtn.oadr.com:8181,https://localhost:8181
vtn.custom-cert-folder /opt/oadr-vtn20b/cert

oadr.xmpp.host xmpp.vtn.oadr.com
oadr.xmpp.domain xmpp.vtn.oadr.com
oadr.xmpp.port 5222
```
## Maven profiles
### In-memory profile
Package jar with JMS broker dependencies. Do not require AMPQ broker middleware. Default true
```sh
mvn clean package -P standalone
```
### External profile
Package jar with RabbitMQ broker dependencies. Require AMPQ broker middleware.
```sh
mvn clean package -P external
```
### Swagger profile
Package jar with swagger dependencies used to deliver HTTP Control API description. Default true
```sh
mvn clean package -P swagger
```
### Frontend profile
Compile ReactJS frontend and package jar with compiled files. Default true
```sh
mvn clean package -P frontend
```
