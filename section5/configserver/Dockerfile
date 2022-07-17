#Start with a base image containing Java runtime
FROM openjdk:11-slim as build

#Information around who maintains the image
#MAINTAINER eazybytes.com
 
# Add the application's jar to the container
COPY target/*.jar configserver-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/configserver-0.0.1-SNAPSHOT.jar"]