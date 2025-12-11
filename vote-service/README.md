# Vote Service (Hexagonal + DDD)

## Responsibilities
- Accept votes (POST /api/elections/{id}/vote)
- Validate via election-service and voter-service
- Persist votes with DB-level uniqueness (one vote per voter per election)
- Provide counts via repository (method exposed via domain service)

## Configuration

### Required Properties

These properties must be set for the application to start:

1. **Service Dependencies**:
   ```yaml
   clients:
     election:
       base-url: http://election-service:8080  # URL of the election service
     voter:
       base-url: http://voter-service:8080     # URL of the voter service
   ```

2. **Database Configuration**:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/votedb
       username: postgres
       password: your-password
     jpa:
       hibernate:
         ddl-auto: update
   ```

### How to Run

#### Using Environment Variables
```bash
export SPRING_APPLICATION_JSON='{"clients":{"election":{"base-url":"http://election-service:8080"},"voter":{"base-url":"http://voter-service:8080"}}}'
mvn spring-boot:run
```

#### Using Command Line Arguments
```bash
java -jar target/vote-service-0.0.1-SNAPSHOT.jar \
  --clients.election.base-url=http://election-service:8080 \
  --clients.voter.base-url=http://voter-service:8080
```

#### Using application.yml
Create or update `src/main/resources/application.yml` with the configuration above.

## Development

### Building
```bash
mvn clean package
```

### Running Tests
```bash
mvn test
```

### Running with Docker
```bash
docker build -t vote-service .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/votedb \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=your-password \
  -e CLIENTS_ELECTION_BASE_URL=http://election-service:8080 \
  -e CLIENTS_VOTER_BASE_URL=http://voter-service:8080 \
  vote-service