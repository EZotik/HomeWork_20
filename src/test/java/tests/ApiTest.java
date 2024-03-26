package tests;
import io.restassured.response.Response;
import models.lombok.ApiTestLombokModel;
import models.lombok.ApiTestesLombokResponseModel;
import models.pojo.ApiTestModel;
import models.pojo.ApiTestesResponseModel;
import org.junit.jupiter.api.Test;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ApiSpecs.*;

public class ApiTest extends TestBase {

    @Test
    public void successfulGetUserListTest() {
        Response statusResponse = step("Get запрос на получение списка Users", ()->given(getUserListRequestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpecReference)
                .body(matchesJsonSchemaInClasspath("schemas/list-users-response-schema.json"))
                .extract().response());
        step("Получение ответа с количеством записей", ()->
            assertThat(statusResponse.path("total"), is(12)));
    }

        @Test
        void successfulPostCreatePojoTest() {
            ApiTestModel authData = new ApiTestModel();
            authData.setName("morpheus");
            authData.setJob("leader");

            ApiTestesResponseModel response = step("POST запрос на создание данных", ()->given(requestSpec)
                    .body(authData)
            .when()
                    .post("/users")

            .then()
                    .spec(responseSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/create-response-schema.json"))
                    .extract().as(ApiTestesResponseModel.class));

            step("Получение ответа с данными по Name", ()->
            assertEquals("morpheus", response.getName()));
            step("Получение ответа с данными по Job", ()->
            assertEquals("leader", response.getJob()));
        }

    @Test
    void successfulPutUpdateLombokTest() {
        ApiTestLombokModel authData = new ApiTestLombokModel();
        authData.setName("morpheus");
        authData.setJob("zion resident");

        ApiTestesLombokResponseModel response = step("PUT запрос на замену данных", ()->given(requestSpec)
                .body(authData)
        .when()
                .put("/users/2")

        .then()
                .spec(responseSpecReference)
                .extract().as(ApiTestesLombokResponseModel.class));

        step("Получение ответа с данными по Name", ()->
        assertEquals("morpheus", response.getName()));
        step("Получение ответа с данными по Job", ()->
        assertEquals("zion resident", response.getJob()));
    }
    @Test
    void successfulPatchUpdateTest() {
        ApiTestLombokModel authData = new ApiTestLombokModel();
        authData.setName("morpheus");
        authData.setJob("zion resident");

        ApiTestesLombokResponseModel response = step("PATCH запрос на обновление данных", ()->given(requestSpec)
                .body(authData)
        .when()
                .patch("/users/2")

        .then()
                .spec(responseSpecReference)
                .extract().as(ApiTestesLombokResponseModel.class));

        step("Получение ответа с данными по Name", ()->
        assertEquals("morpheus", response.getName()));
        step("Получение ответа с данными по Job", ()->
        assertEquals("zion resident", response.getJob()));
    }

    @Test
    void successfulDeleteTest() {
        step("DELETE запрос на удаление пользователя", ()->given(requestSpecDelete)
                .when()
                .delete("/users/2")
                .then()
                .spec(responseSpecDelete)
                .extract().response());
    }
}
