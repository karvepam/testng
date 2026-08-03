# Simple Maven Java TestNG Project

## Project Overview
This is a beginner-friendly Maven project using Java 17 and TestNG.
It demonstrates:
- Basic calculator test automation
- TestNG lifecycle methods
- Groups (Smoke and Regression)
- Data Provider
- Expected exceptions
- Parallel execution
- Listener usage
- Programmatic execution with custom runner

## Project Structure

```text
TestNG/
├── pom.xml
├── testng.xml
├── README.md
└── src
    ├── lib
    │   ├── Calculator.jar   (add manually)
    │   └── PLACE_CALCULATOR_JAR_HERE.txt
    └── test
        └── java
            ├── listeners
            │   └── TestListener.java
            └── tests
                ├── CalculatorTests.java
                └── CustomTestRunner.java
```

## Maven Dependencies
Main dependencies used in pom.xml:
- TestNG
- Local external Calculator.jar (system scoped)

## How to Add Calculator.jar
1. Copy your external jar file into this path:
   src/lib/Calculator.jar
2. Keep the exact file name as Calculator.jar
3. Maven will load it using:
   ${project.basedir}/src/lib/Calculator.jar

## How to Run Tests
Run all tests from Maven:

```bash
mvn clean test
```

## How to Run testng.xml
Maven Surefire is already configured to run testng.xml.

Direct TestNG execution from IDE is also possible by right-clicking testng.xml and running as TestNG suite.

## TestNG Groups
Groups used:
- Smoke
- Regression

In testng.xml:
- SmokeTestsOnly includes Smoke group
- RegressionButExcludeSmoke includes Regression and excludes Smoke

## Data Provider Usage
Data provider name: additionData

Used in:
- testAdditionWithMultipleDataSets

This runs the same addition test with multiple input combinations.

## Expected Exceptions
Method:
- testDivideByZero

Annotation used:
- @Test(expectedExceptions = ArithmeticException.class)

## Parallel Execution
Configured in testng.xml:
- parallel="methods"
- thread-count="3"

This allows multiple test methods to run in parallel.

## Listener Usage
Custom listener:
- listeners.TestListener

Logs:
- Test Started
- Test Passed
- Test Failed

Registered in testng.xml under the listeners tag.

## Custom Runner Usage
Runner class:
- tests.CustomTestRunner

It runs testng.xml programmatically using:
- TestNG testng = new TestNG();
- testng.setTestSuites(...);
- testng.run();

## Potential Bugs Found
- Division by zero can crash calculation if not handled.
- Integer overflow can produce incorrect negative values.
- Invalid input strings can throw NumberFormatException.
- Boundary values near Integer.MAX_VALUE/Integer.MIN_VALUE can lead to unstable behavior.

## Jenkins Setup Steps (Documentation Only)
1. Install Jenkins on your machine/server.
2. Create a new Freestyle job.
3. Configure the Source Code Repository in job settings.
4. Add a Build Step with command:
   mvn clean test
5. Configure TestNG report publishing in post-build actions.
6. Run the job and view TestNG reports in Jenkins.
7. Install the Email Extension Plugin from Jenkins plugin manager.
8. Configure SMTP settings in Jenkins system configuration.
9. Configure recipient email addresses in the job.
10. Configure post-build email action to send TestNG report after execution.
