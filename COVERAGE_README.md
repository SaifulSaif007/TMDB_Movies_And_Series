# Consolidated JaCoCo Coverage Report

This project now supports a consolidated JaCoCo coverage report that combines coverage data from all modules into a single report.

## Available Tasks

### Consolidated Report Tasks
- `jacocoConsolidatedReport` - Generates a consolidated HTML and XML report for all modules
- `jacocoConsolidatedCoverageVerification` - Verifies coverage thresholds for the consolidated report

### Individual Module Tasks (still available)
Each module still has its individual coverage tasks:
- `testDebugUnitTestCoverage` - For individual modules
- `testDebugUnitTestCoverageVerification` - For individual modules

## How to Run

### Using Gradle Commands
```bash
# Generate consolidated report
./gradlew jacocoConsolidatedReport

# Generate consolidated report with verification
./gradlew jacocoConsolidatedCoverageVerification
```

### Using Scripts
- **Linux/Mac**: `./run-coverage.sh`
- **Windows**: `run-coverage.bat`

## Report Locations

### Consolidated Reports
- **HTML Report**: `build/reports/jacoco/jacocoConsolidatedReport/html/index.html`
- **XML Report**: `build/reports/jacoco/jacocoConsolidatedReport.xml`

### Individual Module Reports
- **App Module**: `app/build/reports/jacoco/testDebugUnitTestCoverage/`
- **Movie Module**: `movie/build/reports/jacoco/testDebugUnitTestCoverage/`
- **Person Module**: `person/build/reports/jacoco/testDebugUnitTestCoverage/`
- **TV Shows Module**: `tvshows/build/reports/jacoco/testDebugUnitTestCoverage/`
- **Base Module**: `base/build/reports/jacoco/testDebugUnitTestCoverage/`
- **Shared Module**: `shared/build/reports/jacoco/testDebugUnitTestCoverage/`

## Coverage Configuration

### Excluded Files
The consolidated report excludes the following patterns:
- Generated files (R.class, BuildConfig, etc.)
- Android framework files
- Dagger/Hilt generated files
- Test files
- UI components, adapters, navigation
- Repository paging files
- Model classes

### Coverage Thresholds
- **Minimum Coverage**: 0.0% (no minimum enforced)
- **Line Coverage**: 30% minimum for BUNDLE element
- **Coverage Verification**: Available via `jacocoConsolidatedCoverageVerification`

## CI/CD Integration

The GitHub workflows have been updated to use the consolidated report:
- **Dev Workflow**: Uses `jacocoConsolidatedReport.xml`
- **Prod Workflow**: Uses `jacocoConsolidatedReport.xml`

## Benefits

1. **Single Report**: View coverage for the entire project in one place
2. **Accurate Totals**: Combined coverage percentages reflect the entire codebase
3. **CI/CD Ready**: Integrated with GitHub Actions for automated coverage reporting
4. **Maintains Individual Reports**: Still generates individual module reports for detailed analysis

## Troubleshooting

### Common Issues

1. **No Coverage Data**: Ensure all modules have tests and run `./gradlew clean` first
2. **Missing Classes**: Check that class directories are properly configured
3. **Excluded Files**: Verify exclusion patterns match your project structure

### Debug Commands
```bash
# Clean and rebuild
./gradlew clean

# Run tests for all modules
./gradlew test

# Generate consolidated report with debug info
./gradlew jacocoConsolidatedReport --info
``` 