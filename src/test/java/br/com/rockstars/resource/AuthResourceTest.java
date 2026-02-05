package br.com.rockstars.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AuthResourceTest {

    @Test
    void shouldReturn400WithInvalidCredentials() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"username\":\"invalid\",\"password\":\"invalid\"}")
            .when()
            .post("/api/v1/auth/login")
            .then()
            .statusCode(400)
            .body("message", equalTo("Usuario ou senha invalidos"));
    }

    @Test
    void shouldReturn400WithEmptyCredentials() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"username\":\"\",\"password\":\"\"}")
            .when()
            .post("/api/v1/auth/login")
            .then()
            .statusCode(400);
    }

    @Test
    void shouldReturn401WhenRefreshingWithoutAuth() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/auth/refresh")
            .then()
            .statusCode(401);
    }
}
