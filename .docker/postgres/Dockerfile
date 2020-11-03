FROM postgres:13-alpine

COPY --from=openadr_build:latest /opt/oadr/cert/xmpp.vtn.oadr.com-rsa.fingerprint /opt/oadr-vtn20b/cert/xmpp.vtn.oadr.com-rsa.fingerprint
COPY init-* /docker-entrypoint-initdb.d/

ENV POSTGRES_PASSWORD=supersecure

RUN vtnId=$(cat /opt/oadr-vtn20b/cert/xmpp.vtn.oadr.com-rsa.fingerprint); \
	sed -i 's/%VTN_ID%/'"${vtnId}"'/g' /docker-entrypoint-initdb.d/init-openfire.sh

