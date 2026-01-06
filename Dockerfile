# Usar uma imagem leve com JRE 17
FROM eclipse-temurin:17-jre

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR já gerado para dentro do container
COPY target/terminal-file-explorer-1.0-SNAPSHOT.jar app.jar

# Comando que roda o programa quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]
