FROM openjdk:11
WORKDIR /usr/src/app
COPY ./build/libs/*.jar jenkins-build.jar
ENTRYPOINT [ "java", "-jar", "jenkins-build.jar" ]