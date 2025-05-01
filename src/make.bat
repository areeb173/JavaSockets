@echo off
REM -----------------------------------------------------
REM compile.bat — Compile all Java programs in this folder
REM -----------------------------------------------------

REM Change to the directory where this script resides
cd /d "%~dp0"


echo Compiling Java source files in %cd%...


REM Compile all .java files
javac *.java

REM Check for compilation errors
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo *** Compilation failed with error code %ERRORLEVEL%. ***
    pause
    exit /b %ERRORLEVEL%
)


echo *** Compilation successful! ***
pause
