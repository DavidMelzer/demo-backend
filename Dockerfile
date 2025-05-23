# 1. Stage: Build the application using Maven and Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Kopiere alles aus dem Build-Kontext
COPY . .

# Debug: Zeige, was da überhaupt drin liegt
RUN echo "🧾 Inhalt von /app:" && ls -la /app
RUN echo "📄 pom.xml Inhalt:" && cat /app/pom.xml || echo "❌ pom.xml nicht gefunden"

# Debug: Maven-Tree anzeigen (optional)
RUN echo "📦 Maven-Projektstruktur (pom dependencies):" && mvn dependency:tree || echo "❌ Maven dependency:tree fehlgeschlagen"

# Bauen des Projekts
RUN echo "🚀 Starte Build..." && mvn clean package -DskipTests

# 2. Stage: Runtime mit Java 21 JDK (oder JRE für schlankeres Image)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /work

# Debug: Ordner vorbereiten
RUN echo "📦 Kopiere App zur Runtime Stage..."

COPY --from=build /app/target/quarkus-app/ .

# Debug: Zeige, was wirklich kopiert wurde
RUN echo "✅ Inhalt von /work:" && ls -la /work

# Start-Kommando
CMD ["java", "-jar", "quarkus-run.jar"]
