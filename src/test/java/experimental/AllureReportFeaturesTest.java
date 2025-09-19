package experimental;

import config.TestConfig;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test class demonstrating all Allure reporting features
 * including test statuses, BDD annotations, attachments, and steps
 */
@ExtendWith(AllureJunit5.class)
@Epic("E-Commerce Platform")
@Feature("User Management System")
@Execution(ExecutionMode.CONCURRENT)
@DisplayName("Allure Report Features Test Suite")
public class AllureReportFeaturesTest {

    @BeforeAll
    static void setupClass() {
        Allure.step("Setting up test environment");
        System.out.println("Test suite initialization");
    }

    @BeforeEach
    void setUp() {
        Allure.step("Preparing test data");
    }

    @AfterEach
    void tearDown() {
        Allure.step("Cleaning up test resources");
    }

    // ========== PASSED TESTS ==========

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test user registration with valid credentials")
    @DisplayName("✅ User Registration - Valid Data")
    @Owner("Test Team")
    @Tag("smoke")
    @Tag("regression")
    void testUserRegistrationSuccess() {
        Allure.step("Navigate to registration page");
        Allure.step("Enter valid user credentials", () -> {
            Allure.parameter("username", "testuser@example.com");
            Allure.parameter("password", "SecurePass123!");
        });

        Allure.step("Submit registration form");
        Allure.step("Verify successful registration");

        // Simulate successful test
        assertTrue(true, "User registration completed successfully");

        // Add attachment
        Allure.addAttachment("User Data", "application/json",
                "{\"username\":\"testuser@example.com\",\"status\":\"registered\"}", "json");
    }

    @Test
    @Story("User Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test user login functionality with valid credentials")
    @DisplayName("✅ User Login - Success")
    @Owner("QA Engineer")
    @Tag("smoke")
    void testUserLoginSuccess() {
        performLogin("validuser@test.com", "password123");
        assertEquals("Welcome Dashboard", getCurrentPageTitle());

        // Add screenshot simulation
        Allure.addAttachment("Login Success Screenshot", "image/png",
                new ByteArrayInputStream("fake-screenshot-data".getBytes()), "png");
    }

    // ========== FAILED TESTS ==========

    @Test
    @Story("User Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test login with invalid credentials should fail")
    @DisplayName("❌ User Login - Invalid Credentials")
    @Owner("QA Engineer")
    @Tag("negative")
    void testUserLoginFailure() {
        Allure.step("Attempt login with invalid credentials");

        performLogin("invalid@test.com", "wrongpassword");

        // This will fail intentionally
        String expectedMessage = "Invalid credentials";
        String actualMessage = "Login successful"; // Wrong message to cause failure

        Allure.addAttachment("Login Attempt Log", "text/plain",
                "Login attempt failed as expected with invalid credentials");

        assertEquals(expectedMessage, actualMessage, "Login should fail with invalid credentials");
    }

    @Test
    @Story("Data Validation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test form validation with empty fields")
    @DisplayName("❌ Form Validation - Empty Fields")
    @Tag("validation")
    void testFormValidationFailure() {
        Allure.step("Submit form with empty required fields");

        // Intentional assertion failure
        assertNotNull(null, "Required field should not be empty");
    }

    // ========== BROKEN TESTS ==========

    @Test
    @Story("Payment Processing")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test payment gateway integration")
    @DisplayName("🔧 Payment Processing - Gateway Error")
    @Owner("Payment Team")
    @Tag("integration")
    void testPaymentProcessingBroken() {
        Allure.step("Initialize payment gateway");
        Allure.step("Process payment request");

        // Simulate broken test with exception
        throw new RuntimeException("Payment gateway is currently unavailable");
    }

    @Test
    @Story("External API Integration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test third-party service integration")
    @DisplayName("🔧 External API - Connection Error")
    @Tag("api")
    void testExternalApiIntegrationBroken() {
        Allure.step("Connecting to external API");

        // Simulate network connectivity issue
        throw new IllegalStateException("Unable to establish connection to external service");
    }

    // ========== SKIPPED/UNKNOWN TESTS ==========

    @Test
    @Story("Advanced Features")
    @Severity(SeverityLevel.MINOR)
    @Description("Test advanced user preferences")
    @DisplayName("⏭️ Advanced Preferences - Not Implemented")
    @Disabled("Feature not yet implemented")
    @Tag("future")
    void testAdvancedPreferencesSkipped() {
        // This test will be skipped
        Allure.step("Configure advanced user preferences");
        assertTrue(true);
    }

    @Test
    @Story("Performance Testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Load testing for concurrent users")
    @DisplayName("⏭️ Load Testing - Environment Not Ready")
    @Tag("performance")
    void testLoadTestingConditional() {
        Assumptions.assumeTrue(System.getProperty("test.environment", "").equals("performance"),
                "Performance environment not available");

        // This will be skipped if assumption fails
        Allure.step("Execute load testing scenario");
        assertTrue(true);
    }

