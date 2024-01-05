FROM bellsoft/liberica-openjdk-alpine:17

ARG GIT_TAG_REV
ENV GIT_TAG_REV=${GIT_TAG_REV:-xxxxxxx}

EXPOSE 8080

USER root

RUN addgroup --system archuser \
  && adduser --system archuser --ingroup root \
  && mkdir /target \
  && chmod 775 /target \
  && chown archuser:root /target

COPY --chown=archuser:root target/*.jar app.jar

VOLUME [ "/config" ]

USER archuser

ENTRYPOINT ["java","-jar","/app.jar"]

HEALTHCHECK --timeout=3s --start-period=5s --interval=5s  CMD wget --quiet -O - http://localhost:8080/actuator/health | grep "UP"
