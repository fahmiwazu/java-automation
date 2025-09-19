package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

/**
 * Cross-browser utility class for creating WebDriver instances
 * with predefined configurations for different browsers.
 */
public class CrossBrowser {

    /**
     * Creates a WebDriver instance based on the specified browser name
     *
     * @param browserName the name of the browser (chrome, firefox, edge, safari, ie)
     * @return WebDriver instance configured for the specified browser
     */
    public static WebDriver getDriver(String browserName) {
        return switch (browserName.toLowerCase()) {
            case "chrome" -> getChromeDriver();
            case "firefox" -> getFirefoxDriver();
            case "edge" -> getEdgeDriver();
            case "safari" -> {
                // Check if running on macOS before attempting Safari
                String osName = System.getProperty("os.name").toLowerCase();
                if (!osName.contains("mac")) {
                    System.err.println("ERROR: Safari WebDriver only works on macOS. Current OS: " + osName);
                    System.out.println("Falling back to Chrome browser...");
                    yield getChromeDriver();
                }
                yield getSafariDriver();
            }
            case "ie", "internetexplorer" -> getInternetExplorerDriver();
            default -> {
                System.out.println("Browser '" + browserName + "' not supported. Defaulting to Chrome.");
                yield getChromeDriver();
            }
        };
    }

    /**
     * Creates and configures a Chrome WebDriver instance
     *
     * @return configured ChromeDriver instance
     */
    private static WebDriver getChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Uncomment the line below to run in headless mode
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--incognito");
        options.addArguments("--window-size=1920,1080");

        return new ChromeDriver(options);
    }

    /**
     * Creates and configures a Firefox WebDriver instance
     *
     * @return configured FirefoxDriver instance
     */
    private static WebDriver getFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        // Uncomment the line below to run in headless mode
        // options.addArguments("--headless");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("browser.privatebrowsing.autostart", true);

        return new FirefoxDriver(options);
    }

    /**
     * Creates and configures an Edge WebDriver instance
     *
     * @return configured EdgeDriver instance
     */
    private static WebDriver getEdgeDriver() {
        EdgeOptions options = new EdgeOptions();

        // Uncomment the line below to run in headless mode
        // options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--inprivate");

        // Selenium 4.x handles EdgeDriver automatically
        return new EdgeDriver(options);
    }

    /**
     * Creates and configures a Safari WebDriver instance
     * Note: Only works on macOS
     *
     * @return configured SafariDriver instance
     */
    private static WebDriver getSafariDriver() {
        WebDriverManager.safaridriver().setup();
        SafariOptions options = new SafariOptions();

        options.setAutomaticInspection(false);
        options.setAutomaticProfiling(false);

        return new SafariDriver(options);
    }

    /**
     * Creates and configures an Internet Explorer WebDriver instance
     *
     * @return configured InternetExplorerDriver instance
     */
    private static WebDriver getInternetExplorerDriver() {
        WebDriverManager.iedriver().setup();
        InternetExplorerOptions options = new InternetExplorerOptions();

        options.ignoreZoomSettings();
        options.introduceFlakinessByIgnoringSecurityDomains();
        options.requireWindowFocus();
        options.enablePersistentHovering();

        return new InternetExplorerDriver(options);
    }

    /**
     * Creates a headless Chrome WebDriver instance for CI/CD environments
     *
     * @return configured headless ChromeDriver instance
     */
    public static WebDriver getHeadlessChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        return new ChromeDriver(options);
    }

    /**
     * Creates a headless Firefox WebDriver instance for CI/CD environments
     *
     * @return configured headless FirefoxDriver instance
     */
    public static WebDriver getHeadlessFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        options.addArguments("--headless");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        options.addPreference("dom.webnotifications.enabled", false);

        return new FirefoxDriver(options);
    }
}