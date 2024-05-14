FROM openjdk:17-jdk-alpine
VOLUME /tmp
EXPOSE 8081
ENV LANG pt_BR.UTF-8
ENV XMX 256m
ENV TZ America/Sao_Paulo
COPY target/app.jar app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Xmx${XMX} -XX:MaxMetaspaceSize=512m -Duser.timezone=GMT-3 -jar /app.jar

RUN apk --update add tzdata && \
    cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    rm -rf /var/cache/apk/*