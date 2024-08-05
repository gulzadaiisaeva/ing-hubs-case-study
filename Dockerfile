FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.authors=iisaevagulzada@gmail.com
COPY target/case-study-0.0.1-SNAPSHOT.jar case-study-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/case-study-0.0.1-SNAPSHOT.jar"]