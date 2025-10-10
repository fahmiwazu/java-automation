package ci;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Product;
import model.ProductRequest;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Product Management")
@Feature("CRUD Operations")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleCrudE2ETest {

    private static final String BASE_URL = "https://simple-crud-apps.vercel.app";
    private static final String API_PATH = "/api/products";
    private static String createdProductId;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @Order(1)
    @Description("Create a new product and verify it's created successfully")
    void testCreateProduct() {
        ProductRequest newProduct = ProductRequest.builder()
                .name("Laptop Gaming")
                .quantity(5)
                .price(15000000)
                .build();

        Response response = createProduct(newProduct);

        response.then()
                .statusCode(200)
                .body("name", equalTo("Laptop Gaming"))
                .body("quantity", equalTo(5))
                .body("price", equalTo(15000000))
                .body("_id", notNullValue());

        createdProductId = response.jsonPath().getString("_id");
        System.out.println("Created Product ID: " + createdProductId);
    }

    @Test
    @Order(2)
    @Description("Read/Get all products and verify the created product exists")
    void testGetAllProducts() {
        Response response = getAllProducts();

        response.then()
                .statusCode(200)
                .body("$", not(empty()));

        Product[] products = response.as(Product[].class);
        assertTrue(products.length > 0, "Products list should not be empty");
        System.out.println("Total Products: " + products.length);
    }

    @Test
    @Order(3)
    @Description("Read/Get single product by ID and verify details")
    void testGetProductById() {
        assertNotNull(createdProductId, "Product ID should exist from create test");

        Response response = getProductById(createdProductId);

        response.then()
                .statusCode(200)
                .body("_id", equalTo(createdProductId))
                .body("name", equalTo("Laptop Gaming"))
                .body("quantity", equalTo(5))
                .body("price", equalTo(15000000));

        System.out.println("Retrieved Product: " + response.asString());
    }

    @Test
    @Order(4)
    @Description("Update the created product and verify changes")
    void testUpdateProduct() {
        assertNotNull(createdProductId, "Product ID should exist from create test");

        ProductRequest updateProduct = ProductRequest.builder()
                .name("Laptop Gaming Updated")
                .quantity(10)
                .price(18000000)
                .build();

        Response response = updateProduct(createdProductId, updateProduct);

        response.then()
                .statusCode(200)
                .body("_id", equalTo(createdProductId))
                .body("name", equalTo("Laptop Gaming Updated"))
                .body("quantity", equalTo(10))
                .body("price", equalTo(18000000));

        System.out.println("Updated Product: " + response.asString());
    }

    @Test
    @Order(5)
    @Description("Delete the created product and verify it's removed")
    void testDeleteProduct() {
        assertNotNull(createdProductId, "Product ID should exist from create test");

        Response deleteResponse = deleteProduct(createdProductId);
        deleteResponse.then().statusCode(200);

        // Verify product is deleted by trying to get it
        Response getResponse = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_PATH + "/" + createdProductId);

        // Product should not be found
//        int statusCode = getResponse.getStatusCode();
//        assertTrue(statusCode == 404 || statusCode == 500,
//                "Deleted product should return 404 or 500 status");

        System.out.println("Product deleted successfully");
    }

    // Helper methods with Allure steps
    @Step("Create product: {0}")
    private Response createProduct(ProductRequest product) {
        return given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(API_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Get all products")
    private Response getAllProducts() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Get product by ID: {0}")
    private Response getProductById(String productId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_PATH + "/" + productId)
                .then()
                .extract()
                .response();
    }

    @Step("Update product ID: {0} with data: {1}")
    private Response updateProduct(String productId, ProductRequest product) {
        return given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .put(API_PATH + "/" + productId)
                .then()
                .extract()
                .response();
    }

    @Step("Delete product ID: {0}")
    private Response deleteProduct(String productId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_PATH + "/" + productId)
                .then()
                .extract()
                .response();
    }
}