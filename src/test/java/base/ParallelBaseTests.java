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

import pages.SimpleCRUDPage;
import utils.CrossBrowser;
import utils.EventReporter;
import utils.ScreenshotHandler;
import utils.ValidationUtils;

@ExtendWith(ScreenshotTestWatcher.class)
public class ParallelBaseTests implements WebDriverListener {
    // Thread-safe storage for WebDriver instances
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<ScreenshotHandler> screenshotHandlerThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<SimpleCRUDPage> simpleCRUDPage = new ThreadLocal<>();
    protected static ThreadLocal<ValidationUtils> validator = new ThreadLocal<>();

    @BeforeAll
    static void setupClass() {
        // Rest Assured Setup (shared across threads)
        RestAssured.baseURI = TestConfig.BASE_URL_DEV;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setUp() {
        // Create new WebDriver instance for each test thread
        WebDriver baseDriver = CrossBrowser.getDriver("chrome");

        // Wrap driver with event firing decorator and custom listener
        EventReporter listener = new EventReporter();
        WebDriver decoratedDriver = new EventFiringDecorator<>(listener).decorate(baseDriver);

        // Store in ThreadLocal
        driver.set(decoratedDriver);

        // Initialize Page Objects for this thread
        initializePageObjects();
    }

    /**
     * Initialize all page object instances with the WebDriver for current thread
     */
    private void initializePageObjects() {
        WebDriver currentDriver = driver.get();
        screenshotHandlerThreadLocal.set(new ScreenshotHandler(currentDriver));
        simpleCRUDPage.set(new SimpleCRUDPage(currentDriver));
        validator.set(new ValidationUtils(currentDriver, this.getClass().getSimpleName()));
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
     * Get WebDriver instance for current thread
     *
     * @return current thread's WebDriver instance
     */
    protected static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Get ScreenshotHandler instance for current thread
     * Method name matches ScreenshotTestWatcher's reflection lookup
     */
    protected static ScreenshotHandler getScreenshotHandler() {
        return screenshotHandlerThreadLocal.get();
    }

    /**
     * Alias for getScreenshotHandler() for convenience
     */
    protected static ScreenshotHandler getScreenshot() {
        return screenshotHandlerThreadLocal.get();
    }

    /**
     * Get SimpleCRUDPage instance for current thread
     */
    protected static SimpleCRUDPage getSimpleCRUDPage() {
        return simpleCRUDPage.get();
    }

    /**
     * Get ValidationUtils instance for current thread
     */
    protected static ValidationUtils getValidator() {
        return validator.get();
    }

    @AfterEach
    void tearDown() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            currentDriver.quit();
            driver.remove(); // Clean up ThreadLocal
        }

        // Clean up other ThreadLocal variables
        screenshotHandlerThreadLocal.remove();
        simpleCRUDPage.remove();
        validator.remove();
    }

    /**
     * Method called by ScreenshotTestWatcher to cleanup driver resources
     * Required for parallel test execution compatibility
     */
    public static void cleanupDriverResources() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } catch (Exception e) {
                System.err.println("Error during driver cleanup: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }

        // Clean up all ThreadLocal variables
        screenshotHandlerThreadLocal.remove();
        simpleCRUDPage.remove();
        validator.remove();
    }
}