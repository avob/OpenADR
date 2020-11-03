FROM rabbitmq:3.8.9-alpine

COPY --from=openadr_build:latest /opt/oadr/cert/vtn.oadr.com-rsa.crt /opt/oadr-vtn20b/cert/vtn.oadr.com-rsa.crt
COPY --from=openadr_build:latest /opt/oadr/cert/vtn.oadr.com-rsa.key /opt/oadr-vtn20b/cert/vtn.oadr.com-rsa.key

ENV RABBITMQ_DEFAULT_USER=admin
ENV RABBITMQ_DEFAULT_PASS=admin

RUN rabbitmq-plugins enable --offline rabbitmq_amqp1_0 rabbitmq_auth_backend_http rabbitmq_federation rabbitmq_management rabbitmq_tracing rabbitmq_auth_mechanism_ssl rabbitmq_auth_backend_cache 
