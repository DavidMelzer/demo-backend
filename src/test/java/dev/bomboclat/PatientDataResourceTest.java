package dev.bomboclat;

import dev.bomboclat.model.PatientData;
import dev.bomboclat.resource.PatientDataResource;
import dev.bomboclat.service.AuthService;
import dev.bomboclat.service.PatientDataService;
import dev.bomboclat.util.JwtUtil;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class PatientDataResourceTest {

    @Inject
    AuthService authService;

    @Inject
    JwtUtil jwtUtil;

    @Inject
    PatientDataResource patientDataResource;

    @Test
    public void testGetPatientDataWithValidToken() {
        // First, get a valid token by authenticating
        String requestBody = "{\"email\":\"bomboclat@example.com\",\"password\":\"password123\"}";
        String token = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        System.out.println("[DEBUG_LOG] Token from login: " + token);

        // Then use the token to access the patient data endpoint
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/patient-data")
                .then()
                .statusCode(200)
                .body("id", is(4711))
                .body("user_id", is(12345))
                .body("rheuma_type", is("rheumatoid-arthritis"))
                .body("medication", notNullValue())
                .body("medication.size()", is(2))
                .body("medication[0].name", is("methotrexate"))
                .body("medication[1].name", is("adalimumab"));
    }

    @Test
    public void testGetPatientDataDirectly() {
        // Generate a token directly
        String email = "bomboclat@example.com";
        String token = jwtUtil.generateToken(email);
        System.out.println("[DEBUG_LOG] Generated token: " + token);

        // Call the resource method directly
        Response response = patientDataResource.getPatientData("Bearer " + token);
        System.out.println("[DEBUG_LOG] Response status: " + response.getStatus());

        // Verify the response
        assertEquals(200, response.getStatus());
        assertTrue(response.getEntity() instanceof PatientData);

        PatientData data = (PatientData) response.getEntity();
        assertEquals(4711, data.getId());
        assertEquals(12345, data.getUser_id());
        assertEquals("rheumatoid-arthritis", data.getRheuma_type());
        assertEquals(2, data.getMedication().size());
        assertEquals("methotrexate", data.getMedication().get(0).getName());
        assertEquals("adalimumab", data.getMedication().get(1).getName());
    }

    @Test
    public void testGetPatientDataWithoutToken() {
        // Test without providing an Authorization header
        given()
                .when()
                .get("/patient-data")
                .then()
                .statusCode(401);
    }

    @Test
    public void testGetPatientDataWithInvalidToken() {
        // Test with an invalid token
        given()
                .header("Authorization", "Bearer invalid.token.here")
                .when()
                .get("/patient-data")
                .then()
                .statusCode(401);
    }
}
