package ci;

import base.ParallelBaseTests;
import config.TestConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import pages.SimpleCRUDPage;
import utils.ScreenshotHandler;
import utils.ValidationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Execution(ExecutionMode.CONCURRENT) // Enable parallel execution at class level
@Epic("Simple CRUD Apps")
//@Feature("CRUD Operation")
public class ParallelSimpleCrudTest extends ParallelBaseTests {
    private String baseUrl;

    @BeforeEach
    public void testSetup() {

        baseUrl = TestConfig.BASE_URL_SIMPLE_CRUD_PROD;
        // baseUrl = TestConfig.BASE_URL_SIMPLE_CRUD_DEV;

        getValidator().clearValidationErrors();
    }

    @ParameterizedTest(name="Product : {0}")
    @DisplayName("Product Management - Simple CRUD")
    @Feature("Desktop View")
    @Story("CRUD Regression")
    @Tag("Integration_Test")
    @Tag("E2E")
    @Tag("Regression_Test")
    @Owner("Fahmi Wiradika")
    @Link(name="Repository", url="https://github.com/fahmiwazu/simple-crud-apps")
    @Link(name="Live Apps", url="https://simple-crud-apps.vercel.app/")
    @CsvFileSource(files = {"src/test/resources/test-data/list_of_product.csv"}, numLinesToSkip = 1)
    @Execution(ExecutionMode.CONCURRENT) // Enable parallel execution at method level
    public void testSimpleCrudDesktop(String productName,
                               String productPrice,
                               String productQuantity,
                               String updatedName,
                               String updatedPrice,
                               String updatedQuantity) {

        getDriver().manage().window().setSize(new Dimension(1920, 1080));

        allureDescriptionDesktop();

        SimpleCRUDPage page = getSimpleCRUDPage();
        ScreenshotHandler screenshot = getScreenshot();
        ValidationUtils validator = getValidator();

        Allure.step("Navigate into apps", () -> {
            getDriver().get(baseUrl);
            page.waitLoadProduct();
            screenshot.attachScreenshotToAllure("Apps Main Page");
        });

        Allure.step("Add new Product", () -> {
            page.setProductName(productName);
            page.setProductPrice(productPrice);
            page.setProductQuantity(productQuantity);
            page.clickAddProduct();
            screenshot.attachScreenshotToAllure("New Product Added");
        });

        String product = page.extractProductIdFromNotification();
        page.waitForNotificationToDisappear();

        Allure.step("New Product Assertion", () -> {
            screenshot.attachHighlightScreenshotToAllure(
                    By.xpath(page.productItemXpathLocator(product)),
                    "Evidence"
            );

            validator.validationCheck("Validate Product Name",
                    productName,
                    page.extractProductNameById(product));
            validator.validationCheck("Validate Product Price",
                    productPrice,
                    page.extractProductPriceById(product));
            validator.validationCheck("Validate Quantity",
                    productQuantity,
                    page.extractProductQuantityById(product));
        });

        Allure.step("Update Product", () -> {
            page.clickUpdateButtonByProductId(product);
            page.setUpdateName(updatedName);
            page.setUpdatePrice(updatedPrice);
            page.setUpdateQuantity(updatedQuantity);
            page.clickConfirmUpdate();
//            screenshot.attachScreenshotToAllure("Updated Product");
        });

        String updatedProduct = page.extractProductIdFromNotification();
        page.waitForNotificationToDisappear();

        Allure.step("Update Product Assertion", () -> {
            screenshot.attachHighlightScreenshotToAllure(
                    By.xpath(page.productItemXpathLocator(updatedProduct)),
                    "Evidence"
            );

            if (!Objects.equals(updatedName, "")) {
                validator.validationCheck("Validate Product Name",
                        updatedName,
                        page.extractProductNameById(product));
            }
            if (!Objects.equals(updatedPrice, "")) {
                validator.validationCheck("Validate Product Price",
                        updatedPrice,
                        page.extractProductPriceById(product));
            }
            if (!Objects.equals(updatedQuantity, "")) {
                validator.validationCheck("Validate Quantity",
                        updatedQuantity,
                        page.extractProductQuantityById(product));
            }
        });

        Allure.step("Deleting Product", () -> {
            page.clickDeleteButtonByProductId(updatedProduct);
            page.clickConfirmDelete();
            screenshot.attachScreenshotToAllure("Deleted Product");
        });

        String deletedProduct = page.extractProductIdFromNotification();
        page.waitForNotificationToDisappear();

        Allure.step("Product ID Notification Assertion", () -> {
            validator.assertEquals(product, updatedProduct, deletedProduct);
            validator.assertTrue("Verify Deleted Product",
                    page.isProductDeleted(deletedProduct));
        });
    }


