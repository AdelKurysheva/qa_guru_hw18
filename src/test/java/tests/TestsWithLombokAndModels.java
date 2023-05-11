package tests;

import org.junit.jupiter.api.Test;
import tests.lombok.LombokUserData;
import tests.models.UserData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsWithLombokAndModels {
    @Test
    void checkSingleEmail() {
        UserData data = given()
                .spec(Specs.reqresRequest)
                .when()
                .get("/users/2")
                .then()
                .log().body()
                .spec(Specs.response)
                .extract().as(UserData.class);
        assertEquals("janet.weaver@reqres.in", data.getData().getEmail());
    }

    @Test
    void getNameSingleResourceWithGroovy() {
        given()
                .spec(Specs.reqresRequest)
                .when()
                .get("/unknown")
                .then()
                .log().body()
                .spec(Specs.response)
                .body("data.findAll{it.name =~/./}.name.flatten()",
                        hasItem("fuchsia rose"));
    }

    @Test
    void checkSingleName() {
        UserData data = given()
                .spec(Specs.reqresRequest)
                .when()
                .get("/users/2")
                .then()
                .spec(Specs.response)
                .log().body()
                .extract().as(UserData.class);
        assertEquals("Janet", data.getData().getFirstName());
        assertEquals("Weaver", data.getData().getLastName());
    }

    @Test
    void checkSingleNameLombok() {
        LombokUserData data = given()
                .spec(Specs.reqresRequest)
                .when()
                .get("/users/2")
                .then()
                .spec(Specs.response)
                .log().body()
                .extract().as(LombokUserData.class);
        assertEquals("Janet", data.getUser().getFirstName());
        assertEquals("Weaver", data.getUser().getLastName());
    }

    @Test
    void createUser() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .spec(Specs.reqresRequest)
                .body(data)
                .when()
                .post("/users")
                .then()
                .log().body()
                .spec(Specs.responseCreate)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void singleResourceNotFound() {
        given()
                .spec(Specs.reqresRequest)
                .when()
                .get("/unknown/23")
                .then()
                .log().body()
                .spec(Specs.responseNotFound);
    }

    @Test
    void deleteUser() {
        given()
                .spec(Specs.reqresRequest)
                .when()
                .delete("/users/2")
                .then()
                .log().body()
                .spec(Specs.responseDelete);
    }

    @Test
    void loginUnsuccessful() {
        String data = "{ \"email\": \"sydney@fife\" }";

        given()
                .spec(Specs.reqresRequest)
                .body(data)
                .when()
                .post("/register")
                .then()
                .log().body()
                .spec(Specs.responseFailed)
                .body("error", is("Missing password"));
    }
}
