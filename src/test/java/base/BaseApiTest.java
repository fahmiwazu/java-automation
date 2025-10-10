package base;

import config.TestConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base test class for API tests
 * Sets up RestAssured configuration and common test utilities
 */
public class BaseApiTest {

    protected static RequestSpecification requestSpec;

    @BeforeAll
    public static void setupBase() {
        // Set base URI - using production URL from config
        String baseUrl = System.getProperty("baseUrl", "https://simple-crud-apps.vercel.app");
        RestAssured.baseURI = baseUrl;

        // Create request specification
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();

        // Enable request and response logging
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /**
     * Get the configured request specification
     */
    protected RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpec);
    }

    /**
     * Wait for API to be ready (useful for CI/CD)
     */
    protected void waitForApiReady() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}