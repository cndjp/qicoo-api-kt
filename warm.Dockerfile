FROM alpine:3.10
RUN apk update
RUN apk add --no-cache curl ca-certificates