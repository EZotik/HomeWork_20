package tests;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTest extends TestBase {

    @Test
    public void successfulGetUserListTest() {
        Response statusResponse = given()

                .log().uri()
                .log().method()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/list-users-response-schema.json"))
                .extract().response();

        assertThat(statusResponse.path("total"), is(12));
    }

        @Test
        void successfulPostCreateTest() {
            String body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

            Response statusResponse = given()
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/users")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(201)
                    .body(matchesJsonSchemaInClasspath("schemas/create-response-schema.json"))
                    .extract().response();

            assertThat(statusResponse.path("name"), is("morpheus"));
            assertThat(statusResponse.path("job"), is("leader"));
        }

    @Test
    void successfulPutUpdateTest() {
        String body = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        Response statusResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        assertThat(statusResponse.path("name"), is("morpheus"));
        assertThat(statusResponse.path("job"), is("zion resident"));
        assertThat(statusResponse.path("updatedAt"), notNullValue());
    }

    @Test
    void successfulPatchUpdateTest() {
        String body = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        Response statusResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .patch("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        assertThat(statusResponse.path("name"), is("morpheus"));
        assertThat(statusResponse.path("job"), is("zion resident"));
        assertThat(statusResponse.path("updatedAt"), notNullValue());
    }
    @Test
    void successfulDeleteTest() {

    given()
                .log().uri()
                .log().method()
                .when()
                .delete("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204)
                .extract().response();
    }
}
