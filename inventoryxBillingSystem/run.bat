@echo off
cd /d "%~dp0"
echo Compiling...
javac -d bin -sourcepath src src/Main.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation Failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Running...
java -cp bin Main
pause
