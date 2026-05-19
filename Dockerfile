# Étape 1 : Compilation du projet avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# On compile le projet et on génère le fichier .war
RUN mvn clean package -DskipTests

# Étape 2 : Déploiement sur le serveur Tomcat
FROM tomcat:10.1-jdk21
# On récupère le .war généré à l'étape 1
COPY --from=build /app/target/COTIZIA-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD sed -i "s/port=\"8080\"/port=\"${PORT:-8080}\"/g" /usr/local/tomcat/conf/server.xml && catalina.sh run
