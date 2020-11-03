FROM nodered/node-red

COPY --from=openadr_build:latest /opt/oadr/cert/ven1.oadr.com.fingerprint /opt/oadr-vtn20b/cert/ven1.oadr.com.fingerprint
COPY --from=openadr_build:latest /opt/oadr/cert/ven1.oadr.com.crt /opt/oadr-vtn20b/cert/ven1.oadr.com.crt
COPY --from=openadr_build:latest /opt/oadr/cert/ven1.oadr.com.key /opt/oadr-vtn20b/cert/ven1.oadr.com.key
COPY --from=openadr_build:latest /opt/oadr/cert/oadr.com.crt /opt/oadr-vtn20b/cert/oadr.com.crt


RUN npm install --unsafe-perm=true --allow-root avob/node-red-contrib-oadr-ven

RUN npm uninstall node-red-contrib-ui
RUN npm install node-red-dashboard

COPY flows.json /data/flows.json

RUN venId=$(cat /opt/oadr-vtn20b/cert/ven1.oadr.com.fingerprint); \
	sed -i 's/%VEN_ID%/'"${venId}"'/g' /data/flows.json