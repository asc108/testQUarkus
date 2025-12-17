# Quarkus Demo Project

Jednostavan Quarkus projekat sa PostgreSQL i Liquibase integracijom.

## Preduslovi

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

## Pokretanje lokalno

### 1. Pokreni PostgreSQL
```bash
docker-compose up -d postgres
```

### 2. Pokreni aplikaciju
```bash
mvn quarkus:dev
```

Aplikacija će biti dostupna na: http://localhost:8080

## Testiranje

### Pokreni testove (sa Testcontainers)
```bash
mvn test
```

### API Endpoints

- GET /persons - Lista svih osoba
- GET /persons/{id} - Jedna osoba po ID
- POST /persons - Kreiraj novu osobu

### Primer POST requesta
```bash
curl -X POST http://localhost:8080/persons \
  -H "Content-Type: application/json" \
  -d '{"name":"Nova Osoba","email":"nova@example.com"}'
```

## Docker Build

### Build jar
```bash
mvn clean package
```

### Build Docker image
```bash
docker build -t quarkus-demo .
```

### Pokreni sve sa Docker Compose
```bash
docker-compose up --build
```

## Jenkins Pipeline

Jenkinsfile je konfigurisan za:
1. Build Maven projekta
2. Pokretanje testova sa Testcontainers
3. Build Docker image-a
4. Push u registry (opciono)
5. Deploy sa Docker Compose

### Jenkins Setup

Potrebno je konfigurisati:
- Maven 3.9 (ime: 'Maven 3.9')
- JDK 17 (ime: 'JDK 17')
- Docker na Jenkins agentu

## Struktura projekta

```
.
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Person.java
│   │   │   └── PersonResource.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/changelog/
│   └── test/
│       ├── java/com/example/
│       └── resources/
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
└── pom.xml
```

## Napomene

- Testovi koriste Testcontainers za PostgreSQL
- Liquibase automatski kreira tabele i dummy podatke
- Docker image koristi UBI8 sa OpenJDK 17
