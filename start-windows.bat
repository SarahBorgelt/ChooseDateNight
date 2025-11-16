@echo off
@REM @echo off turns of the command echoing so that the terminal doesn't print each command before running it

@REM Navigate to project directory (script location). %~dp0 is a special Windows batch variable: drive letter + path of the batch file
@REM /d ensures that it switches drives if necessary
cd /d %~dp0

@REM prints a message to the terminal so the user knows what is happening. It runs Maven to start the Spring Boot application
echo Starting BucketList on Windows...
mvn spring-boot:run

@REM pause keeps the terminal open after the program ends, displaying the "Press any key to continue..." message.
pause