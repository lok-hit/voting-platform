# Utwórz główny katalog
$root = "VotingPlatform"
New-Item -ItemType Directory -Force -Path $root

# Lista mikroserwisów
$services = @("voter-service", "election-service", "vote-service", "gateway-service", "auth-service")

foreach ($svc in $services) {
    $base = Join-Path $root $svc
    New-Item -ItemType Directory -Force -Path $base
    New-Item -ItemType Directory -Force -Path (Join-Path $base "src\main\java\com\example\$($svc.Replace('-',''))")
    New-Item -ItemType Directory -Force -Path (Join-Path $base "src\test\java\com\example\$($svc.Replace('-',''))")
    New-Item -ItemType Directory -Force -Path (Join-Path $base "resources")
}

# Frontend
New-Item -ItemType Directory -Force -Path (Join-Path $root "frontend\angular-app")
New-Item -ItemType Directory -Force -Path (Join-Path $root "frontend\vue-app")

# Infra
New-Item -ItemType Directory -Force -Path (Join-Path $root "infra\docker")
New-Item -ItemType Directory -Force -Path (Join-Path $root "infra\k8s")

Write-Host "Struktura katalogów została utworzona."