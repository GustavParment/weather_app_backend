# Starta med en JDK 21-bild
FROM eclipse-temurin:21-jdk

# Ange en arbetskatalog i containern
WORKDIR /app

# Kopiera keystore-fil
COPY build/resources/main/keystore.p12 /app/keystore.p12


# Kopiera Spring Boot-jar-filen (byt 'weather_app.jar' mot namnet på din JAR-fil)
COPY build/libs/Weather_App_JavaEE-0.0.1-SNAPSHOT.jar /app/weather_app.jar


# Ange miljövariabler för Java
ENV JAVA_OPTS="-Dserver.port=8443 -Dserver.ssl.key-store=/app/keystore.p12 -Dserver.ssl.key-store-password=2gfv77f6 -Dserver.ssl.key-store-type=PKCS12"

# Öppna port 8443
EXPOSE 8443

# Starta Spring Boot-applikationen
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/weather_app.jar"]
