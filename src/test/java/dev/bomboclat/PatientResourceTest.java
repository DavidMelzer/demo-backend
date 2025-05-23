package dev.bomboclat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;

@QuarkusTest
public class PatientResourceTest {

    @Test
    public void testGetAllPatientsEndpoint() {
        given()
          .when().get("/patients")
          .then()
             .statusCode(200)
             .body("size()", is(2))
             .body("id", hasItems(12345, 67890))
             .body("name", hasItems("Bomboclat", "John Doe"))
             .body("age", hasItems(30, 25))
             .body("email", hasItems("bomboclat@example.com", "johndoe@example.com"));
    }

    @Test
    public void testGetPatientByIdEndpoint() {
        given()
          .when().get("/patients/12345")
          .then()
             .statusCode(200)
             .body("id", is(12345))
             .body("name", is("Bomboclat"))
             .body("age", is(30))
             .body("email", is("bomboclat@example.com"));
    }

    @Test
    public void testGetPatientByIdNotFound() {
        given()
          .when().get("/patients/99999")
          .then()
             .statusCode(404);
    }
}