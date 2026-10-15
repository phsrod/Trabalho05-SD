FROM maven:3.9.11-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
COPY app app
RUN mvn -q -DskipTests install
RUN mvn -q -f app/support-consumer/pom.xml dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency
CMD ["java", "-cp", "app/support-consumer/target/classes:app/common/target/classes:app/support-consumer/target/dependency/*", "br.edu.ufpi.sd.support.SupportConsumer"]
