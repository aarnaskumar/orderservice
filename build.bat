@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21
cd /d C:\Users\pc\IdeaProjects\orderservice
java -version
echo Building Order Service...
java -jar .mvn/wrapper/maven-wrapper.jar clean install

