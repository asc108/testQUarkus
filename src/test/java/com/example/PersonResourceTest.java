package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class PersonResourceTest {

    @Test
    public void testListEndpoint() {
        given()
          .when().get("/persons")
          .then()
             .statusCode(200)
             .body("size()", greaterThan(0));
    }

    @Test
    public void testCreatePerson() {
        Person person = new Person();
        person.name = "Test User";
        person.email = "test@example.com";

        given()
          .contentType(ContentType.JSON)
          .body(person)
          .when().post("/persons")
          .then()
             .statusCode(200)
             .body("name", is("Test User"))
             .body("email", is("test@example.com"));
    }
}