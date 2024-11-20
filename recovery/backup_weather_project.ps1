# Variabler
$Date = Get-Date -Format "yyyyMMddHHmm"
$BackupDir = "C:\Users\Gustav Parment\Documents\DBHantering\Weather_App_JavaEE\recovery"
$DbName = "weatherdb-projekt-arbete"
$DbUser = "postgres"
$DbPassword = "2gfv77f6"
$DbHost = "localhost"
$DbPort = "5432"
$SpringProjectDir = "C:\Users\Gustav Parment\Documents\DBHantering\Weather_App_JavaEE"  # Ändra till projektkatalogen
$SpringBackup = "$BackupDir\spring_project_backup_$Date.zip"

# Skapa backup-mappen om den inte finns
if (!(Test-Path -Path $BackupDir)) {
    New-Item -ItemType Directory -Path $BackupDir
}

# Säkerhetskopiera PostgreSQL-databasen
Write-Host "Säkerhetskopierar databasen $DbName..."
$Env:PGPASSWORD = $DbPassword
& "C:\Program Files\PostgreSQL\16\bin\pg_dump.exe" -U $DbUser -h $DbHost -p $DbPort $DbName > "$BackupDir\db_backup_$Date.sql"
if ($?) {
    Write-Host "Databas-backup skapad: $BackupDir\db_backup_$Date.sql"
} else {
    Write-Host "Fel vid säkerhetskopiering av databasen!"
    exit 1
}

# Säkerhetskopiera Spring Boot-projektet
Write-Host "Säkerhetskopierar Spring Boot-projektet..."
Compress-Archive -Path "$SpringProjectDir\*" -DestinationPath $SpringBackup
if ($?) {
    Write-Host "Spring Boot-projekt-backup skapad: $SpringBackup"
} else {
    Write-Host "Fel vid säkerhetskopiering av Spring Boot-projektet!"
    exit 1
}

# Rensa lösenordsvariabeln
Remove-Item Env:\PGPASSWORD

Write-Host "Backup-processen är klar!"
