import dev.bomboclat.model.LoginRequest;
import dev.bomboclat.model.RegisterRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testRegisterAndLogin() {
        RegisterRequest register = new RegisterRequest();
        register.email = "test@example.com";
        register.password = "secret";
        register.name = "Test User";

        given()
                .contentType(ContentType.JSON)
                .body(register)
                .when().post("/auth/register")
                .then()
                .statusCode(201);

        LoginRequest login = new LoginRequest();
        login.setEmail("test@example.com");
        login.setPassword("secret");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("/auth/login");

        response.then().statusCode(200)
                .body("token", notNullValue());
    }
}
