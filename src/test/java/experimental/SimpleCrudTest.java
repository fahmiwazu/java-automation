package experimental;


import base.BaseTests;
import config.TestConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.*;
import utils.ScreenshotHandler;
import utils.ValidationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleCrudTest extends BaseTests {
    private String baseUrl;

    @BeforeAll
    public void setUp() {
        driver.manage().window().setSize(new Dimension(1920, 1080));
//        driver.manage().window().maximize();
        screenshot = new ScreenshotHandler(driver, this.getClass().getSimpleName());
        validator = new ValidationUtils(driver, this.getClass().getSimpleName());
        // baseUrl = TestConfig.BASE_URL_SIMPLE_CRUD_DEV;
        baseUrl = TestConfig.BASE_URL_SIMPLE_CRUD_PROD;
    }

    @BeforeEach
    public void variableSetup(){
        validator.clearValidationErrors();
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src/test/resources/test-data/single_product.csv"}, numLinesToSkip = 1)
    public void testSimpleCrud(String productName,
                               String productPrice,
                               String productQuantity,
                               String updatedName,
                               String updatedPrice,
                               String updatedQuantity) {

        Allure.step("Navigate into apps",()-> {
            driver.get(baseUrl);
            simpleCRUDPage.waitLoadProduct();
            screenshot.attachScreenshotToAllure("Apps Main Page");
        });

        Allure.step("Add new Product",()->{
            simpleCRUDPage.setProductName(productName);
            simpleCRUDPage.setProductPrice(productPrice);
            simpleCRUDPage.setProductQuantity(productQuantity);
            simpleCRUDPage.clickAddProduct();
//            screenshot.attachScreenshotToAllure("New Product Added");
        });

        String product = simpleCRUDPage.extractProductIdFromNotification();
        simpleCRUDPage.waitForNotificationToDisappear();

        Allure.step("New Product Assertion", ()->{
            screenshot.attachHighlightScreenshotToAllure(
                    By.xpath(simpleCRUDPage.productItemXpathLocator(product)),
                    "Evidence");

            validator.validationCheck("Validate Product Name",
                    productName,
                    simpleCRUDPage.extractProductNameById(product));
            validator.validationCheck("Validate Product Price",
                    productPrice,
                    simpleCRUDPage.extractProductPriceById(product));
            validator.validationCheck("Validate Quantity",
                    productQuantity,
                    simpleCRUDPage.extractProductQuantityById(product));
        });

        Allure.step("Update Product",()->{
            simpleCRUDPage.clickUpdateButtonByProductId(product);
            simpleCRUDPage.setUpdateName(updatedName);
            simpleCRUDPage.setUpdatePrice(updatedPrice);
            simpleCRUDPage.setUpdateQuantity(updatedQuantity);
            simpleCRUDPage.clickConfirmUpdate();
            // screenshot.attachScreenshotToAllure("Updated Product");
        });

        String updatedProduct = simpleCRUDPage.extractProductIdFromNotification();
        simpleCRUDPage.waitForNotificationToDisappear();

        Allure.step("Update Product Assertion", ()->{
            screenshot.attachHighlightScreenshotToAllure(
                    By.xpath(simpleCRUDPage.productItemXpathLocator(updatedProduct)),
                    "Evidence"
            );

            if(!Objects.equals(updatedName, "")){
                validator.validationCheck("Validate Product Name",
                        updatedName,
                        simpleCRUDPage.extractProductNameById(product));
            }
            if(!Objects.equals(updatedPrice,"")){
                validator.validationCheck("Validate Product Price",
                        updatedPrice,
                        simpleCRUDPage.extractProductPriceById(product));
            }
            if(!Objects.equals(updatedQuantity,"")){
                validator.validationCheck("Validate Quantity",
                        updatedQuantity,
                        simpleCRUDPage.extractProductQuantityById(product));
            }
        });


        Allure.step("Deleting Product",()->{
            simpleCRUDPage.clickDeleteButtonByProductId(updatedProduct);
            simpleCRUDPage.clickConfirmDelete();
        });

        String deletedProduct = simpleCRUDPage.extractProductIdFromNotification();

        Allure.step("Product ID Notification Assertion",()->{
            simpleCRUDPage.waitForNotificationToDisappear();
            screenshot.attachScreenshotToAllure("Deleted Product");
            validator.assertTrue("Verify Deleted Product",
                    simpleCRUDPage.isProductDeleted(deletedProduct));
        });

    }

    @Step("Check Validation Result")
    @AfterEach
    public void summarizeResult(){
        validator.printValidationSummary();
    }

    @AfterAll
    public void reporting(){
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