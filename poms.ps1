# create-service-poms.ps1
# Tworzy podstawowe pliki pom.xml dla mikroserwisów VotingPlatform

$root = "C:/Lok-Hit/Onwello project/VotingPlatform"
$services = @("voter-service", "election-service", "vote-service", "gateway-service", "auth-service")

foreach ($svc in $services) {
    $svcPath = Join-Path $root $svc
    $pomPath = Join-Path $svcPath "pom.xml"

    $content = @"
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.lokhit</groupId>
        <artifactId>voting-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>$svc</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- Podstawowe zależności -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
"@

    # Utwórz plik pom.xml
    Set-Content -Path $pomPath -Value $content -Encoding UTF8
    Write-Host "Utworzono $pomPath"
}