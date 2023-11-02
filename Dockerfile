FROM adoptopenjdk/openjdk11
EXPOSE 8080
ADD target/*.jar appwithjwt.jar
ENTRYPOINT ["java","-jar","appwithjwt.jar"]
