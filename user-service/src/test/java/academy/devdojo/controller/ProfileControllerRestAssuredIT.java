package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.config.TestcontainersConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestcontainersConfiguration.class)
public class ProfileControllerRestAssuredIT extends IntegrationTestConfig {
    private final static String URL = "/v1/profiles";
    @LocalServerPort
    private int port;
    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET v1/profiles - Finding all profiles")
    @Sql(value = "/sql/init_two_profile.sql")
    @Order(1)
    void findAll_ReturnsProfiles_WhenSuccessFul() {
        var response = fileUtils.readResourceFile("profile/get-all-profiles-200.json");
        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/profiles - Finding all profiles when nothing is found")
    @Sql("/sql/clean_profile.sql")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsFound() {
        var response = fileUtils.readResourceFile("profile/get-find-all-empty-list-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("POST v1/profiles - Save profile when success ful")
    @Order(3)
    void save_SavesProfile_WhenSuccessFul() {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var expectedResponse = fileUtils.readResourceFile("profile/post-response-profile-201.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                // Possui um body
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();

    }

    @ParameterizedTest
    @MethodSource(value = "postProfileBadRequest")
    @DisplayName("POST v1/profiles - Save profile when name and description is empty")
    @Order(4)
    void save_SavesProfile_WhenNameAndDescriptionEmpty(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("profile/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("profile/%s".formatted(responseFile));

        // Defino como variavel para poder fazer o jsonassertions
        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                // Possui um body
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                // Extraindo a responsa para o body em string
                .extract().response().body().asString();

        // Ignoro o timestamp e verifico se a resposta é igual ao esperado
        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> postProfileBadRequest() {
        return Stream.of(
                Arguments.of("/post-request-profile-blank-field-400.json", "/post-response-profile-blank-field-400.json")
        );
    }
}
