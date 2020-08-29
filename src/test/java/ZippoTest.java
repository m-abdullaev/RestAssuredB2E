import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void test() {
        given()
                .when()
                .then()
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
        ;
    }

    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void logTest() {
        given()
                .log().all() // print out request
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all() // print out response
        ;
    }

    @Test
    public void bodyJsonPathTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all() // print out response
                .body("country", equalTo("United States"))
        ;
    }

    @Test
    public void bodyJsonPathTest2() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all() // print out response
                .body("places[0].state", equalTo("California"))
        ;
    }

    @Test
    public void bodyJsonPathTest3() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all() // print out response
                .body("places[0].'place name'", equalTo("Beverly Hills"))
        ;
    }

    @Test
    public void bodyArraySizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all() // print out response
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void chainingTests() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().all() // print out response
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places[0].'state abbreviation'", equalTo("CA"))
        ;
    }


}