    @ParameterizedTest(name="Product : {0}")
    @DisplayName("Product Management - Simple CRUD")
    @Feature("Mobile View")
    @Story("CRUD Regression")
    @Tag("Integration_Test")
    @Tag("E2E")
    @Tag("Regression_Test")
    @Owner("Fahmi Wiradika")
    @Link(name="Repository", url="https://github.com/fahmiwazu/simple-crud-apps")
    @Link(name="Live Apps", url="https://simple-crud-apps.vercel.app/")
    @CsvFileSource(files = {"src/test/resources/test-data/list_of_product.csv"}, numLinesToSkip = 1)
    @Execution(ExecutionMode.CONCURRENT) // Enable parallel execution at method level
    public void testSimpleCrudMobile(String productName,
                               String productPrice,
                               String productQuantity,
                               String updatedName,
                               String updatedPrice,
                               String updatedQuantity) {

        getDriver().manage().window().setSize(new Dimension(428, 926));

        allureDescriptionMobile();

        SimpleCRUDPage page = getSimpleCRUDPage();
        ScreenshotHandler screenshot = getScreenshot();
        ValidationUtils validator = getValidator();

        Allure.step("Navigate into apps", () -> {
            getDriver().get(baseUrl);
            page.waitLoadProduct();
            screenshot.attachScreenshotToAllure("Apps Main Page");
        });

        Allure.step("Add new Product", () -> {
            page.setProductName(productName);
            page.setProductPrice(productPrice);
            page.setProductQuantity(productQuantity);
            page.clickAddProduct();
            screenshot.attachScreenshotToAllure("New Product Added");
        });

        String product = page.extractProductIdFromNotification();
        page.waitForNotificationToDisappear();

        Allure.step("New Product Assertion", () -> {
            screenshot.attachHighlightScreenshotToAllure(
                    By.xpath(page.productItemXpathLocator(product)),
                    "Evidence"
            );

            validator.validationCheck("Validate Product Name",
                    productName,
                    page.extractProductNameById(product));
            validator.validationCheck("Validate Product Price",
                    productPrice,
                    page.extractProductPriceById(product));
            validator.validationCheck("Validate Quantity",
                    productQuantity,
                    page.extractProductQuantityById(product));
        });

        Allure.step("Update Product", () -> {
            page.clickUpdateButtonByProductId(product);
            page.setUpdateName(updatedName);
            page.setUpdatePrice(updatedPrice);
            page.setUpdateQuantity(updatedQuantity);
            page.clickConfirmUpdate();
//            screenshot.attachScreenshotToAllure("Updated Product");
        });

        String updatedProduct = page.extractProductIdFromNotification();
        page.waitForNotificationToDisappear();

        Allure.step("Update Product Assertion", () -> {
            screenshot.attachHighlightScreenshotToAllure(
                    By.xpath(page.productItemXpathLocator(updatedProduct)),
                    "Evidence"
            );

            if (!Objects.equals(updatedName, "")) {
                validator.validationCheck("Validate Product Name",
                        updatedName,
                        page.extractProductNameById(product));
            }
            if (!Objects.equals(updatedPrice, "")) {
                validator.validationCheck("Validate Product Price",
                        updatedPrice,
                        page.extractProductPriceById(product));
            }
            if (!Objects.equals(updatedQuantity, "")) {
                validator.validationCheck("Validate Quantity",
                        updatedQuantity,
                        page.extractProductQuantityById(product));
            }
        });

        Allure.step("Deleting Product", () -> {
            page.clickDeleteButtonByProductId(updatedProduct);
            page.clickConfirmDelete();
            screenshot.attachScreenshotToAllure("Deleted Product");
        });

        String deletedProduct = page.extractProductIdFromNotification();
        page.waitForNotificationToDisappear();

        Allure.step("Product ID Notification Assertion", () -> {
            validator.assertEquals(product, updatedProduct, deletedProduct);
            validator.assertTrue("Verify Deleted Product",
                    page.isProductDeleted(deletedProduct));
        });
    }

    private void allureDescriptionDesktop(){
        Allure.description("""
        # 🚀 Parallel Simple CRUD Test — Desktop View

        ## 📋 Overview
        Validates end-to-end CRUD on desktop viewport with parallel execution support.

        ## 🌍 Environment
        - Live App URL: [https://simple-crud-apps.vercel.app/](https://simple-crud-apps.vercel.app/)
        - Resolution: 1920 x 1080
        - Parallel: Class and method level

        ## 🔄 Flow
        1) Initialize browser at 1920x1080
        2) Navigate to app and wait for products
        3) Add → Assert → Update → Assert → Delete → Assert
        4) Attach screenshots and highlights to Allure

        ## 📊 Evidence
        - Desktop screenshots and validation logs in Allure
        
        """
        );
    }

    private void allureDescriptionMobile(){
        Allure.description("""
        # 🚀 Parallel Simple CRUD Test — Mobile View

        ## 📋 Overview
        Validates end-to-end CRUD on a mobile-like viewport with parallel execution support.

        ## 🌍 Environment
        - Live App URL: [https://simple-crud-apps.vercel.app/](https://simple-crud-apps.vercel.app/)
        - Resolution: 428 x 926
        - Parallel: Class and method level

        ## 🔄 Flow
        1) Initialize browser at 428x926
        2) Navigate to app and wait for products
        3) Add → Assert → Update → Assert → Delete → Assert
        4) Attach screenshots and highlights to Allure

        ## 📊 Evidence
        - Mobile screenshots and validation logs in Allure
        """
        );
    }

    @Step("Check Validation Result")
    @AfterEach
    public void summarizeResult() {
        getValidator().printValidationSummary();
    }

    @AfterAll
    public static void reporting() {
        String os = System.getProperty("os.name").toLowerCase();

        if (!os.contains("win")) {
            System.out.println("Skipping Allure report generation on non-Windows OS.");
        } else {
            generateAllureReport();
        }
    }

    static void generateAllureReport() {
        try {
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

        processBuilder.directory(new File(TestConfig.PROJECT_DIR));
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        return process.waitFor();
    }
}