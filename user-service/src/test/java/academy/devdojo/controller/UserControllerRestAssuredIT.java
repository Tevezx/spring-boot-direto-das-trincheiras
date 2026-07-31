package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.repository.UserRespository;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerRestAssuredIT extends IntegrationTestConfig {
    private final static String URL = "/v1/users";
    @LocalServerPort
    private int port;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserRespository respository;

    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET v1/users returns all users")
    @Sql(value = "/sql/user/init_two_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessFul() {
        var expectedResponse = fileUtils.readResourceFile("user/get-all-users-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/users/filterName returns all users when name is equal to parametrized")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    void listUserName_ReturnsUsers_WhenNameExists() {
        var expectedResponse = fileUtils.readResourceFile("user/get-list-name-carlos-user-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("filterName", "Carlos")
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/users/filterName?name=x returns empty list when name is not exists")
    @Order(3)
    void listUserName_ReturnsEmptyList_WhenNameNotExists() {
        var expectedResponse = fileUtils.readResourceFile("user/get-list-name-x-user-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("filterName", "Paulo")
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/users/1 returns user by id = 1")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(4)
    void findById_ReturnsUser_WhenIdExists() {
        var expectedResponse = fileUtils.readResourceFile("user/get-find-by-id-1-200.json");
        var userId = respository.findAll().getFirst().getId();

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", userId)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/users/90 returns throw ThrowNotFound when id not exists")
    @Order(5)
    void findById_ReturnsThrowNotFound_WhenIdNotExists() {
        var expectedResponse = fileUtils.readResourceFile("user/get-find-by-id-404.json");
        var userId = 90L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", userId)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("POST v1/users saving user when success ful")
    @Order(6)
    void save_SavesUser_WhenSuccessFul() {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/post-response-user-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("DELETE v1/users/1 delete user by id when id exists")
    @Sql(value = "/sql/user/init_one_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(7)
    void deleteById_ReturnsUser_WhenIdExists() {
        var userId = respository.findAll().getFirst().getId();

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", userId)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("DELETE v1/users/90 delete user by id when id not exists")
    @Order(8)
    void deleteById_ReturnsThrowNotFound_WhenIdNotExists() {
        var expectedResponse = fileUtils.readResourceFile("user/delete-by-id-404.json");
        var id = 90L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("PUT v1/users update user when id exists")
    @Order(9)
    void update_UpdateUser_WhenIdExists() {
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("PUT v1/users update user when id not exists")
    @Order(10)
    void update_UpdateUser_WhenIdNotExists() {
        var request = fileUtils.readResourceFile("user/put-request-throw-user-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/put-response-user-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource("putBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when field are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", "post-response-user-empty-fields-400.json"),
                Arguments.of("post-request-user-blank-fields-400.json", "post-response-user-blank-fields-400.json"),
                Arguments.of("post-request-user-email-invalid-field-400.json", "post-response-user-email-invalid-field-400.json")
        );
    }

    private static Stream<Arguments> putBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-user-empty-field-400.json", "put-response-user-empty-field-400.json"),
                Arguments.of("put-request-user-blank-field-400.json", "put-response-user-blank-field-400.json"),
                Arguments.of("put-request-user-email-invalid-400.json", "put-response-user-email-invalid-400.json"),
                Arguments.of("put-request-user-id-null-field-400.json", "put-response-user-id-null-field-400.json")
        );
    }
}
