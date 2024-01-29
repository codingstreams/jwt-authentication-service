FROM azul/zulu-openjdk:17
LABEL authors="akxhd"

WORKDIR /app

COPY target/jwt-authentication-service-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "jwt-authentication-service-0.0.1-SNAPSHOT.jar"]