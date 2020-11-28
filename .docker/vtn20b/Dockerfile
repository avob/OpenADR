FROM openjdk:11.0.9-jre-slim

RUN mkdir /opt/oadr-vtn20b/
RUN mkdir /opt/oadr-vtn20b/lib
RUN mkdir /var/log/oadr-vtn20b
RUN mkdir /var/run/oadr-vtn20b

COPY --from=openadr_build:latest /opt/oadr/build/wait-for-it.sh /opt/oadr-vtn20b/wait-for-it.sh
COPY --from=openadr_build:latest /opt/oadr/cert /opt/oadr-vtn20b/cert
COPY --from=openadr_build:latest /opt/oadr/vtn20b/*.jar /opt/oadr-vtn20b/
COPY --from=openadr_build:latest /opt/oadr/lib/* /opt/oadr-vtn20b/lib/

COPY application.properties /opt/oadr-vtn20b/application.properties
COPY loader.properties /opt/oadr-vtn20b/loader.properties
COPY log4j2.xml /opt/oadr-vtn20b/log4j2.xml
COPY oadr-vtn20b.conf /opt/oadr-vtn20b/oadr-vtn20b.conf
RUN mv /opt/oadr-vtn20b/*.jar /opt/oadr-vtn20b/oadr-vtn20b.jar

RUN chmod +x /opt/oadr-vtn20b/wait-for-it.sh

CMD ["/opt/oadr-vtn20b/wait-for-it.sh", "rabbitmq:5672", "-t", "90", "--" \
	, "java", "-jar", "/opt/oadr-ven20b/oadr-ven20b.jar", "--spring.config.location=/opt/oadr-ven20b/application.properties"]

CMD ["/opt/oadr-vtn20b/oadr-vtn20b.jar"]