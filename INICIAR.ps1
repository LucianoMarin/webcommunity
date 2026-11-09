param([switch]$EnSegundoPlano)
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
$tools = Join-Path (Split-Path $PSScriptRoot) '.tools'
$javaCommand = Get-Command java -ErrorAction SilentlyContinue
if ($javaCommand) { $java = $javaCommand.Source }
else {
    $jdk = Get-ChildItem -LiteralPath (Join-Path $tools 'jdk') -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
    if (!$jdk) { throw 'Instala Java 17 o utiliza el JDK portátil de la carpeta .tools.' }
    $java = Join-Path $jdk.FullName 'bin/java.exe'
}
$secretsPath = Join-Path $PSScriptRoot '.local-secrets.xml'
if (!(Test-Path -LiteralPath $secretsPath)) {
    $secrets = @{}
    foreach ($key in @('LOCAL_DB_PASSWORD','DEMO_ADMIN_PASSWORD','DEMO_USUARIO_PASSWORD','DEMO_USUARIO2_PASSWORD')) {
        $bytes = New-Object byte[] 24
        $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
        $rng.GetBytes($bytes); $rng.Dispose()
        $plain = [Convert]::ToBase64String($bytes)
        $secrets[$key] = ConvertTo-SecureString $plain -AsPlainText -Force
    }
    $secrets | Export-Clixml -LiteralPath $secretsPath
}
$secrets = Import-Clixml -LiteralPath $secretsPath
$credentialLines = @('Gamer Community - credenciales locales privadas', 'URL: http://127.0.0.1:8080', '')
foreach ($key in $secrets.Keys) {
    $plain = [System.Net.NetworkCredential]::new('', $secrets[$key]).Password
    [Environment]::SetEnvironmentVariable($key, $plain, 'Process')
    if ($key -like 'DEMO_*') { $credentialLines += ($key.Replace('DEMO_','').Replace('_PASSWORD','').ToLower() + ': ' + $plain) }
}
$credentialLines | Set-Content -LiteralPath '.local-credentials.txt' -Encoding utf8
# Los secretos cifrados con DPAPI sólo pueden abrirse con este usuario de Windows.
$identity = [System.Security.Principal.WindowsIdentity]::GetCurrent().Name
foreach ($file in @('.local-secrets.xml','.local-credentials.txt')) {
    & icacls.exe $file /inheritance:r /grant:r "${identity}:(F)" | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'No se pudieron proteger los archivos de credenciales locales.' }
}
$jar = Join-Path $PSScriptRoot 'target/web-0.0.1-SNAPSHOT.jar'
if (!(Test-Path -LiteralPath $jar)) { throw 'Falta compilar: ejecuta COMPILAR.ps1 primero.' }
$env:SPRING_PROFILES_ACTIVE='local'
if ($EnSegundoPlano) {
    if (Test-Path -LiteralPath '.local-process.json') {
        $old = Get-Content '.local-process.json' -Raw | ConvertFrom-Json
        $existing = Get-Process -Id $old.pid -ErrorAction SilentlyContinue
        if ($existing -and $existing.StartTime.ToUniversalTime().Ticks -eq $old.startedTicks) {
            Write-Host 'La aplicación ya está iniciada. URL: http://127.0.0.1:8080'; exit 0
        }
    }
    $process = Start-Process -FilePath $java -ArgumentList '-jar','target/web-0.0.1-SNAPSHOT.jar' -WorkingDirectory $PSScriptRoot -WindowStyle Hidden -PassThru -RedirectStandardOutput '.local-app.log' -RedirectStandardError '.local-error.log'
    @{pid=$process.Id;startedTicks=$process.StartTime.ToUniversalTime().Ticks} | ConvertTo-Json | Set-Content '.local-process.json'
    $ready=$false
    for($attempt=0;$attempt -lt 30;$attempt++) {
        if ($process.HasExited) { throw 'La aplicación no pudo arrancar. Revisa .local-error.log y .local-app.log.' }
        try { $response=Invoke-WebRequest 'http://127.0.0.1:8080/login' -UseBasicParsing -TimeoutSec 1; if($response.StatusCode -eq 200){$ready=$true;break} } catch {}
        Start-Sleep -Milliseconds 500
    }
    if (!$ready) { throw 'La aplicación sigue iniciándose. Revisa .local-app.log antes de volver a intentar.' }
    Write-Host 'Aplicación lista en http://127.0.0.1:8080. Credenciales: .local-credentials.txt'
} else {
    Write-Host 'Abre http://127.0.0.1:8080. Credenciales: .local-credentials.txt. Ctrl+C para detener.'
    & $java -jar $jar
}
