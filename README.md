# Java Automation Framework

A comprehensive test automation framework built with Java, Selenium WebDriver, REST Assured, JUnit 5, and Allure reporting. This framework supports both UI and API testing with advanced features like parallel execution, cross-browser testing, and detailed reporting.

## 🚀 Features

### Core Capabilities
- **UI Testing**: Selenium WebDriver with cross-browser support
- **API Testing**: REST Assured for comprehensive API validation
- **Parallel Execution**: JUnit 5 parallel test execution
- **Reporting**: Allure reports with screenshots and detailed logs
- **Cross-Browser**: Chrome, Firefox, Edge, Safari, and IE support
- **CI/CD Ready**: GitHub Actions integration with automated report deployment

### Advanced Features
- **Event-Driven Logging**: Comprehensive WebDriver event reporting
- **Safe Actions**: Enhanced element interactions with highlighting and scrolling
- **Screenshot Management**: Automatic failure screenshots with Allure integration
- **Validation Utilities**: Flexible validation framework with detailed reporting
- **Configuration Management**: Environment-based configuration support

## 📈 Framework Metrics

- **Languages**: Java 19
- **Test Framework**: JUnit 5
- **UI Automation**: Selenium WebDriver 4.35.0
- **API Testing**: REST Assured 5.5.5
- **Reporting**: Allure 2.29.1
- **Build Tool**: Maven 3.14.0
- **CI/CD**: GitHub Actions
- **Browsers Supported**: Chrome, Firefox, Edge, Safari, IE

## 📁 Project Structure

```
fahmi-java-framework/
├── src/
│   ├── main/java/
│   │   ├── config/
│   │   │   ├── ConfigLoader.java          # Configuration loader
│   │   │   └── TestConfig.java            # Test configuration constants
│   │   └── utils/
│   │       ├── EventReporter.java         # WebDriver event logging
│   │       ├── SafeAction.java            # Enhanced element interactions
│   │       └── ScreenshotHandler.java     # Screenshot management
│   ├── test/java/
│   │   ├── base/
│   │   │   ├── BaseTests.java             # Base test class
│   │   │   ├── ScreenshotCapable.java     # Screenshot interface
│   │   │   └── ScreenshotTestWatcher.java # Test failure handler
│   │   ├── experimental/
│   │   │   └── PortfolioTest.java               # Portfolio site test
│   │   └── utils/
│   │       ├── CrossBrowser.java          # Browser management
│   │       └── ValidationUtils.java       # Validation framework
│   └── main/resources/
│       └── dev.properties                 # Environment configuration
├── .github/workflows/
│   └── ci.yml                            # CI/CD pipeline
├── pom.xml                               # Maven dependencies
└── README.md                             # This file
```

## 🛠️ Technologies & Dependencies

### Core Technologies

- **Java 19**: Programming language
- **Maven**: Dependency management and build tool
- **JUnit 5**: Testing framework with parallel execution support
- **Selenium WebDriver 4.35.0**: UI automation
- **REST Assured 5.5.5**: API testing
- **Allure 2.29.1**: Test reporting and visualization

### Key Dependencies

- **WebDriverManager 6.1.0**: Automatic driver management
- **Jackson 2.19.2**: JSON processing
- **Apache Commons**: Utility libraries
- **AssertJ 3.27.3**: Fluent assertions
- **Hamcrest 3.0**: Matchers for testing
- **Lombok 1.18.30**: Boilerplate code reduction

## 🚦 Getting Started

### Prerequisites

- **Java 19** or higher
- **Maven 3.6+**
- **Chrome browser** (for default execution)
- **Allure CLI** (optional, for local report generation)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd fahmi-java-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure environment** (optional)

   Edit `src/main/resources/dev.properties`:
   ```properties
   PROJECT_DIR=C:\\path\\to\\your\\project
   BASE_URL_DEV=https://your-test-site.com
   GENERATE_ALLURE_HTML_REPORT=allure generate --single-file target/allure-results -o target/allure-report --clean
   ```

### Running Tests

#### Basic Test Execution
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn clean test -Dtest=PortfolioTest

# Run with specific browser
mvn clean test -Dbrowser=firefox

# Run in headless mode (CI environment)
mvn clean test -DHEADLESS=true
```

#### Parallel Execution
Tests are configured to run in parallel by default. Configuration in `src/test/resources/junit-platform.properties`:
```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.classes.default=concurrent
```

## 📊 Test Reporting

### Allure Reports

The framework automatically generates Allure reports with:

- **Test execution results** with pass/fail status
- **Screenshots** for failed tests
- **Step-by-step execution** details
- **API request/response** logs
- **Environment information**

#### Generate Local Report
```bash
mvn clean test
allure serve target/allure-results
```

#### View Reports in CI
Reports are automatically deployed to GitHub Pages after each CI run. Access via:
`https://<username>.github.io/<repository-name>/`

## 🌐 Cross-Browser Support

### Supported Browsers
```java
// Chrome (default)
WebDriver driver = CrossBrowser.getDriver("chrome");

// Firefox
WebDriver driver = CrossBrowser.getDriver("firefox");

// Edge
WebDriver driver = CrossBrowser.getDriver("edge");

// Safari (macOS only)
WebDriver driver = CrossBrowser.getDriver("safari");

// Headless Chrome (CI/CD)
WebDriver driver = CrossBrowser.getHeadlessChromeDriver();
```

