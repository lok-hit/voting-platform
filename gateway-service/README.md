```markdown
# Gateway - docker + konfiguracja

Pliki w tym katalogu:
- docker-compose.yml - uruchamia Redis, Postgres (przykładowo), kilka prostych mock serwisów i gateway (budowany z lokalnego Dockerfile).
- Dockerfile - buduje obraz gateway z gotowym JAR-em (w katalogu target).
- .dockerignore - ignoruje katalog target i pliki VCS.

Szybkie uruchomienie:
1. Zbuduj jar gateway:
   mvn -pl gateway-service -am package

   Upewnij się, że plik target/*.jar istnieje.

2. Uruchom docker-compose:
   docker compose up --build

3. Sprawdź endpointy:
   - Gateway: http://localhost:8080
   - Mock auth: http://localhost:8081
   - Mock election: http://localhost:8082
   - itp.

Uwagi:
- Mock serwisy użyte tutaj (hashicorp/http-echo) tylko echo'ują tekst. W prawdziwym środowisku zastąp obrazami Twoich usług (auth, election, voter, vote, infra).
- Konfiguracja gateway (application.yml) powinna używać nazwy serwisów (lb://auth-service itd.), co w konfiguracji z discovery kierowałoby do zarejestrowanych instancji. Przy uruchomieniu w docker-compose gateway łączy się z tymi usługami przez sieć dockera przez ich nazwy hostów (np. auth-service:8081). Jeżeli używasz loadbalancera bez discovery, w application.yml zmień uri na http://auth-service:8081 itp.
- Upewnij się, że masz KeyResolver bean o nazwie "ipKeyResolver" (plik GatewayConfig.java).
```