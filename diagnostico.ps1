# Script de diagnóstico para ClarityTimer Backend
Write-Host "🔍 Diagnóstico del Backend ClarityTimer" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar Java
Write-Host "1. Verificando Java..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-Object -First 1
if ($javaVersion) {
    Write-Host "   ✅ Java encontrado: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "   ❌ Java no encontrado. Instala Java 17+" -ForegroundColor Red
}

# 2. Verificar Maven
Write-Host "2. Verificando Maven..." -ForegroundColor Yellow
try {
    $mvnVersion = mvn -version 2>&1 | Select-Object -First 1
    if ($mvnVersion -and $mvnVersion -notmatch "no se reconoce") {
        Write-Host "   ✅ Maven encontrado" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Maven no encontrado, pero tenemos Maven Wrapper" -ForegroundColor Yellow
    }
} catch {
    Write-Host "   ⚠️  Maven no encontrado, pero tenemos Maven Wrapper" -ForegroundColor Yellow
}

# 3. Verificar MySQL
Write-Host "3. Verificando MySQL..." -ForegroundColor Yellow
$mysqlServices = Get-Service -Name "*mysql*" -ErrorAction SilentlyContinue
if ($mysqlServices) {
    foreach ($service in $mysqlServices) {
        Write-Host "   ✅ Servicio MySQL encontrado: $($service.Name) - Estado: $($service.Status)" -ForegroundColor Green
    }
} else {
    Write-Host "   ⚠️  No se encontraron servicios MySQL con ese nombre" -ForegroundColor Yellow
    Write-Host "      Verificando puerto 3306..." -ForegroundColor Yellow
    $mysqlPort = Test-NetConnection -ComputerName localhost -Port 3306 -InformationLevel Quiet -WarningAction SilentlyContinue
    if ($mysqlPort) {
        Write-Host "   ✅ MySQL está escuchando en el puerto 3306" -ForegroundColor Green
    } else {
        Write-Host "   ❌ MySQL no está corriendo en el puerto 3306" -ForegroundColor Red
        Write-Host "      Necesitas instalar y ejecutar MySQL" -ForegroundColor Yellow
    }
}

# 4. Verificar puerto 8080
Write-Host "4. Verificando puerto 8080..." -ForegroundColor Yellow
$backendPort = Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue
if ($backendPort) {
    Write-Host "   ✅ Backend está corriendo en el puerto 8080" -ForegroundColor Green
} else {
    Write-Host "   ❌ Backend no está corriendo en el puerto 8080" -ForegroundColor Red
}

# 5. Verificar procesos Java
Write-Host "5. Verificando procesos Java..." -ForegroundColor Yellow
$javaProcesses = Get-Process -Name java -ErrorAction SilentlyContinue
if ($javaProcesses) {
    Write-Host "   ✅ Procesos Java encontrados: $($javaProcesses.Count)" -ForegroundColor Green
    foreach ($proc in $javaProcesses) {
        Write-Host "      - PID: $($proc.Id), Inicio: $($proc.StartTime)" -ForegroundColor Cyan
    }
} else {
    Write-Host "   ❌ No hay procesos Java corriendo" -ForegroundColor Red
}

# 6. Verificar archivos del proyecto
Write-Host "6. Verificando archivos del proyecto..." -ForegroundColor Yellow
$pomExists = Test-Path ".\pom.xml"
$mvnwExists = Test-Path ".\mvnw.cmd"
$appPropsExists = Test-Path ".\src\main\resources\application.properties"

if ($pomExists) { Write-Host "   ✅ pom.xml encontrado" -ForegroundColor Green } else { Write-Host "   ❌ pom.xml no encontrado" -ForegroundColor Red }
if ($mvnwExists) { Write-Host "   ✅ mvnw.cmd encontrado" -ForegroundColor Green } else { Write-Host "   ❌ mvnw.cmd no encontrado" -ForegroundColor Red }
if ($appPropsExists) { Write-Host "   ✅ application.properties encontrado" -ForegroundColor Green } else { Write-Host "   ❌ application.properties no encontrado" -ForegroundColor Red }

Write-Host ""
Write-Host "📋 Resumen:" -ForegroundColor Cyan
Write-Host "   Si MySQL no está corriendo, el backend no podrá iniciar." -ForegroundColor Yellow
Write-Host "   Si el backend no está en el puerto 8080, revisa la ventana donde lo ejecutaste." -ForegroundColor Yellow
Write-Host ""
Write-Host "💡 Para iniciar el backend:" -ForegroundColor Cyan
Write-Host "   .\run-backend.ps1" -ForegroundColor White
Write-Host "   O manualmente: .\mvnw.cmd spring-boot:run" -ForegroundColor White