### Browser Configuration

Each browser includes optimized settings:

- **Chrome**: Headless support, incognito mode, automation flags
- **Firefox**: Private browsing, notification blocking
- **Edge**: InPrivate mode, automation compatibility
- **Safari**: macOS-specific configurations

## 🔧 Framework Components

### SafeAction Utility
Enhanced element interactions with automatic retry and highlighting:
```java
SafeAction safeAction = new SafeAction(driver);

// Safe clicking with scroll and highlight
safeAction.safeClick(By.id("submitButton"));

// Safe text input with validation
safeAction.safeInput(By.id("username"), "testuser");

// Dropdown selection
safeAction.selectFromDropdown(By.id("country"), "United States");

// Wait for elements with highlighting
safeAction.waitForElementToBeVisible(By.className("loading"));
```

### Validation Framework
Flexible validation system with detailed reporting:
```java
ValidationUtils validator = new ValidationUtils(driver, "TestClassName");

// Different assertion types
validator.assertEquals("Login Title", "Expected Title", actualTitle);
validator.assertNotNull("User Profile", userProfile);
validator.assertTrue("Terms Accepted", isTermsChecked);

// Check all validations at test end
validator.checkValidationResults(); // Fails test if any validation failed
// OR
validator.printValidationSummary(); // Just prints summary
```

### Screenshot Management
Automatic screenshot capture for failures and manual screenshots:
```java
ScreenshotHandler screenshot = new ScreenshotHandler(driver, "TestClass");

// Automatic failure screenshots (via ScreenshotTestWatcher)
// Manual screenshots
screenshot.takeFullPageScreenshot("login-page");
screenshot.attachScreenshotToAllure("Step 1 - Login Form");

// Highlighted element screenshots
screenshot.takeHighlightedElementScreenshot(By.id("error-message"), "error-state");
```

## 🔄 CI/CD Pipeline

### GitHub Actions Workflow
The framework includes a complete CI/CD pipeline (`.github/workflows/ci.yml`) that:

1. **Environment Setup**
    - Java 19 with Maven caching
    - Chrome browser installation
    - Allure CLI setup

2. **Test Execution**
    - Clean test runs with parallel execution
    - Environment variable support
    - Comprehensive logging

3. **Report Generation**
    - Automatic Allure report generation
    - GitHub Pages deployment
    - Artifact preservation

4. **Features**
    - Runs on every push to main branch
    - Parallel job execution
    - Detailed logging and error handling
    - Automatic cleanup

### Pipeline Status
```yaml
# Trigger on main branch push
on:
  push:
    branches: [main]

# Parallel execution with 2 threads
parallel: classes
threadCount: 2
```


## 🎯 Best Practices

### Test Organization
- **Base Classes**: Extend `BaseTests` for standard UI tests
- **Page Objects**: Implement page object pattern for maintainability
- **Test Data**: Use parameterized tests for data-driven testing
- **Assertions**: Use `ValidationUtils` for detailed validation reporting

### Error Handling
- **Automatic Screenshots**: Failure screenshots via `ScreenshotTestWatcher`
- **Retry Mechanisms**: Built into `SafeAction` utilities
- **Graceful Degradation**: Cross-browser fallbacks
- **Detailed Logging**: Event-driven logging for debugging

### Performance
- **Parallel Execution**: JUnit 5 parallel test execution
- **Driver Management**: Automatic WebDriver lifecycle management
- **Resource Cleanup**: Proper driver cleanup after tests
- **Caching**: Maven dependency caching in CI

## 🐛 Troubleshooting

### Common Issues

#### WebDriver Issues
```bash
# Clear WebDriver cache
mvn clean test -Dwdm.clearCache=true

# Force driver update
mvn clean test -Dwdm.forceCache=false
```

#### Memory Issues
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2048m -XX:MaxMetaspaceSize=512m"
```

#### Parallel Execution Issues
```properties
# Reduce parallelism in junit-platform.properties
junit.jupiter.execution.parallel.config.fixed.parallelism=1
```

### Debug Mode
Enable detailed logging by modifying the `EventReporter` configuration:
```java
EventReporter listener = new EventReporter(
    true,  // logNavigation
    true,  // logElementInteractions  
    true   // logDriverActions
);
```

## 🤝 Contributing

1. **Fork the repository**
2. **Create feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit changes**: `git commit -m 'Add amazing feature'`
4. **Push to branch**: `git push origin feature/amazing-feature`
5. **Open Pull Request**

### Development Guidelines
- Follow existing code style and patterns
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 🔗 Quick Links

- **Framework Repository**: [https://github.com/fahmiwazu/java-automation](https://github.com/fahmiwazu/java-automation)
- **Live Test Results**: [https://fahmiwazu.github.io/java-automation](https://fahmiwazu.github.io/java-automation)


## 📄 License

This project is licensed under the Apache 2.0 License.

## 📞 Support

For questions and support:

- **Issues**: Create GitHub issues for bugs and feature requests
- **Documentation**: Check existing tests for usage examples
- **Allure Reports**: Review generated reports for test execution details

---

**Happy Testing! 🎉**