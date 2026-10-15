FROM maven:3.9.11-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
COPY app app
RUN mvn -q -DskipTests install
RUN mvn -q -f app/producer/pom.xml dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency
CMD ["java", "-cp", "app/producer/target/classes:app/common/target/classes:app/producer/target/dependency/*", "br.edu.ufpi.sd.producer.MessageProducer"]
