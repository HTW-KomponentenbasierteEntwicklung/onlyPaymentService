#FROM openjdk:17-jdk-slim as build
#WORKDIR /workspace/app
#
#COPY mvnw mvnw.cmd ./
#COPY .mvn .mvn
#COPY pom.xml .
#COPY src src
## COPY src/main/resources/application.properties src/main/resources/
## COPY src/main/resources/application-test.properties /app/src/test/resources/
## COPY . .
#
##RUN ./mvnw install
#RUN sed -i 's/\r$//' mvnw
#RUN ./mvnw install -DskipTests
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
## RUN /bin/sh mvnw dependency:resolve
##RUN ./mvnw test
##RUN ./mvnw test -Dspring.profiles.active=test
#
#
#FROM openjdk:17-jdk-slim
##EXPOSE 8083
#VOLUME /tmp
#ARG DEPENDENCY=/workspace/app/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#
#ENTRYPOINT ["java","-cp","app:app/lib/*","de.htwberlin.paymentService.PaymentServiceApplication"]
##ENTRYPOINT ["java","-cp","app:app/lib/*","-Dspring.profiles.active=test","de.htwberlin.paymentService.PaymentServiceApplication"]

FROM openjdk:17-jdk-slim as build
WORKDIR /workspace/app

COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY . .

#RUN sed -i 's/\r$//' mvnw
#RUN /bin/sh mvnw dependency:resolve
#gg. mit: -DskipTests
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","de.htwberlin.paymentService.PaymentServiceApplication"]