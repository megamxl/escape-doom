FROM golang:1.23
ENV GOOS=linux
ENV GOARCH=amd64
WORKDIR /opt/app
COPY . .

RUN go build -o /opt/app/main github.com/megamxl/escape-doom/CodeExecutor/cmd

FROM docker:27.4.0-rc.1-cli-alpine3.20
RUN apk add --no-cache libc6-compat
COPY --from=0 /opt/app/main main
COPY --from=0 /opt/app/docker-network.properties docker-network.properties
RUN chmod 777 main

ENTRYPOINT ["./main", "docker-network.properties"]
