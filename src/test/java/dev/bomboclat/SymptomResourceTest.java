import dev.bomboclat.model.LoginRequest;
import dev.bomboclat.model.RegisterRequest;
import dev.bomboclat.model.SymptomEntry;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class SymptomResourceTest {

    private String registerAndLogin() {
        RegisterRequest register = new RegisterRequest();
        register.email = "symptom@example.com";
        register.password = "secret";
        register.name = "Symptom User";

        given()
                .contentType(ContentType.JSON)
                .body(register)
                .when().post("/auth/register")
                .then()
                .statusCode(201);

        LoginRequest login = new LoginRequest();
        login.setEmail("symptom@example.com");
        login.setPassword("secret");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("/auth/login");

        return response.then()
                .statusCode(200)
                .extract().path("token");
    }

    @Test
    public void testCreateAndListSymptoms() {
        String token = registerAndLogin();

        SymptomEntry entry = new SymptomEntry();
        entry.painLevel = 3;
        entry.stiffnessLevel = 2;
        entry.notes = "feeling ok";

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(entry)
                .when().post("/symptoms")
                .then().statusCode(201)
                .body("painLevel", equalTo(3));

        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/symptoms")
                .then().statusCode(200)
                .body("size()", equalTo(1));
    }
}
