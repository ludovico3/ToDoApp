FROM openjdk:21-jdk
# Indica la versione di Java
WORKDIR /app
# Indica nel container la cartella contenente il jar eseguibile dell'app
COPY target/todo-0.0.1-SNAPSHOT.jar /app/todo.jar
# Copia l'eseguibile della nostra app nella cartella app del container
EXPOSE 8081
# Il container espone l'app sulla porta 8081 (sistema locale)
CMD ["java", "-jar", "todo.jar"]
# Comando utilizzato dal container per eseguire l'app