$ErrorActionPreference='Stop'
$tools=Join-Path (Split-Path $PSScriptRoot) '.tools'
$zap=Get-ChildItem (Join-Path $tools 'zap') -Directory | Where-Object { $_.Name -like 'ZAP_*' } | Select-Object -First 1
$jdk=Get-ChildItem (Join-Path $tools 'jdk') -Directory | Select-Object -First 1
if (!$zap -or !$jdk) { throw 'Se necesita la distribución portátil ZAP y Java de la carpeta .tools de este PC.' }
$jar=Get-ChildItem $zap.FullName -Filter 'zap-*.jar' | Select-Object -First 1
Set-Location -LiteralPath $zap.FullName
& (Join-Path $jdk.FullName 'bin/java.exe') -Xmx512m -jar $jar.Name -host 127.0.0.1 -port 8090 -dir ../zap-presentacion -config checkForUpdates.checkOnStart=false
