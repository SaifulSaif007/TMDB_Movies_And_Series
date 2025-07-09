@echo off
echo Opening consolidated JaCoCo coverage report...

set REPORT_PATH=build\reports\jacoco\jacocoConsolidatedReport\html\index.html

if exist "%REPORT_PATH%" (
    echo Found report at: %REPORT_PATH%
    start "" "%REPORT_PATH%"
    echo Report opened in your default browser!
) else (
    echo Report not found at: %REPORT_PATH%
    echo Please run: ./gradlew jacocoConsolidatedReport
    pause
) 
 