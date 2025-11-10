package experimental;

import SimpleCRUDApps.model.ProductRequest;
import SimpleCRUDApps.model.ProductResponse;
import base.BaseApiTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;

import static SimpleCRUDApps.api.productProperties.productAPI;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleCrudModularTest extends BaseApiTest {

    @Test
    @Order(1)
    public void getProductTest(){
        ProductResponse[] res =
                given()
                        .contentType(ContentType.JSON)
                .when()
                        .get(productAPI)
                .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(ProductResponse[].class);

        Arrays.stream(res).map(ProductResponse::toString).forEach(System.out::println);
    }

    @Test
    @Order(2)
    public void createProduct(){
        ProductRequest request = new ProductRequest();

        request.setName("Ketoprak");
        request.setQuantity(2);
        request.setPrice(10000);

        ProductResponse res =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                .when()
                        .post(productAPI)
                .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(ProductResponse.class);

        productId = res.getId();
    }

    @Test
    @Order(3)
    public void getProductById() {
        ProductResponse res =
                given()
                        .contentType(ContentType.JSON)
                .when()
                        .get(productAPI + productId)
                .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(ProductResponse.class);

        System.out.println(res);
    }

    @Test
    @Order(4)
    public void updateProductByID(){
        ProductRequest request = new ProductRequest();
        request.setPrice(18000);

        ProductResponse res =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                .when()
                        .put(productAPI + productId)
                .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(ProductResponse.class);

        System.out.println(res);
    }

    @Test
    @Order(5)
    public void deleteProductByID(){
        ProductResponse res =
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(productAPI + productId)
        .then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(ProductResponse.class);

        System.out.println(res.getMessage());

    }

}