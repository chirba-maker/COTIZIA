# Utilise Tomcat 10 (compatible Jakarta EE 10)
FROM tomcat:10.1-jdk19
# Copie le fichier WAR compilé dans le dossier webapps de Tomcat
COPY target/COTIZIA-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
