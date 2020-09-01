package goRest;
import goRest.model.Posts;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class GoRestPostsTests {

    private int postId;

    @Test()
    public void getPosts() {
        List<Posts> postsList = given()
                .when()
                .get("https://gorest.co.in/public-api/posts")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("data", not(empty()))
                .extract().jsonPath().getList("data", Posts.class);

        for (Posts post : postsList) {
            System.out.println(post);
        }
    }

    @Test()
    public void createPost() {
        postId = given()
                // prerequisite data
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body("{\"user_id\":\"" + getRandomUserId() + "\", \"title\": \"Techno\", \"body\":\"Male\"}")
                .when()
                //action
                .post("https://gorest.co.in/public-api/posts")
                .then()
                .log().body()
                //validations
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(201))
                .extract().jsonPath().getInt("data.id");
        System.out.println(postId);
    }

    @Test(dependsOnMethods = "createPost")
    public void getPostById(){
        given()
                .pathParam("postId", postId)
                .when()
                .get("https://gorest.co.in/public-api/posts/{postId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("data.id", equalTo(postId))
        ;
    }

    @Test(dependsOnMethods = "createPost")
    public void updatePostById(){
        String updateText = "Update Post Test";
        given()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body("{\"body\": \""+updateText+"\"}")
                .pathParam("postId",postId)
                .when()
                .put("https://gorest.co.in/public-api/posts/{postId}")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("data.body", equalTo(updateText));
    }

    @Test(dependsOnMethods = "createPost", priority = 1)
    public void deletePostById(){
        given()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .pathParam("postId",postId)
                .when()
                .delete("https://gorest.co.in/public-api/posts/{postId}")
                .then()
                .statusCode(200)
                .body("code", equalTo(204))
        ;
    }

    @Test(dependsOnMethods = "deletePostById")
    public void getPostByIdNegative() {
        given()
                .pathParam("postId", postId)
                .when()
                .get("https://gorest.co.in/public-api/posts/{postId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(404))
        ;
    }

    private String getRandomUserId() {
        return new Random().nextInt(10) + 1 + "";
    }
}
