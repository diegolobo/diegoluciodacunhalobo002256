package br.com.rockstars.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ArtistResourceTest {

    @Test
    void shouldListArtistsWithoutAuth() {
        given()
            .when()
            .get("/api/v1/artists")
            .then()
            .statusCode(200)
            .body("content", notNullValue());
    }

    @Test
    void shouldReturn401WhenCreatingArtistWithoutAuth() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Test Artist\",\"type\":\"SOLO\"}")
            .when()
            .post("/api/v1/artists")
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void shouldCreateArtistWithAuth() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Test Artist\",\"type\":\"SOLO\"}")
            .when()
            .post("/api/v1/artists")
            .then()
            .statusCode(201)
            .body("name", equalTo("Test Artist"))
            .body("type", equalTo("SOLO"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void shouldReturn400WhenCreatingArtistWithInvalidData() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"\",\"type\":\"SOLO\"}")
            .when()
            .post("/api/v1/artists")
            .then()
            .statusCode(400);
    }
}
