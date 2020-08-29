import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TaskSolutions {
    @Test
    public void task1() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }

    @Test
    public void task2() {
//        String body = given()
//                .when()
//                .get("https://httpstat.us/418")
//                .then()
//                .statusCode(418)
//                .contentType(ContentType.TEXT)
//                .extract().asString();
//
//        Assert.assertEquals(body, "418 I'm a teapot");

        given()
                .when()
                .get("https://httpstat.us/418")
                .then()
                .statusCode(418)
                .contentType(ContentType.TEXT)
                .body(equalTo("418 I'm a teapot"));
    }

    @Test
    public void task3() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;
    }

    @Test
    public void task4() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed",equalTo(false));
        ;
    }

    @Test
    public void task5() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title[2]", equalTo("fugiat veniam minus"))
                .body("userId[2]", equalTo(1))
        ;

    }
}
