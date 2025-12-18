package com.example;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
public class UserResourceTest {

    @Test
    public void testGetAllUsers() {
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.name = "John Doe";

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/users")
                .then()
                .statusCode(200)
                .body("name", is("John Doe"))
                .body("id", greaterThan(0));
    }
}