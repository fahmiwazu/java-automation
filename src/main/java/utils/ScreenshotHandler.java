package utils;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Enhanced ScreenshotHandler with better error handling and thread safety
 *
 * @param testClassName Getters
 */
public record ScreenshotHandler(WebDriver driver, String testClassName) {
    private static WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(ScreenshotHandler.class.getName());
    private static final String BASE_SCREENSHOT_DIR = "evidence/screenshot";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public ScreenshotHandler(WebDriver driver) {
        this(driver, determineTestClassName());
    }

    public ScreenshotHandler(WebDriver driver, String testClassName) {
        this.driver = validateDriver(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.testClassName = testClassName;
        createScreenshotDirectory();
    }

    /**
     * Validates WebDriver instance
     */
    private WebDriver validateDriver(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver cannot be null");
        }

        // Check if driver is still valid
        try {
            driver.getTitle(); // This will throw if driver is closed
            return driver;
        } catch (Exception e) {
            throw new IllegalStateException("WebDriver is not in a valid state", e);
        }
    }

    /**
     * Takes a failure screenshot with comprehensive error handling
     */
    public String takeFailureScreenshot(String testName, String testClass) {
        return takeFailureScreenshot(testName, testClass, 3); // 3 retry attempts
    }

    /**
     * Takes a failure screenshot with retry mechanism
     */
    public String takeFailureScreenshot(String testName, String testClass, int maxRetries) {
        String fileName = "FAILURE_" + testClass + "_" + testName + "_" +
                LocalDateTime.now().format(DATE_FORMAT);

        Exception lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String screenshotPath = takeScreenshotInternal(fileName);
                logger.info("Failure screenshot captured on attempt " + attempt + ": " + screenshotPath);
                return screenshotPath;

            } catch (Exception e) {
                lastException = e;
                logger.warning("Screenshot attempt " + attempt + " failed: " + e.getMessage());

                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(1000); // Wait before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        // All attempts failed
        logger.log(Level.SEVERE, "Failed to capture screenshot after " + maxRetries + " attempts", lastException);
        throw new RuntimeException("Screenshot capture failed after " + maxRetries + " attempts", lastException);
    }

    /**
     * Takes a full page screenshot
     *
     * @param fileName Name of the screenshot file (without extension)
     * @return File path of the saved screenshot
     */
    public String takeFullPageScreenshot(String fileName) throws InterruptedException {
        Thread.sleep(500);
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            String filePath = generateFilePath(fileName);
            File destFile = new File(filePath);

            FileUtils.moveFile(sourceFile, destFile);
            logger.info("Full page screenshot saved: " + filePath);
            return filePath;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to take full page screenshot", e);
            throw new RuntimeException("Screenshot capture failed", e);
        }
    }

    /**
     * Internal screenshot capture method
     */
    private String takeScreenshotInternal(String fileName) throws IOException {
        // Validate driver state before taking screenshot
        if (isDriverClosed()) {
            throw new IllegalStateException("Cannot take screenshot: WebDriver is closed");
        }

        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            String filePath = generateFilePath(fileName);
            File destFile = new File(filePath);

            // Ensure parent directory exists
            destFile.getParentFile().mkdirs();

            FileUtils.moveFile(sourceFile, destFile);
            return filePath;

        } catch (WebDriverException e) {
            // Handle specific WebDriver exceptions
            if (Objects.requireNonNull(e.getMessage()).contains("chrome not reachable") ||
                    e.getMessage().contains("session deleted")) {
                throw new IllegalStateException("WebDriver session is no longer valid", e);
            }
            throw e;
        }
    }

    /**
     * Safely attaches screenshot to Allure report
     */
    public void attachScreenshotToAllure(String name) {
        try {
            if (isDriverClosed()) {
                logger.warning("Cannot attach screenshot to Allure: WebDriver is closed");
                return;
            }

            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment(name, "image/png", "png", screenshot);
            logger.info("Screenshot attached to Allure report: " + name);

        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to attach screenshot to Allure report", e);
            // Don't throw exception here as this is supplementary functionality
        }
    }

    public void attachHighlightScreenshotToAllure(By locator, String fileName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            // Store original style
            String originalStyle = element.getAttribute("style");

            // Highlight the element
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='3px solid red'", element);

            // Take screenshot
            attachScreenshotToAllure(fileName);

            // Restore original style
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].setAttribute('style', arguments[1])", element, originalStyle);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to take highlighted element screenshot", e);
            throw new RuntimeException("Highlighted element screenshot capture failed", e);
        }
    }

    public void takeHighlightedElementScreenshot(By locator, String fileName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            // Store original style
            String originalStyle = element.getAttribute("style");

            // Highlight the element
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='3px solid red'", element);

            // Take screenshot
            String filePath = takeFullPageScreenshot(fileName);

            // Restore original style
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].setAttribute('style', arguments[1])", element, originalStyle);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to take highlighted element screenshot", e);
            throw new RuntimeException("Highlighted element screenshot capture failed", e);
        }
    }

    /**
     * Checks if WebDriver is closed or invalid
     */
    private boolean isDriverClosed() {
        try {
            driver.getCurrentUrl();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Generates file path with proper structure
     */
    private String generateFilePath(String fileName) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String fullFileName = timestamp + "-" + fileName + ".png";
        return BASE_SCREENSHOT_DIR + File.separator + testClassName + File.separator + fullFileName;
    }

    /**
     * Creates screenshot directory structure
     */
    private void createScreenshotDirectory() {
        try {
            File directory = new File(BASE_SCREENSHOT_DIR + File.separator + testClassName);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    logger.info("Created screenshot directory: " + directory.getAbsolutePath());
                } else {
                    logger.warning("Failed to create screenshot directory: " + directory.getAbsolutePath());
                }
            }
        } catch (SecurityException e) {
            logger.log(Level.WARNING, "Permission denied creating screenshot directory", e);
        }
    }

    /**
     * Clears all screenshots from the test class directory
     */
    public void clearScreenshotDirectory() {
        try {
            File directory = new File(BASE_SCREENSHOT_DIR + File.separator + testClassName);
            if (directory.exists()) {
                FileUtils.cleanDirectory(directory);
                logger.info("Cleared screenshot directory for: " + testClassName);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to clear screenshot directory", e);
        }
    }

    /**
     * Determines test class name from stack trace
     */
    private static String determineTestClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
            if (className.endsWith("Test") || className.endsWith("Tests")) {
                return className.substring(className.lastIndexOf('.') + 1);
            }
        }
        return "UnknownTest";
    }

    /**
     * Gets the count of screenshots in the test class directory
     *
     * @return Number of screenshot files
     */
    public int getScreenshotCount() {
        File directory = new File(BASE_SCREENSHOT_DIR + File.separator + testClassName);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
            return files != null ? files.length : 0;
        }
        return 0;
    }

    /**
     * Gets the full directory path for screenshots
     *
     * @return Directory path
     */
    public String getScreenshotDirectory() {
        return BASE_SCREENSHOT_DIR + File.separator + testClassName;
    }

}