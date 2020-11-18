FROM openjdk:11.0.9-jre-slim

RUN mkdir /opt/oadr-ven20b
RUN mkdir /var/log/oadr-ven20b
RUN mkdir /var/run/oadr-ven20b

COPY --from=openadr_build:latest /opt/oadr/cert /opt/oadr-ven20b/cert
COPY --from=openadr_build:latest /opt/oadr/dummy-ven20b/*.jar /opt/oadr-ven20b/

COPY realEnergy.registerReport.xml /opt/oadr-ven20b/realEnergy.registerReport.xml
COPY application.properties /opt/oadr-ven20b/application.properties
RUN mv /opt/oadr-ven20b/*.jar /opt/oadr-ven20b/oadr-ven20b.jar

COPY wait-for-it.sh /opt/oadr-ven20b/wait-for-it.sh
RUN chmod +x /opt/oadr-ven20b/wait-for-it.sh

CMD ["/opt/oadr-ven20b/wait-for-it.sh", "vtn.oadr.com:8081", "-t", "60", "--" \
	, "java", "-jar", "/opt/oadr-ven20b/oadr-ven20b.jar", "--spring.config.location=/opt/oadr-ven20b/application.properties"]