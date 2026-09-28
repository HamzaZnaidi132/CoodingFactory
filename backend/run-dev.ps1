# Démarre l'API sur le port 8090 (voir application.yml)
$port = 8090
$listeners = netstat -ano | Select-String ":\s*$port\s+.*LISTENING"
if ($listeners) {
    $pid = ($listeners.ToString() -split '\s+')[-1]
    $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
    Write-Host "Le port $port est déjà utilisé par PID $pid ($($proc.ProcessName))."
    if ($proc.ProcessName -eq 'java') {
        $answer = Read-Host "Arrêter cette instance Java et relancer ? (o/N)"
        if ($answer -match '^[oOyY]') {
            Stop-Process -Id $pid -Force
            Start-Sleep -Seconds 2
        } else {
            Write-Host "Libérez le port ou lancez: mvn spring-boot:run `"-Dspring-boot.run.arguments=--server.port=9090`""
            exit 1
        }
    } else {
        Write-Host "Changez SERVER_PORT ou arrêtez le programme qui occupe le port."
        exit 1
    }
}
mvn spring-boot:run
