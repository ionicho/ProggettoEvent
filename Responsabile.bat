@echo off
echo Avvio dell'applicazione Responsabile...

rem Imposta il percorso del progetto
set PROJECT_DIR=%~dp0

rem Esegui l'applicazione ResponsabileApplication utilizzando Maven
mvn exec:java -Dexec.mainClass="client.ResponsabileApplication"

pause
