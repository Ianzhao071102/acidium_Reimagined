FROM eclipse-temurin:21
MAINTAINER Ian071102,dphater,rob2t,chrissy_boy,Cringester
COPY build/libs/acidium-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]