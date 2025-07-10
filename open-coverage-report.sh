#!/bin/bash

echo "Opening consolidated JaCoCo coverage report..."

REPORT_PATH="build/reports/jacoco/jacocoConsolidatedReport/html/index.html"

if [ -f "$REPORT_PATH" ]; then
    echo "Found report at: $REPORT_PATH"
    
    # Try different commands to open the browser
    if command -v xdg-open > /dev/null; then
        xdg-open "$REPORT_PATH"
    elif command -v open > /dev/null; then
        open "$REPORT_PATH"
    elif command -v sensible-browser > /dev/null; then
        sensible-browser "$REPORT_PATH"
    else
        echo "Could not find a suitable browser command"
        echo "Please open manually: $REPORT_PATH"
    fi
    
    echo "Report opened in your default browser!"
else
    echo "Report not found at: $REPORT_PATH"
    echo "Please run: ./gradlew jacocoConsolidatedReport"
fi 