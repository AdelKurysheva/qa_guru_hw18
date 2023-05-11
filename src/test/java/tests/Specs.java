package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class Specs {
    public static RequestSpecification reqresRequest = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);
    public static ResponseSpecification response = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification responseCreate = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .build();

    public static ResponseSpecification responseDelete = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .build();

    public static ResponseSpecification responseFailed = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .build();
    public static ResponseSpecification responseNotFound = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .build();
}
