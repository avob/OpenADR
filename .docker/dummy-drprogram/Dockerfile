FROM openjdk:11.0.9-jre-slim

RUN mkdir /opt/dummy-drprogram
RUN mkdir /var/log/dummy-drprogram
RUN mkdir /var/run/dummy-drprogram

COPY --from=openadr_build:latest /opt/oadr/cert /opt/dummy-drprogram/cert
COPY --from=openadr_build:latest /opt/oadr/dummy-drprogram/*.jar /opt/dummy-drprogram/

COPY application.properties /opt/dummy-drprogram/application.properties
COPY simple.DemandResponseEventCreateDto.json /opt/dummy-drprogram/simple.DemandResponseEventCreateDto.json
RUN mv /opt/dummy-drprogram/*.jar /opt/dummy-drprogram/dummy-drprogram.jar

COPY wait-for-it.sh /opt/dummy-drprogram/wait-for-it.sh
RUN chmod +x /opt/dummy-drprogram/wait-for-it.sh

CMD ["/opt/dummy-drprogram/wait-for-it.sh", "vtn.oadr.com:8081", "-t", "60", "--" \
	, "java", "-jar", "/opt/dummy-drprogram/dummy-drprogram.jar", "--spring.config.location=/opt/dummy-drprogram/application.properties"]