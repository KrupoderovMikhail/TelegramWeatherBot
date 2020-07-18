FROM bellsoft/liberica-openjdk-alpine:8u242

COPY . /opt/app

WORKDIR /opt/app

RUN apk add --update bash unzip maven && rm -rf /var/cache/apk/* \
 && mvn clean package -Dmaven.test.skip=true \
 && chmod 777 -R /opt/app/ \
 && mkdir -p jar \
 && cp -f target/telegrambot-1.0-jar-with-dependencies.jar jar/telegrambot-1.0-jar-with-dependencies.jar \
 && ls | grep -v jar | xargs rm -rfv

ENV JRE_HOME="${JAVA_HOME}/jre"

CMD ${JAVA_HOME}/bin/java -jar jar/telegrambot-1.0-jar-with-dependencies.jar

EXPOSE 8092
