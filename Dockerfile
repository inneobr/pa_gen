FROM container-registry.oracle.com/java/openjdk:19
RUN rm -f /etc/localtime
RUN ln -s /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime
RUN mkdir sistema
COPY target/genesis-0.0.1-SNAPSHOT.jar /sistema/
ENTRYPOINT ["java","-jar","/sistema/genesis-0.0.1-SNAPSHOT.jar"]
