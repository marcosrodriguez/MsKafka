FROM qa.stratio.com/stratio/java-microservice-dockerbase:0.2.0

VOLUME /tmp

ADD *.jar app.jar
ADD entrypoint.sh entrypoint.sh

RUN touch /data/app.jar && \
    chmod 600 /data/entrypoint.sh

ENTRYPOINT ["bash", "/data/entrypoint.sh" ]

