FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.authors=iisaevagulzada@gmail.com
COPY target/case-study.jar case-study.jar
ENTRYPOINT ["java","-jar","/case-study.jar"]