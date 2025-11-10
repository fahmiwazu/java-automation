package SimpleCRUDApps.api;

import SimpleCRUDApps.model.ProductRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Service {

    private static final String API_PATH = "/api/products";

    public Response createProduct(ProductRequest product) {
        return given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(API_PATH)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getAllProducts() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_PATH)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getProductById(String productId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_PATH + "/" + productId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response updateProduct(String productId, ProductRequest product) {
        return given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .put(API_PATH + "/" + productId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response deleteProduct(String productId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_PATH + "/" + productId)
                .then()
                .log().all()
                .extract()
                .response();
    }
}


