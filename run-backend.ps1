# Script para ejecutar el backend de ClarityTimer
# Uso: .\run-backend.ps1

Write-Host "🚀 Iniciando ClarityTimer Backend..." -ForegroundColor Cyan

# Cambiar al directorio del script si no estamos ya ahí
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

# Verificar que existe el pom.xml
if (-not (Test-Path ".\pom.xml")) {
    Write-Host "❌ Error: No se encontró pom.xml en el directorio actual" -ForegroundColor Red
    Write-Host "   Directorio actual: $(Get-Location)" -ForegroundColor Yellow
    Write-Host "   Asegúrate de ejecutar este script desde el directorio del proyecto." -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Directorio correcto: $(Get-Location)" -ForegroundColor Green

# Verificar si Maven está instalado
$mvnCommand = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvnCommand) {
    Write-Host "⚠️  Maven no está instalado o no está en el PATH" -ForegroundColor Yellow
    Write-Host "💡 Usando Maven Wrapper (mvnw)..." -ForegroundColor Cyan
    
    # Usar Maven Wrapper si está disponible
    if (Test-Path ".\mvnw.cmd") {
        Write-Host "✅ Usando Maven Wrapper..." -ForegroundColor Green
        .\mvnw.cmd spring-boot:run
    } else {
        Write-Host "❌ No se encontró Maven Wrapper (mvnw.cmd)." -ForegroundColor Red
        Write-Host "   Por favor instala Maven o usa un IDE como IntelliJ/Eclipse." -ForegroundColor Yellow
        exit 1
    }
} else {
    Write-Host "✅ Maven encontrado. Ejecutando aplicación..." -ForegroundColor Green
    Write-Host "   Comando: mvn spring-boot:run" -ForegroundColor Cyan
    mvn spring-boot:run
}

