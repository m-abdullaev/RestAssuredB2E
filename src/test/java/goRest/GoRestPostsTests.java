package goRest;

import goRest.model.Posts;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestPostsTests {

    private int postId;

    @BeforeClass
    public void init() {
        baseURI = "https://gorest.co.in/public-api/posts";
    }

    @Test()
    public void getPosts() {
        List<Posts> postsList = given()
                .when()
                .get()
                .then()
                .spec(getStatusCodeSpec(200))
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
                .post()
                .then()
                .log().body()
                //validations
                .spec(getStatusCodeSpec(201))
                .extract().jsonPath().getInt("data.id");
        System.out.println(postId);
    }

    @Test(dependsOnMethods = "createPost")
    public void getPostById() {
        given()
                .pathParam("postId", postId)
                .when()
                .get("/{postId}")
                .then()
                .spec(getStatusCodeSpec(200))
                .body("data.id", equalTo(postId))
        ;
    }

    @Test(dependsOnMethods = "createPost")
    public void updatePostById() {
        String updateText = "Update Post Test";
        given()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .contentType(ContentType.JSON)
                .body("{\"body\": \"" + updateText + "\"}")
                .pathParam("postId", postId)
                .when()
                .put("/{postId}")
                .then()
                .spec(getStatusCodeSpec(200))
                .body("data.body", equalTo(updateText));
    }

    @Test(dependsOnMethods = "createPost", priority = 1)
    public void deletePostById() {
        given()
                .header("Authorization", "Bearer 55b19d86844d95532f80c9a2103e1a3af0aea11b96817e6a1861b0d6532eef47")
                .pathParam("postId", postId)
                .when()
                .delete("/{postId}")
                .then()
                .spec(getStatusCodeSpec(204))
        ;
    }

    @Test(dependsOnMethods = "deletePostById")
    public void getPostByIdNegative() {
        given()
                .pathParam("postId", postId)
                .when()
                .get("/{postId}")
                .then()
                .spec(getStatusCodeSpec(404))
        ;
    }

    private ResponseSpecification getStatusCodeSpec(Integer statusCode){
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody("code", equalTo(statusCode))
                .build();
    }

    private String getRandomUserId() {
        return new Random().nextInt(10) + 1 + "";
    }
}
