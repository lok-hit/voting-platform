# Election Service

A Spring Boot application designed with **Domain-Driven Design (DDD)** and **Hexagonal Architecture** principles.  
The service manages elections and their options (candidates), providing APIs to create, close, and query elections.

## Features

- **Election Management**
    - Create new elections
    - Close elections (prevent adding new options or votes)

- **Option Management**
    - Add options (candidates) to an election
    - Query options per election

- **REST API**
    - Endpoints for creating, closing, and querying elections
    - Endpoints for adding and listing options
    - JSON DTOs with validation and error handling

- **Persistence**
    - PostgreSQL database
    - JPA/Hibernate entities with proper relations
    - Flyway migrations for schema evolution

- **Events**
    - Domain events published via Spring’s `ApplicationEventPublisher`
    - Events: `ElectionCreated`, `ElectionClosed`, `OptionAdded`

## Architecture

- **Domain Layer**
    - Aggregates: `Election`, `Option`
    - Value Objects: `ElectionId`, `OptionId`, `ElectionStatus`, `TimeSource`
    - Ports: `ElectionRepository`, `OptionRepository`, `DomainEventPublisher`
    - Events: `ElectionCreated`, `ElectionClosed`, `OptionAdded`

- **Application Layer**
    - Command handlers: `CreateElectionCommandHandler`, `CloseElectionCommandHandler`, `AddOptionCommandHandler`
    - Query services: `ElectionQueryService`, `OptionQueryService`

- **Adapters**
    - REST controllers with DTOs and exception handling
    - JPA repositories with entity mappers
    - Messaging adapter for domain events

- **Bootstrap**
    - Spring Boot entry point (`ElectionApplication`)
    - Configuration of beans (`BootstrapConfig`)

## Requirements

- Java 21+
- Maven 3.9+
- PostgreSQL 15+
- Flyway for migrations

## Run

```bash
mvn spring-boot:run