param(
  [Parameter(Mandatory = $true)][string]$HostName,
  [Parameter(Mandatory = $true)][string]$Username,
  [int]$Port = 4000
)

$ErrorActionPreference = 'Stop'
$driverRoot = Join-Path $env:USERPROFILE '.m2\repository\com\mysql\mysql-connector-j'
$driver = Get-ChildItem -Path $driverRoot -Filter 'mysql-connector-j-*.jar' -Recurse |
  Sort-Object FullName |
  Select-Object -Last 1

if (-not $driver) {
  throw 'MySQL Connector/J was not found in the local Maven cache. Build the backend once, then retry.'
}

$outputDir = Join-Path (Join-Path $PSScriptRoot '..') 'sistema-m01-m02\target\tidb-connection-check'
New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
javac -d $outputDir (Join-Path $PSScriptRoot 'TestTidbConnection.java')
if ($LASTEXITCODE -ne 0) {
  throw 'Could not compile the local connection checker.'
}

$classpath = "$outputDir;$($driver.FullName)"
java -cp $classpath TestTidbConnection $HostName $Port $Username
if ($LASTEXITCODE -ne 0) {
  throw 'TiDB connection check failed.'
}
