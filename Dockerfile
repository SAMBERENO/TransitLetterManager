FROM openjdk:25-ea-jdk
WORKDIR ListManagmentAppApplication.java
RUN javac ListManagmentAppApplication.java
CMD ["java"]
