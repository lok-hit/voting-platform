# Voter Service

A Spring Boot application designed with **Domain-Driven Design (DDD)** and **Hexagonal Architecture** principles.  
The service manages voters and their votes across multiple election instances.

## Features

- **Voter Management**
    - Register new voters
    - Block/unblock voters

- **Election Integration**
    - Each election is a separate instance (e.g., "Mayor Election 2025")
    - Options (candidates) defined per election
    - Integration via `ElectionCatalog` port

- **Voting**
    - A voter can cast only one vote per election
    - Votes are tied to election options
    - Enforced by domain rules and database constraints

- **REST API**
    - Endpoints for voter registration, blocking/unblocking, and voting
    - Query endpoints for voter details and votes
    - JSON DTOs with validation

- **Persistence**
    - PostgreSQL database
    - JPA/Hibernate entities
    - Flyway migrations

## Architecture

- **Domain Layer**
    - Aggregates: `Voter`, `Vote`
    - Value Objects: `Email`, `VoterId`, `VoteId`, `ElectionId`, `OptionId`
    - Ports: `VoterRepository`, `VoteRepository`, `ElectionCatalog`, `DomainEventPublisher`
    - Events: `VoterRegistered`, `VoterBlocked`, `VoterUnblocked`, `VoteCast`

- **Application Layer**
    - Command handlers: `RegisterVoterCommandHandler`, `BlockVoterCommandHandler`, `UnblockVoterCommandHandler`, `CastVoteCommandHandler`
    - Query service: `VoterQueryService`

- **Adapters**
    - REST controllers
    - JPA repositories
    - HTTP adapter for elections
    - Messaging adapter for domain events

- **Bootstrap**
    - Spring Boot entry point
    - Configuration of beans and adapters

## Requirements

- Java 21+
- Maven 3.9+
- PostgreSQL 15+
- Flyway for migrations

## Run

```bash
mvn spring-boot:run