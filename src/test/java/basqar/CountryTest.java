package basqar;

import basqar.model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    private Cookies cookies;
    private String randomGenName;
    private String randomGenCode;
    private String id;

    @BeforeClass
    public void init() {
        baseURI = "https://test.basqar.techno.study";

        randomGenName = RandomStringUtils.randomAlphabetic(8);
        randomGenCode = RandomStringUtils.randomAlphabetic(4);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "daulet2030@gmail.com");
        credentials.put("password", "TechnoStudy123@");

        cookies = given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies();
    }

    @Test
    public void searchRandomCountryNegative() {

        Map<String, String> searchBody = new HashMap<>();
        searchBody.put("name", RandomStringUtils.randomAlphabetic(8));

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(searchBody)
                .when()
                .post("/school-service/api/countries/search")
                .then()
                .body(equalTo("[]"))
        ;
    }

    @Test
    public void createCountry() {
        Country country = new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);

        id = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(201)
                .body("id", not(empty()))
                .extract().path("id");
//                .extract().jsonPath().getString("id");
        System.out.println(id);
    }

    @Test(dependsOnMethods = "createCountry")
    public void searchCountry() {

        Map<String, String> searchBody = new HashMap<>();
        searchBody.put("name", randomGenName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(searchBody)
                .when()
                .post("/school-service/api/countries/search")
                .then()
                .body(not(empty()))
                .body("name", hasItem(randomGenName)) // at least one item should contain name = randomGenName
        ;
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {
        Country country = new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(400)
                .body("message", equalTo("The Country with Name \"" + randomGenName + "\" already exists."));
    }

    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {
        Country country = new Country();
        country.setId(id);
        country.setName(RandomStringUtils.randomAlphabetic(8));
        country.setCode(RandomStringUtils.randomAlphabetic(4));

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(405) // TODO: fix this
                .body("name", equalTo(country.getName()))
                .body("code", equalTo(country.getCode()));
    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteById() {
        given()
                .cookies(cookies)
                .pathParam("countryId", id)
                .when()
                .delete("school-service/api/countries/{countryId}")
                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteById")
    public void searchDeletedCountryNegative() {

        Map<String, String> searchBody = new HashMap<>();
        searchBody.put("name", randomGenName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(searchBody)
                .when()
                .post("/school-service/api/countries/search")
                .then()
                .body(equalTo("[]"))
        ;
    }

    @Test(dependsOnMethods = "deleteById")
    public void updateCountryNegative() {
        Country country = new Country();
        country.setId(id);
        country.setName(RandomStringUtils.randomAlphabetic(8));
        country.setCode(RandomStringUtils.randomAlphabetic(4));

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(404)
                .body("message", equalTo("Country not found"));
    }

    @Test(dependsOnMethods = "deleteById")
    public void deleteByIdNegative() {
        given()
                .cookies(cookies)
                .pathParam("countryId", id)
                .when()
                .delete("school-service/api/countries/{countryId}")
                .then()
                .statusCode(404)
        ;
    }
}