    // ========== PARAMETERIZED TESTS ==========

    @ParameterizedTest
    @ValueSource(strings = {"admin@test.com", "user@test.com", "guest@test.com"})
    @Story("User Roles")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test different user role permissions")
    @DisplayName("🔄 User Roles - Permission Check")
    @Tag("roles")
    void testUserRolePermissions(String email) {
        Allure.parameter("userEmail", email);

        Allure.step("Login with user: " + email, () -> {
            performLogin(email, "password123");
        });

        Allure.step("Verify role-based permissions");

        if (email.contains("invalid")) {
            // Some parameterized tests will fail
            fail("Invalid user should not have access");
        }

        assertTrue(email.contains("@"), "Valid email format required");
    }

    // ========== ADDITIONAL EPIC/FEATURE STRUCTURE ==========

    @Test
    @Epic("Mobile Application")
    @Feature("Push Notifications")
    @Story("Notification Delivery")
    @Severity(SeverityLevel.TRIVIAL)
    @Description("Test push notification delivery to mobile devices")
    @DisplayName("📱 Push Notifications - Delivery Success")
    @Owner("Mobile Team")
    @Tag("mobile")
    @Tag("notifications")
    void testPushNotificationDelivery() {
        Allure.step("Configure notification settings");
        Allure.step("Send test notification");
        Allure.step("Verify notification received");

        // Add custom attachment
        Allure.addAttachment("Notification Payload", "application/xml",
                "<notification><title>Test</title><body>Test message</body></notification>", "xml");

        assertTrue(true, "Notification delivered successfully");
    }

    @Test
    @Epic("Security")
    @Feature("Authentication")
    @Story("Session Management")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test session timeout functionality")
    @DisplayName("🔒 Session Timeout - Auto Logout")
    @Owner("Security Team")
    @Tag("security")
    @Tag("session")
    void testSessionTimeout() {
        Allure.step("Login user");
        Allure.step("Wait for session timeout");
        Allure.step("Verify automatic logout");

        // Simulate time-based test
        try {
            TimeUnit.MILLISECONDS.sleep(100); // Simulate waiting
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Test interrupted", e);
        }

        assertTrue(true, "Session timeout worked correctly");
    }

    @Test
    @Epic("Data Management")
    @Feature("Data Export")
    @Story("CSV Export")
    @Severity(SeverityLevel.MINOR)
    @Description("Test CSV data export functionality")
    @DisplayName("📊 Data Export - CSV Format")
    @Owner("Data Team")
    @Tag("export")
    void testCsvDataExport() {
        Allure.step("Select data for export");
        Allure.step("Choose CSV format");
        Allure.step("Generate export file");
        Allure.step("Validate file content");

        // Add CSV content as attachment
        String csvContent = "Name,Email,Role\nJohn Doe,john@test.com,User\nJane Smith,jane@test.com,Admin";
        Allure.addAttachment("Export Data", "text/csv", csvContent, "csv");

        assertTrue(csvContent.contains("Name,Email,Role"), "CSV header validation");
    }

    // ========== HELPER METHODS ==========

    @Step("Perform login with credentials: {username}")
    private void performLogin(String username, String password) {
        Allure.step("Enter username: " + username);
        Allure.step("Enter password");
        Allure.step("Click login button");

        // Simulate login logic
        if (username.contains("invalid")) {
            Allure.step("Login failed - invalid credentials");
        } else {
            Allure.step("Login successful");
        }
    }

    @Step("Get current page title")
    private String getCurrentPageTitle() {
        return "Welcome Dashboard";
    }

    @Step("Navigate to page: {pageName}")
    private void navigateToPage(String pageName) {
        Allure.step("Loading page: " + pageName);
    }

    @AfterAll
    static void reporting(){
        String os = System.getProperty("os.name").toLowerCase();

        if (!os.contains("win")) {
            System.out.println("Skipping Allure report generation on non-Windows OS.");
        } else {
            generateAllureReport();
        }
    }

    static void generateAllureReport() {
        try {
            // Create ProcessBuilder for PowerShell / CMD
            int exitCode = getCode();

            if (exitCode == 0) {
                System.out.println("✅ Allure report generated successfully.");
            } else {
                System.err.println("❌ Failed to generate Allure report. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int getCode() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c",
                TestConfig.GENERATE_ALLURE_HTML_REPORT
        );

        // ✅ Set working directory to your project path
        processBuilder.directory(new File(TestConfig.PROJECT_DIR));

        processBuilder.inheritIO(); // so logs appear in console
        Process process = processBuilder.start();
        return process.waitFor();
    }
}