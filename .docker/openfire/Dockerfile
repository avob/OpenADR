FROM gizmotronic/openfire:4.4.4

COPY --from=openadr_build:latest /opt/oadr/cert/oadr.com.crt /opt/oadr-vtn20b/cert/oadr.com.crt
COPY --from=openadr_build:latest /opt/oadr/cert/xmpp.vtn.oadr.com-rsa.p12 /opt/oadr-vtn20b/cert/xmpp.vtn.oadr.com-rsa.p12

COPY available-plugins.xml /etc/openfire/available-plugins.xml
COPY openfire.xml /etc/openfire/openfire.xml

ENV OPENFIRE_PLUGIN="/usr/share/openfire/plugins"
ENV OPENFIRE_CONFIG="/etc/openfire"
ENV OPENFIRE_HOME="/var/lib/openfire"
ENV OPENFIRE_SECURITY="/usr/share/openfire/resources/security"

# Download openadr plugin
ADD https://github.com/avob/openfire-oadr-plugin/releases/download/0.0.2/OpenfireOadrPlugin-openfire-plugin-assembly.jar "$OPENFIRE_PLUGIN/OpenfireOadrPlugin-openfire-plugin-assembly.jar"

# Change Identity Store pass
RUN keytool -importkeystore -srckeystore "$OPENFIRE_SECURITY/keystore" -srcstorepass changeit \
			-destkeystore "$OPENFIRE_SECURITY/keystore" -deststorepass changeme ;

# Import VTN identity
RUN keytool -v -importkeystore -srcstorepass "changeme" -srckeystore /opt/oadr-vtn20b/cert/xmpp.vtn.oadr.com-rsa.p12 -srcstoretype PKCS12 \
			-deststorepass changeme -destkeystore "$OPENFIRE_SECURITY/keystore" -deststoretype JKS;

# Change Truststore pass
RUN keytool -importkeystore -srckeystore "$OPENFIRE_SECURITY/truststore" -srcstorepass changeit \
			-destkeystore "$OPENFIRE_SECURITY/truststore" -deststorepass changeme;

# Import CA certificate
RUN keytool -v -import -alias OadrCA -file /opt/oadr-vtn20b/cert/oadr.com.crt \
			-deststorepass changeme -destkeystore "$OPENFIRE_SECURITY/truststore" -deststoretype JKS -noprompt;

# Change Client Truststore pass
# RUN keytool -importkeystore -srckeystore "$OPENFIRE_SECURITY/client.truststore"  -srcstorepass changeit \
#			-destkeystore "$OPENFIRE_SECURITY/client.truststore" -deststorepass changeme;

# Import CA certificate
# RUN keytool -v -import -alias OadrCA -file /opt/oadr-vtn20b/cert/oadr.com.crt \
#			-deststorepass changeme -destkeystore "$OPENFIRE_SECURITY/client.truststore" -deststoretype JKS -noprompt;



RUN cp /etc/openfire/security/truststore /usr/share/openfire/resources/security/client.truststore

RUN chown -R openfire:openfire "$OPENFIRE_PLUGIN"
RUN chown -R openfire:openfire "$OPENFIRE_CONFIG"
RUN chown -R openfire:openfire "$OPENFIRE_HOME"
RUN chown -R openfire:openfire "$OPENFIRE_SECURITY"
