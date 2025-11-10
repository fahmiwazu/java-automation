package base;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

import SimpleCRUDApps.pages.SimpleCRUDPage;
import utils.CrossBrowser;
import utils.EventReporter;
import utils.ScreenshotHandler;
import utils.ValidationUtils;

@ExtendWith(ScreenshotTestWatcher.class)
public class BaseTests implements WebDriverListener {
    protected static WebDriver driver;
    protected static utils.ScreenshotHandler screenshot;
    protected static SimpleCRUDPage simpleCRUDPage;
    protected static ValidationUtils validator;

    @BeforeAll
    static void setupClass() {
        // Rest Assured Setup
        RestAssured.baseURI = TestConfig.BASE_URL_DEV;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Driver Setup using CrossBrowser utility
        WebDriver baseDriver = CrossBrowser.getDriver("chrome");

        // Wrap driver with event firing decorator and custom listener
        EventReporter listener = new EventReporter();
        driver = new EventFiringDecorator<>(listener).decorate(baseDriver);

        // Initialize Page Objects
        initializePageObjects();

        // Test database connection
        // DatabaseConfig.getConnection();
    }

    /**
     * Initialize all page object instances with the WebDriver
     */
    private static void initializePageObjects() {
        // Initialize IMS pages
        screenshot = new ScreenshotHandler(driver);
        simpleCRUDPage = new SimpleCRUDPage(driver);
    }

    /**
     * Get base request specification with common headers and content type
     *
     * @return RequestSpecification with default configuration
     */
    protected RequestSpecification getBaseRequestSpec() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("User-Agent", "NicePay-API-Test/1.0")
                .log().all();
    }

    /**
     * Get WebDriver instance for use in test methods
     *
     * @return current WebDriver instance
     */
    protected static WebDriver getDriver() {
        return driver;
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        // DatabaseConfig.closeConnection();
    }
}