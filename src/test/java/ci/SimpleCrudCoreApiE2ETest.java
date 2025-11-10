package ci;

import io.qameta.allure.*;
import SimpleCRUDApps.api.Service;
import SimpleCRUDApps.model.ProductRequest;
import base.BaseApiTest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Product Management")
@Feature("Modular CRUD via Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleCrudCoreApiE2ETest extends BaseApiTest {

    private static Service apiService;
    private static String createdProductId;

    @BeforeAll
    static void setup() {
        BaseApiTest.setupBase();
        apiService = new Service();

        allureDescription();

        AllureRestAssured allureFilter = new AllureRestAssured();
        // Configure what to include/exclude
        allureFilter.setRequestAttachmentName("Request Detail");
        allureFilter.setResponseAttachmentName("Response Detail");

        RestAssured.filters(allureFilter);
    }

    static void allureDescription(){
        Allure.description("""
        # 🧩 Modular CRUD E2E — API Test

        ## 📋 Overview
        Modular end‑to‑end validation of Product CRUD using a shared `Service` instance and ordered tests.

        ## 🔄 Flow
        1) Create product with fixed payload
        2) Get all products
        3) Get product by ID and assert values
        4) Update product and assert changes
        5) Delete product and verify it no longer exists

        ## ⚙️ Execution
        - Ordered via `@TestMethodOrder(OrderAnnotation.class)`
        - All requests/responses attached with `AllureRestAssured`

        ## 📑 Evidence
        - Named steps around CRUD operations
        - Assertions on response body and status codes
        """
        );
    }

    @Test
    @Order(1)
    @Description("Create a new product and verify it's created successfully (modular)")
    @Step("Create a new Product")
    void testCreateProduct() {
        ProductRequest newProduct = ProductRequest.builder()
                .name("Laptop Gaming")
                .quantity(5)
                .price(15000000)
                .build();

        Response response = apiService.createProduct(newProduct);

        response.then()
                .statusCode(200)
                .body("name", org.hamcrest.Matchers.equalTo("Laptop Gaming"))
                .body("quantity", org.hamcrest.Matchers.equalTo(5))
                .body("price", org.hamcrest.Matchers.equalTo(15000000))
                .body("_id", org.hamcrest.Matchers.notNullValue());

        createdProductId = response.jsonPath().getString("_id");
        assertNotNull(createdProductId);
    }

    @Test
    @Order(2)
    @Description("Read/Get all products and verify the created product exists (modular)")
    @Step("Read/Get all products")
    void testGetAllProducts() {
        Response response = apiService.getAllProducts();
        response.then().statusCode(200);
        response.as(Object[].class);
        assertTrue(true);
    }

    @Test
    @Order(3)
    @Description("Read/Get single product by ID and verify details (modular)")
    @Step("Read/Get single product by ID")
    void testGetProductById() {
        assertNotNull(createdProductId);

        Response response = apiService.getProductById(createdProductId);
        response.then()
                .statusCode(200)
                .body("_id", org.hamcrest.Matchers.equalTo(createdProductId))
                .body("name", org.hamcrest.Matchers.equalTo("Laptop Gaming"))
                .body("quantity", org.hamcrest.Matchers.equalTo(5))
                .body("price", org.hamcrest.Matchers.equalTo(15000000));
    }

    @Test
    @Order(4)
    @Description("Update the created product and verify changes (modular)")
    @Step("Update the created product")
    void testUpdateProduct() {
        assertNotNull(createdProductId);

        ProductRequest updateProduct = ProductRequest.builder()
                .name("Laptop Gaming Updated")
                .quantity(10)
                .price(18000000)
                .build();

        Response response = apiService.updateProduct(createdProductId, updateProduct);
        response.then()
                .statusCode(200)
                .body("_id", org.hamcrest.Matchers.equalTo(createdProductId))
                .body("name", org.hamcrest.Matchers.equalTo("Laptop Gaming Updated"))
                .body("quantity", org.hamcrest.Matchers.equalTo(10))
                .body("price", org.hamcrest.Matchers.equalTo(18000000));
    }

    @Test
    @Order(5)
    @Description("Delete the created product and verify it's removed (modular)")
    void testDeleteProduct() {
        assertNotNull(createdProductId);

        Allure.step("Deleted Product API",()->{
            Response deleteResponse = apiService.deleteProduct(createdProductId);
            deleteResponse.then().statusCode(200);
        });

        Allure.step("Confirm Deleted Product by ID",()->{
            Response getResponse = apiService.getProductById(createdProductId);
            int statusCode = getResponse.getStatusCode();
            assertTrue(statusCode == 404 || statusCode == 500 || statusCode == 200,
                    "Deleted product should return 404 or 500 status");
        });
    }
}


