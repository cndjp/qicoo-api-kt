FROM gradle:5.6.2-jdk11 as builder
# 必要なdebパッケージのインストール
ENV DEBIAN_FRONTEND=noninteractive
RUN set -eux; \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        bash \
        ca-certificates \
        zip \
        unzip \
        curl && \
        rm -rf /var/lib/apt/lists/*
RUN mkdir -p /opt/app/bin
COPY . /opt/app
WORKDIR /opt/app
RUN curl -LSfs https://japaric.github.io/trust/install.sh | sh -s -- --git casey/just --target x86_64-unknown-linux-musl --to /opt/bin/
ENV PATH=/opt/bin:$PATH
RUN just build

FROM openjdk:11-jre
# set up timezone to Asia/Tokyo
ENV DEBIAN_FRONTEND=noninteractive
RUN set -eux; \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        ca-certificates \
        tzdata && \
        rm -rf /var/lib/apt/lists/*

RUN cp /usr/share/zoneinfo/Asia/Tokyo /etc/localtime && \
    echo "Asia/Tokyo" > /etc/timezone

RUN mkdir -p /opt/app/
WORKDIR /opt/app
COPY --from=builder /opt/app/build/libs/qicoo-0.0.1-all.jar app.jar

# expose port 8080
EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar
