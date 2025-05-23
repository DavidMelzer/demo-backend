package dev.bomboclat;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testLoginSuccess() {
        // Test with valid credentials from data.json
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"bomboclat@example.com\",\"password\":\"password123\"}")
            .when()
            .post("/auth/login")
            .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("tokenType", is("Bearer"));
    }

    @Test
    public void testLoginFailure() {
        // Test with invalid credentials
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"bomboclat@example.com\",\"password\":\"wrongpassword\"}")
            .when()
            .post("/auth/login")
            .then()
            .statusCode(401);
    }

    @Test
    public void testLoginBadRequest() {
        // Test with missing credentials
        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
            .post("/auth/login")
            .then()
            .statusCode(400);
    }
}