#### Run test
mvn test

#### Run application 
* docker pull postgres //get the latest postgres image
* docker run --name postgres-db -e POSTGRES_PASSWORD=docker -p 5433:5432 -d postgres
* mvn spring-boot:run

#### Run application via docker

From the root folder of the project
* ./mvnw clean package -DskipTests
* cp target/user-service-0.0.1-SNAPSHOT.jar docker/
* docker-compose -f docker/docker-compose.yml up

#### Api documentation
http://localhost:8080/swagger-ui/index.html

#### TODO
* separate business logic by restcontroller (service + dto)
* improve csv api validation (wrong fields, wrong fields type, etc...)
* add more test to cover all rest controllers

 
