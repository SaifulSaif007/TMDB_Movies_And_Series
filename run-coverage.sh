#!/bin/bash

echo "Running consolidated JaCoCo coverage report for all modules..."

# Clean previous builds
./gradlew clean

# Run the consolidated coverage report
./gradlew jacocoConsolidatedReport

echo "Coverage report generated!"
echo "HTML report location: build/reports/jacoco/jacocoConsolidatedReport/html/index.html"
echo "XML report location: build/reports/jacoco/jacocoConsolidatedReport.xml"

# Optional: Open the HTML report in default browser (uncomment if you want this)
# if command -v xdg-open > /dev/null; then
#     xdg-open build/reports/jacoco/jacocoConsolidatedReport/html/index.html
# elif command -v open > /dev/null; then
#     open build/reports/jacoco/jacocoConsolidatedReport/html/index.html
# fi 