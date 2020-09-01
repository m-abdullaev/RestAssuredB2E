package goRest;

import goRest.model.User;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestTests {
    @Test
    public void getUsers() {
        List<User> userList = given()
                .when()
                .get("https://gorest.co.in/public-api/users")
                .then()
                .log().body()
                //assertions
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("data", not(empty()))
                //extracting users list
                .extract().jsonPath().getList("data", User.class);

        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void getUsersExtactingMultipleTimes() {
        ExtractableResponse<Response> extract = given()
                .when()
                .get("https://gorest.co.in/public-api/users")
                .then()
                //assertions
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("data", not(empty()))
                //extracting users list
                .extract();

        int code = extract.jsonPath().getInt("code");
        System.out.println("Code: " + code);

//        List<User> userList = extract.jsonPath().getList("data", User.class);
//        for (User user : userList) {
//            System.out.println(user);
//        }

//        User[] data = extract.jsonPath().getObject("data", User[].class);
//        for (int i = 0; i < data.length; i++) {
//            System.out.println(data[i]);
//        }

        // extracting certain user only
        User user = extract.jsonPath().getObject("data[4]", User.class);
        System.out.println(user);
    }

    @Test
    public void createUser() {
        given()
                // prerequisite data
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body("{\"email\":\"Carmine_Koch2@gmail.com\", \"name\": \"Techno\", \"gender\":\"Male\", \"status\": \"Active\"}")
                .when()
                //action
                .post("https://gorest.co.in/public-api/users")
                .then()
                //validations
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(201))
        ;
    }
}
