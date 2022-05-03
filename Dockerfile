FROM openjdk:11
EXPOSE  8083
WORKDIR /app
ADD   ./target/*.jar /app/product-service.jar
ENTRYPOINT ["java","-jar","/app/product-service.jar"]