# OpenADRServerVTN20b
VTN 2.0b Spring-boot app:
- Oadr profiles 2.0a / 2.0b
- HTTP Pull and Push mode
- HTTP / XMPP transports
- HTTP Json Control API
- HTTP ReactJS frontend application
## Configuration example
```sh
oadr.server.port=8181
oadr.server.context_path=/testvtn
oadr.vtnid=9bf3f51607f5a213db08
# support push https request
oadr.supportPush=true
# support push http request
oadr.supportUnsecuredHttpPush=true
# default pull frequency
oadr.pullFrequencySeconds=1
# validate incoming payloads against oadr xsd files
oadr.validateOadrPayloadAgainstXsd=true
# insert updateReport payload in db
oadr.saveVenData=false
# replay protect xml signature payload validation accepted delay
oadr.security.replayProtectAcceptedDelaySecond=1200
# digest based authentication system real
oadr.security.digest.realm=oadr.avob.com
# broker connection (either plain or ssl)
oadr.broker.host=localhost
oadr.broker.port=5672
#oadr.broker.ssl.host=vtn.oadr.com
#oadr.broker.ssl.port=45672
# xmpp connection
oadr.xmpp.host=localhost
oadr.xmpp.domain=vtn.oadr.com
oadr.xmpp.port=5222
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
