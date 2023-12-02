# syntax=docker/dockerfile:1.3.1

## Developer build (incl. JDK and sources)

FROM eclipse-temurin:21-jdk AS build
VOLUME /tmp

RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
        --mount=target=/var/cache/apt,type=cache,sharing=locked \
        rm -f /etc/apt/apt.conf.d/docker-clean \
        && apt-get update \
        && apt-get install -y npm

COPY . /src
WORKDIR /src
RUN npm ci
RUN --mount=type=cache,target=/root/.m2 ./mvnw package -Dmaven.test.skip

RUN cp target/*.jar /srv/app.jar
COPY docker/jdk/run.sh /srv/run.sh

RUN groupadd antragsgruen && useradd -g antragsgruen antragsgruen
USER antragsgruen
WORKDIR /srv
ENTRYPOINT ["/srv/run.sh"]


## Runtime build

FROM eclipse-temurin:21-jre AS runtime

COPY --from=build /srv/app.jar /srv/app.jar
COPY docker/jdk/run.sh /srv/run.sh

RUN groupadd antragsgruen && useradd -g antragsgruen antragsgruen
USER antragsgruen
WORKDIR /srv
ENTRYPOINT ["/srv/run.sh"]
