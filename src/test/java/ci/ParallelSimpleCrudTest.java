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
@Feature("CRUD Operation")
public class ParallelSimpleCrudTest extends ParallelBaseTests {
    private String baseUrl;

    @BeforeEach
    public void testSetup() {
        getDriver().manage().window().setSize(new Dimension(1920, 1080));
        baseUrl = "https://simple-crud-apps.vercel.app/";
        // baseUrl = "http://localhost:3000/";

        getValidator().clearValidationErrors();
    }

    @ParameterizedTest(name="Product : {0}")
    @DisplayName("Product Management - Simple CRUD")
    @Story("CRUD Regression")
    @Tag("Integration_Test")
    @Tag("E2E")
    @Tag("Regression_Test")
    @Owner("Fahmi Wiradika")
    @Link(name="Repository", url="https://github.com/fahmiwazu/simple-crud-apps")
    @Link(name="Live Apps", url="https://simple-crud-apps.vercel.app/")
    @CsvFileSource(files = {"src/test/resources/test-data/list_of_product.csv"}, numLinesToSkip = 1)
    @Execution(ExecutionMode.CONCURRENT) // Enable parallel execution at method level
    public void testSimpleCrud(String productName,
                               String productPrice,
                               String productQuantity,
                               String updatedName,
                               String updatedPrice,
                               String updatedQuantity) {

        allureDescription();

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

    private void allureDescription(){
        Allure.description("""
        # 🚀 Parallel Simple CRUD Test
        
        ## 📋 Overview
        This test class validates **end-to-end CRUD (Create, Read, Update, Delete)** functionality of the [Simple CRUD App](https://simple-crud-apps.vercel.app/) with parallel execution support! ⚡ \s
        It uses **JUnit 5**, **Selenium WebDriver**, and **Allure Reporting** to ensure reliability, reproducibility, and comprehensive test evidence. 📊
        
        ## 🌍 Environment
        - **Live App URL:** [https://simple-crud-apps.vercel.app/](https://simple-crud-apps.vercel.app/) 🌐 \s
        - **Local Dev URL (optional):** `http://localhost:3000/` 💻 \s
        - **Browser Resolution:** `1920 x 1080` 🖥️ \s
        
        ## ✨ Key Features
        - **⚡ Parallel Execution** \s
          - Class and method level parallelization enabled with `@Execution(ExecutionMode.CONCURRENT)`. 🏃‍♂️💨 \s
          - Multiple CSV test data rows executed in parallel for lightning-fast results! ⚡ \s

        - **📊 Data-Driven Testing** \s
          - Test inputs are sourced from `list_of_product.csv`. 📁 \s
          - Each row represents a product scenario (name, price, quantity, updates). 🛒 \s

        - **📸 Step-Level Reporting with Allure** \s
          - Screenshots and element highlights attached at every major step. 📷✨ \s
          - Validation checks logged with clear pass/fail evidence. ✅❌ \s
          - Custom Allure steps (`@Step`, `Allure.step`) provide a narrative flow. 📖 \s

        ## 🔄 Test Flow
        1. **🛠️ Setup** \s
           - Browser initialized with 1920x1080 resolution. 🖥️ \s
           - Base URL set (local or deployed app). 🌐 \s
           - Validation errors cleared before each test. 🧹 \s

        2. **🎯 Test Steps**
           - **🚪 Navigate to App** \s
             - Open CRUD app and wait for product list. 📱 \s
             - Capture initial screenshot. 📸 \s

           - **➕ Add Product** \s
             - Enter product details (name, price, quantity). ✏️ \s
             - Submit form and verify creation notification. 🔔 \s
             - Assert product details on UI. ✅ \s

           - **✏️ Update Product** \s
             - Trigger update form for created product. 🔄 \s
             - Modify fields (name, price, quantity). 📝 \s
             - Validate updated product fields. ✔️ \s

           - **🗑️ Delete Product** \s
             - Delete updated product. 🚮 \s
             - Verify deletion notification and absence of product in list. 👻 \s

        3. **🏁 Post-Test**
           - **📊 Validation Summary**: Aggregates and prints all validation results (`@AfterEach`). 📈 \s
           - **📑 Allure Report Generation**: Automatically triggers report build on Windows (`@AfterAll`). 🎉 \s

        ## 🔍 Evidence
        - **📸 Screenshots**: \s
          - Page states (before/after CRUD). 🖼️ \s
          - Highlighted product elements for assertion. 🎯 \s

        - **📝 Validation Logs**: \s
          - Field-by-field comparisons between expected and actual values. 🔬 \s

        ## 🎁 Benefits
        - Ensures reliability of **CRUD operations** in the application. 🛡️ \s
        - Provides **parallel speedup** for large datasets. 🚄 \s
        - Generates **comprehensive Allure reports** with visual and textual evidence. 📊✨ \s

        ---

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