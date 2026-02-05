package br.com.rockstars.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AlbumResourceTest {

    @Test
    void shouldListAlbumsWithoutAuth() {
        given()
            .when()
            .get("/api/v1/albums")
            .then()
            .statusCode(200)
            .body("content", notNullValue());
    }

    @Test
    void shouldReturn401WhenCreatingAlbumWithoutAuth() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"title\":\"Test Album\",\"releaseYear\":2024}")
            .when()
            .post("/api/v1/albums")
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void shouldCreateAlbumWithAuth() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"title\":\"Test Album\",\"releaseYear\":2024}")
            .when()
            .post("/api/v1/albums")
            .then()
            .statusCode(201)
            .body("title", equalTo("Test Album"))
            .body("releaseYear", equalTo(2024));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void shouldReturn400WhenCreatingAlbumWithInvalidData() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"title\":\"\",\"releaseYear\":2024}")
            .when()
            .post("/api/v1/albums")
            .then()
            .statusCode(400);
    }
}
