#!/bin/bash

# Konfigurationsvariabler
DATE=$(date +"%Y%m%d%H%M")  # Tidsstämpel
BACKUP_DIR="/c/Users/Gustav\ Parment/Documents/DBHantering/Weather_App_JavaEE/recovery"# Backup-mapp
DB_NAME="weatherdb-projekt-arbete"
DB_USER="postgres"
DB_PASSWORD="2gfv77f6"
DB_HOST="localhost"
DB_PORT="5432"
SPRING_PROJECT_DIR="/c/Users/Gustav\ Parment/Documents/DBHantering/Weather_App_JavaEE"  # Ändra till projektkatalogen
SPRING_BACKUP="$BACKUP_DIR/spring_project_backup_$DATE.zip"

# Exportera lösenord för PostgreSQL för att slippa prompts
export PGPASSWORD=$DB_PASSWORD

# Kontrollera om backup-mappen finns, annars skapa den
mkdir -p "$BACKUP_DIR"

# Säkerhetskopiera PostgreSQL-databasen
echo "Säkerhetskopierar databasen $DB_NAME..."
pg_dump -U $DB_USER -h $DB_HOST -p $DB_PORT $DB_NAME > "$BACKUP_DIR/db_backup_$DATE.sql"
if [ $? -eq 0 ]; then
  echo "Databas-backup skapad: $BACKUP_DIR/db_backup_$DATE.sql"
else
  echo "Fel vid säkerhetskopiering av databasen!"
  exit 1
fi

# Säkerhetskopiera Spring Boot-projektet
echo "Säkerhetskopierar Spring Boot-projektet..."
zip -r "$SPRING_BACKUP" "$SPRING_PROJECT_DIR"
if [ $? -eq 0 ]; then
  echo "Spring Boot-projekt-backup skapad: $SPRING_BACKUP"
else
  echo "Fel vid säkerhetskopiering av Spring Boot-projektet!"
  exit 1
fi

# Rensa lösenordsvariabeln
unset PGPASSWORD

echo "Backup-processen är klar!"
