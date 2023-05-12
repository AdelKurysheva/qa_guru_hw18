package tests;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.LombokUserData;
import tests.models.*;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;

public class TestsWithLombokAndModels {
    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Проверка наличия выбранного email")
    void checkSingleEmailLombok() {
        LombokUserData data = step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .spec(Specs.reqresRequest)
                        .when()
                        .get("/users/2")
                        .then()
                        .log().body()
                        .spec(Specs.response)
                        .extract().as(LombokUserData.class));
        step("Verify response", () ->
                assertThat(data.getUser().getEmail()).isEqualTo("janet.weaver@reqres.in"));
    }

    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Проверка наличия выбранного наименования цвета")
    void getNameSingleResourceWithGroovy() {
        step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .spec(Specs.reqresRequest)
                        .when()
                        .get("/unknown")
                        .then()
                        .log().body()
                        .spec(Specs.response)
                        .body("data.findAll{it.name =~/./}.name.flatten()",
                                hasItem("fuchsia rose")));
    }

    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Проверка данных пользователя")
    void checkSingleName() {
        UserData data = step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .spec(Specs.reqresRequest)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(Specs.response)
                        .log().body()
                        .extract().as(UserData.class));
        step("Verify response: first name", () -> assertThat(data.getData().getFirstName()).isEqualTo("Janet"));
        step("Verify response: last name", () -> assertThat(data.getData().getLastName()).isEqualTo("Weaver"));
    }

    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Проверка данных пользователя (Lombok)")
    void checkSingleNameLombok() {
        LombokUserData data = step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .spec(Specs.reqresRequest)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(Specs.response)
                        .log().body()
                        .extract().as(LombokUserData.class));
        step("Verify response: first name", () -> assertThat(data.getUser().getFirstName()).isEqualTo("Janet"));
        step("Verify response: last name", () -> assertThat(data.getUser().getLastName()).isEqualTo("Weaver"));
    }

    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Создание нового пользователя")
    void createUserWithPojo() {
        CreateBodyPojoModel createBody = new CreateBodyPojoModel();
        createBody.setName("Adel");
        createBody.setJob("qa");
        CreateResponsePojoModel createResponse = step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .spec(Specs.reqresRequest)
                        .body(createBody)
                        .when()
                        .post("/users")
                        .then()
                        .log().body()
                        .spec(Specs.responseCreate)
                        .extract().as(CreateResponsePojoModel.class));
        step("Verify response: name", () -> assertThat(createResponse.getName()).isEqualTo("Adel"));
        step("Verify response: job", () -> assertThat(createResponse.getJob()).isEqualTo("qa"));
    }

    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Проверка отсутствия ресурса (Negative)")
    void singleResourceNotFound() {
        step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .spec(Specs.reqresRequest)
                        .when()
                        .get("/unknown/23")
                        .then()
                        .log().body()
                        .spec(Specs.responseNotFound));
    }

    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Удаление данных")
    void deleteUser() {
        step("Make request", () -> given()
                .filter(withCustomTemplates())
                .spec(Specs.reqresRequest)
                .when()
                .delete("/users/2")
                .then()
                .log().body()
                .spec(Specs.responseDelete));
    }

    @Test
    @Owner("Курышева Адэль")
    @DisplayName("Проверка валидации пароля (Negative)")
    void loginUnsuccessfulWithPojo() {
        User loginBody = new User();
        loginBody.setEmail("sydney@fife");
        ResponsePojoModel responseBody = step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .spec(Specs.reqresRequest)
                        .body(loginBody)
                        .when()
                        .post("/register")
                        .then()
                        .log().body()
                        .spec(Specs.responseFailed)
                        .extract().as(ResponsePojoModel.class));
        step("Verify response", () -> assertThat(responseBody.getError()).isEqualTo("Missing password"));
    }
}
