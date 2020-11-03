FROM adoptopenjdk/openjdk8

RUN mkdir /opt/oadr-vtn20b/
RUN mkdir /opt/oadr-vtn20b/lib
RUN mkdir /var/log/oadr-vtn20b
RUN mkdir /var/run/oadr-vtn20b

COPY --from=openadr_build:latest /opt/oadr/cert /opt/oadr-vtn20b/cert
COPY --from=openadr_build:latest /opt/oadr/vtn20b/*.jar /opt/oadr-vtn20b/

ADD https://jdbc.postgresql.org/download/postgresql-42.2.5.jar /opt/oadr-vtn20b/lib/postgresql-42.2.5.jar

COPY application.properties /opt/oadr-vtn20b/application.properties
COPY loader.properties /opt/oadr-vtn20b/loader.properties
COPY log4j2.xml /opt/oadr-vtn20b/log4j2.xml
COPY oadr-vtn20b.conf /opt/oadr-vtn20b/oadr-vtn20b.conf
RUN mv /opt/oadr-vtn20b/*.jar /opt/oadr-vtn20b/oadr-vtn20b.jar

CMD ["/opt/oadr-vtn20b/oadr-vtn20b.jar"]