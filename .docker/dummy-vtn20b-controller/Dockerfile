FROM openjdk:11.0.9-jre-slim

RUN mkdir /opt/oadr-vtn20b-controller
RUN mkdir /var/log/oadr-vtn20b-controller
RUN mkdir /var/run/oadr-vtn20b-controller

COPY --from=openadr_build:latest /opt/oadr/cert /opt/oadr-vtn20b-controller/cert
COPY --from=openadr_build:latest /opt/oadr/dummy-vtn20b-controller/*.jar /opt/oadr-vtn20b-controller/

COPY application.properties /opt/oadr-vtn20b-controller/application.properties
RUN mv /opt/oadr-vtn20b-controller/*.jar /opt/oadr-vtn20b-controller/oadr-vtn20b-controller.jar

COPY wait-for-it.sh /opt/oadr-vtn20b-controller/wait-for-it.sh
RUN chmod +x /opt/oadr-vtn20b-controller/wait-for-it.sh

CMD ["/opt/oadr-vtn20b-controller/wait-for-it.sh", "vtn.oadr.com:8081", "-t", "45", "--" \
	, "java", "-jar", "/opt/oadr-vtn20b-controller/oadr-vtn20b-controller.jar", "--spring.config.location=/opt/oadr-vtn20b-controller/application.properties"]