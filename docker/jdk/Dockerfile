FROM eclipse-temurin:17-jdk
VOLUME /tmp

COPY . /src
WORKDIR /src
RUN --mount=type=cache,target=/root/.m2 ./mvnw package -Dmaven.test.skip
RUN cp target/*.jar /srv/app.jar

COPY docker/jdk/run.sh /srv/run.sh
WORKDIR /srv
ENTRYPOINT ["/srv/run.sh"]
