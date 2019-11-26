FROM openjdk:11-jre as runner
# set up timezone to Asia/Tokyo
ENV DEBIAN_FRONTEND=noninteractive
RUN set -eux; \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        ca-certificates \
        tzdata && \
        rm -rf /var/lib/apt/lists/*

RUN cp /usr/share/zoneinfo/Asia/Tokyo /etc/localtime && \
    echo Asia/Tokyo > /etc/timezone

RUN groupadd --non-unique --gid 23456 cndjp
RUN useradd --non-unique --system --uid 12345 --gid 23456 qicoo
USER qicoo