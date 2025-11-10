package ci;

import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import SimpleCRUDApps.api.Service;
import SimpleCRUDApps.model.ProductRequest;
import base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@Epic("CI Product Management")
@Feature("CSV-Driven CRUD")
@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleCrudCoreApiTest extends BaseApiTest {

    private final ThreadLocal<Service> apiService = ThreadLocal.withInitial(Service::new);
    private final ThreadLocal<String> createdProductId = new ThreadLocal<>();


    @BeforeAll
    void init() {
        BaseApiTest.setupBase();

        AllureRestAssured allureFilter = new AllureRestAssured();
        // Configure what to include/exclude
        allureFilter.setRequestAttachmentName("Request Detail");
        allureFilter.setResponseAttachmentName("Response Detail");

        RestAssured.filters(allureFilter);
    }

    @AfterEach
    void smallDelay() {
        waitForApiReady();
    }

    void allureDescription(){
        Allure.description("""
        # 🚀 CI Product Management — CSV‑Driven API Test

        ## 📋 Overview
        End‑to‑end API validation of Product CRUD using RestAssured with parallel execution.
        Each test iteration is sourced from a CSV row and isolated via ThreadLocal state.

        ## 🗂️ Data Source
        - File: `test-data/list_of_product.csv`
        - Columns: `Product Name`, `Product Price`, `Product Quantity`, `Updated Product Name`, `Updated Product Price`, `Updated Product Quantity`
        - Empty values in the update columns are treated as partial updates (field unchanged).

        ## 🔄 Flow
        1) Create product from CSV (name, price, quantity)
        2) Get product by ID and assert created values
        3) Optionally update fields if CSV provides non‑empty values
        4) Delete product
        5) Verify deleted product lookup returns "Product not found"

        ## ⚙️ Parallelism
        - Class: `@Execution(CONCURRENT)`
        - Isolation: `ThreadLocal<Service>` and per‑thread `createdProductId`

        ## 📑 Evidence
        - Allure attachments for request/response via `AllureRestAssured` filter
        - Named steps for Create / Read / Update / Delete / Verify Deleted
        """
        );
    }

    @ParameterizedTest(name = "CSV row -> create/update product: {0}")
    @CsvFileSource(resources = "/test-data/list_of_product.csv", numLinesToSkip = 1)
    @Description("Create and optionally update products using CSV-driven parameters")
    void testCreateUpdateDeleteFromCsv(
            String productName,
            Integer productPrice,
            Integer productQuantity,
            String updatedName,
            String updatedPrice,
            String updatedQuantity
    ) {

        allureDescription();

        ProductRequest createReq = ProductRequest.builder()
                .name(productName)
                .price(productPrice)
                .quantity(productQuantity)
                .build();

        Allure.step("Create a Product",()->{
            Response createResp = apiService.get().createProduct(createReq);
            createResp.then()
                    .statusCode(200)
                    .body("_id", org.hamcrest.Matchers.notNullValue())
                    .body("name", org.hamcrest.Matchers.equalTo(productName));
            createdProductId.set(createResp.jsonPath().getString("_id"));
        });

        Allure.step("Get Product by ID",()->{
            Response getResp = apiService.get().getProductById(createdProductId.get());
            getResp.then()
                    .statusCode(200)
                    .body("_id",org.hamcrest.Matchers.equalTo(createdProductId.get()));
        });

        Allure.step("Update Product by ID",()->{
            String updName = emptyToNull(clean(updatedName));
            Integer updPrice = parseInteger(emptyToNull(clean(updatedPrice)));
            Integer updQty = parseInteger(emptyToNull(clean(updatedQuantity)));

            if (updName != null || updPrice != null || updQty != null) {
                ProductRequest updateReq = ProductRequest.builder()
                        .name(updName != null ? updName : productName)
                        .price(updPrice != null ? updPrice : productPrice)
                        .quantity(updQty != null ? updQty : productQuantity)
                        .build();

                Response updateResp = apiService.get().updateProduct(createdProductId.get(), updateReq);
                updateResp.then()
                        .statusCode(200)
                        .body("_id", org.hamcrest.Matchers.equalTo(createdProductId.get()))
                        .body("name", org.hamcrest.Matchers.equalTo(updateReq.getName()))
                        .body("price", org.hamcrest.Matchers.equalTo(updateReq.getPrice()))
                        .body("quantity", org.hamcrest.Matchers.equalTo(updateReq.getQuantity()));
            }
        });

        // Cleanup
        Allure.step("Delete Product by ID",()->{
            Response deleteResp = apiService.get().deleteProduct(createdProductId.get());
            deleteResp.then().statusCode(200);
            createdProductId.remove();
        });

        Allure.step("Check Deleted Product by ID",()->{
            apiService.get().getProductById(createdProductId.get())
                    .then().statusCode(200)
                    .body("message",org.hamcrest.Matchers.equalTo("Product not found"));
        });
    }

    // helper method for parametrized test
    private static String clean(String raw) {
        if (raw == null) return null;
        String trimmed = raw.trim();
        if (trimmed.startsWith("\"")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed.trim().replace("\"", "").replace("'", "");
    }

    private static String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static Integer parseInteger(String s) {
        if (s == null) return null;
        String t = s.replaceAll("[^0-9-]", "");
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}


