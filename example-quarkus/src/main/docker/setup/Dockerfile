FROM alpine

COPY setup.sh /tmp/

RUN apk add bash curl jq \
 && chmod a+x /tmp/setup.sh

ENTRYPOINT [ "/tmp/setup.sh" ]
