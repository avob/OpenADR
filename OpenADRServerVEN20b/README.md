# OpenADRServerVEN20b
VEN 2.0b Spring-boot app:
- HTTP Pull and Push mode
- HTTP / XMPP transports

## Configuration example
```sh
ven.autostart = true
oadr.venName = oadr-ven.avob.com
oadr.pullFrequencySeconds 5
oadr.reportOnly false
oadr.xmlSignature false
oadr.pullModel false
oadr.server.port = 8081
oadr.venUrl https://localhost:8081
oadr.venid = 2E:55:12:81:B9:EE:9C:46:72:1D
oadr.security.vtn.trustcertificate src/test/resources/rsa/vtn/TEST_OpenADR_RSA_SPCA0002_Cert.pem,src/test/resources/rsa/vtn/TEST_OpenADR_RSA_RCA0002_Cert.pem,src/test/resources/ecc/vtn/TEST_OpenADR_ECC_Root_CA3_cert.pem,src/test/resources/ecc/vtn/TEST_OpenADR_ECC_SHA256_VTN_Int_CA3_cert.pem
oadr.security.ven.key src/test/resources/rsa/ven/TEST_RSA_VEN_17032333409_privkey.pem
oadr.security.ven.cert src/test/resources/rsa/ven/TEST_RSA_VEN_17032333409_cert.pem
# replay protect xml signature payload validation accepted delay
oadr.security.replayProtectAcceptedDelaySecond 1200
oadr.vtn.myvtn.vtnid=04:D0:20:52:32:23:6A:1F:73:E2
oadr.vtn.myvtn.vtnUrl=https://localhost:8181
oadr.vtn.myxmppvtn.vtnid=04:D0:20:52:32:23:6A:1F:73:E3
oadr.vtn.myxmppvtn.xmpp.host=vtn.oadr.com
oadr.vtn.myxmppvtn.xmpp.port=5222
```
