import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

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
}
