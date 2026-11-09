$ErrorActionPreference='Stop'
Set-Location -LiteralPath $PSScriptRoot
$tools=Join-Path (Split-Path $PSScriptRoot) '.tools'
if (!(Get-Command java -ErrorAction SilentlyContinue)) {
    $jdk=Get-ChildItem (Join-Path $tools 'jdk') -Directory | Select-Object -First 1
    $env:JAVA_HOME=$jdk.FullName
    $env:Path="$env:JAVA_HOME/bin;$env:Path"
}
$maven=Get-ChildItem (Join-Path $tools 'maven') -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
if ($maven) { & (Join-Path $maven.FullName 'bin/mvn.cmd') -B "-Dmaven.repo.local=$tools/m2" clean verify }
else { & ./mvnw.cmd -B clean verify }
if ($LASTEXITCODE -ne 0) { throw 'La compilación o las pruebas fallaron.' }
