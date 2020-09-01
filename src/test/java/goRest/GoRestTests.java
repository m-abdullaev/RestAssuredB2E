package goRest;

import goRest.model.User;
import io.restassured.http.ContentType;
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
}
