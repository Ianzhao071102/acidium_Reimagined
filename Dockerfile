FROM eclipse-temurin:21
MAINTAINER Ian071102,dphater,rob2t,chrissy_boy,Cringester,HenriSuppr
COPY build/libs/acidium-0.0.1-SNAPSHOT.jar app.jar
COPY docker-compose.yml docker-compose.yml
ENTRYPOINT ["java","-jar","/app.jar"]