# create-gitignore.ps1
# Skrypt tworzy plik .gitignore dla środowiska Java + IntelliJ IDEA

$gitignoreContent = @"
# Compiled class files
*.class

# Log files
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
*.jad

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# Virtual machine crash logs
hs_err_pid*

# IntelliJ IDEA
.idea/
*.iml
*.ipr
*.iws
out/

# Maven
target/

# Gradle
.gradle/
build/

# Eclipse (opcjonalnie, jeśli ktoś używa)
.classpath
.project
.settings/

# NetBeans
nbproject/private/
build/
nbbuild/
dist/
nbdist/
.nb-gradle/

# VS Code
.vscode/

# MacOS
.DS_Store

# Windows
Thumbs.db
"@

# Zapisz do pliku .gitignore w bieżącym katalogu
Set-Content -Path ".gitignore" -Value $gitignoreContent -Encoding UTF8

Write-Host ".gitignore został utworzony w bieżącym katalogu."