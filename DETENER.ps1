$ErrorActionPreference='Stop'
Set-Location -LiteralPath $PSScriptRoot
if (!(Test-Path '.local-process.json')) { Write-Host 'No hay proceso registrado. Si se inició en consola, usa Ctrl+C.'; exit 0 }
$saved=Get-Content '.local-process.json' -Raw | ConvertFrom-Json
$process=Get-Process -Id $saved.pid -ErrorAction SilentlyContinue
if ($process -and $process.StartTime.ToUniversalTime().Ticks -eq $saved.startedTicks) {
    Stop-Process -Id $process.Id
    Write-Host 'Aplicación detenida.'
} else { Write-Host 'El proceso registrado ya no está activo.' }
Remove-Item -LiteralPath '.local-process.json'
