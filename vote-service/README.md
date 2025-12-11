Vote-service (hexagonal + DDD)
- Responsibilities:
    - Accept votes (POST /api/elections/{id}/vote)
    - Validate via election-service and voter-service
    - Persist votes with DB-level uniqueness (one vote per voter per election)
    - Provide counts via repository (method exposed via domain service)

How to configure:
- Set clients' base URLs in application.yml (properties clients.election.base-url and clients.voter.base-url)
- Configure Postgres credentials in application.yml
- Build and run: mvn package && java -jar target/vote-service-0.0.1-SNAPSHOT.jar