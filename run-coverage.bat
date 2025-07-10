@echo off
echo Running consolidated JaCoCo coverage report for all modules...

REM Clean previous builds
call gradlew clean

REM Run the consolidated coverage report
call gradlew jacocoConsolidatedReport

echo Coverage report generated!
echo HTML report location: build\reports\jacoco\jacocoConsolidatedReport\html\index.html
echo XML report location: build\reports\jacoco\jacocoConsolidatedReport.xml

REM Open the HTML report in default browser
if exist "build\reports\jacoco\jacocoConsolidatedReport\html\index.html" (
    echo Opening report in browser...
    start "" "build\reports\jacoco\jacocoConsolidatedReport\html\index.html"
) else (
    echo Report not found!
)
 