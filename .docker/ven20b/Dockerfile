#FROM adoptopenjdk/openjdk8:debian-slim
FROM openjdk:8u232-jre-slim

RUN mkdir /opt/oadr-ven20b
RUN mkdir /var/log/oadr-ven20b
RUN mkdir /var/run/oadr-ven20b

COPY --from=openadr_build:latest /opt/oadr/cert /opt/oadr-ven20b/cert
COPY --from=openadr_build:latest /opt/oadr/ven20b/*.jar /opt/oadr-ven20b/

COPY application.properties /opt/oadr-ven20b/application.properties
RUN mv /opt/oadr-ven20b/*.jar /opt/oadr-ven20b/oadr-ven20b.jar

COPY wait-fot-it.sh /opt/oadr-ven20b/wait-fot-it.sh

RUN chmod +x /opt/oadr-ven20b/wait-fot-it.sh

CMD ["/opt/oadr-ven20b/wait-fot-it.sh", "vtn.oadr.com:8081", "-t", "30", "--" \
	, "java", "-jar", "/opt/oadr-ven20b/oadr-ven20b.jar", "--spring.config.location=/opt/oadr-ven20b/application.properties"]