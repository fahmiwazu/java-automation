package experimental;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestAssuredAPITestCourseTest {

    private static RequestSpecification requestSpecification;

    private static ResponseSpecification responseSpecification;

    @BeforeAll
    public void createBaseURL(){

        requestSpecification = new RequestSpecBuilder().
                setBaseUri("https://api.zippopotam.us").
                build();

        responseSpecification = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();
    }

    @Test
    public void requestUsZipCode(){
        given().
                log().all().
                spec(requestSpecification).
        when().
                get("/US/00210").
        then().
                log().body().
                spec(responseSpecification).
        and().
                assertThat().
                body("places[0].'place name'"
                        , equalTo("Portsmouth"))
        ;

    }

    @Test
    public void requestExtractionUsZipCode(){

        String placeName =

        given().
                log().all().
                spec(requestSpecification).
        when().
                get("/US/00210").
        then().
                log().body().
                spec(responseSpecification).
        and().
                extract().
                path("places[0].'place name'")
        ;

        Assertions.assertEquals("Portsmouth", placeName);

    }

    @Test
    public void requestJpZipCode(){
        given().
                log().all().
                spec(requestSpecification).
        when().
                log().all().
                get("/JP/100-0001").
        then().
                log().all().
                log().body().
                assertThat().
                statusCode(200).

                body("places[0].'place name'"
                        , equalTo("Chiyoda")).
                body("places[0].state"
                        , equalTo("Toukyouto")).
                body("'country abbreviation'"
                        , equalTo("JP")).
                body("'post code'", equalTo("100-0001")).
                body("places.'place name'"
                        , hasItem("Chiyoda")).
                body("'post code'", not(hasItem(1001))).
                body("places.'place name'"
                        , hasSize(1))
        ;

    }

    @ParameterizedTest(name="Country: {0} Postcode: {1}")
    @MethodSource("zipCodesAndPlaces")
    void requestZipCode(String country, String zipCode, String expectedPlace) {
        // Your test logic here
        given().
                pathParam("countryCode", country).
                pathParam("zipCode", zipCode).
                log().all().
        when().
                get("https://api.zippopotam.us/{countryCode}/{zipCode}").
        then().
                log().body().
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON).
                contentType("application/json").
                body("places[0].'place name'"
                        , equalTo(expectedPlace))
        ;
    }

    static Stream<Arguments> zipCodesAndPlaces() {
        return Stream.of(
                Arguments.of("us", "90210", "Beverly Hills"),
                Arguments.of("us", "12345", "Schenectady"),
                Arguments.of("ca", "B2R", "Waverley"),
                Arguments.of("JP","100-0001","Chiyoda")
        );
    }


}
