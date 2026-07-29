package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.config.IntegrationTestConfig;
import academy.devdojo.config.TestcontainersConfiguration;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

// Carrega todas as classes, starta o servidor
// Definindo uma porta aleatoria para o servidor iniciar
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestcontainersConfiguration.class)
public class ProfileControllerIT extends IntegrationTestConfig {
    private final static String URL = "/v1/profiles";
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("GET v1/profiles - Finding all profiles")
    @Sql(value = "/sql/init_two_profile.sql")
    @Order(1)
    void findAll_ReturnsProfiles_WhenSuccessFul() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
        };
        var responseEntity = restTemplate.exchange(baseUrl() + URL, GET, null, typeReference);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();

        // Verificando se cada item da minha resposta não obtem nada nulo
        responseEntity
                .getBody()
                .forEach(profileGetResponse -> Assertions.assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET v1/profiles - Finding all profiles when nothing is found")
    @Sql("/sql/clean_profile.sql")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsFound() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
        };
        var responseEntity = restTemplate.exchange(baseUrl() + URL, GET, null, typeReference);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("POST v1/profiles - Save profile when success ful")
    @Order(3)
    void save_SavesProfile_WhenSuccessFul() throws IOException {
        // var profileToSave = ProfileUtils.newProfileToSave();
        // var profileHttpEntity = new HttpEntity<>(profileToSave);
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var httpEntity = buildHttpEntity(request);
        var responseEntity = restTemplate.exchange(baseUrl() + URL, POST, httpEntity, ProfilePostResponse.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    @ParameterizedTest
    @MethodSource(value = "postProfileBadRequest")
    @DisplayName("POST v1/profiles - Save profile when name and description is empty")
    @Order(4)
    void save_SavesProfile_WhenNameAndDescriptionEmpty(String requestFile, String responseFile) throws Exception {
        var request = fileUtils.readResourceFile("profile/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("profile/%s".formatted(responseFile));

        var httpEntity = buildHttpEntity(request);
        var exception = Assertions.catchThrowableOfType(
                () -> restTemplate.exchange(baseUrl() + URL, POST, httpEntity, String.class),
                HttpClientErrorException.BadRequest.class);

        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Com a dependencia do maven chamada: Json Unit AssertJ
        // Conseguimos ignorar um campo do que queremos comparar
        // Por exemplo: o timestamp, pois ele muda a cada run no codigo
        JsonAssertions.assertThatJson(exception.getResponseBodyAsString())
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> postProfileBadRequest() {
        return Stream.of(
                Arguments.of("/post-request-profile-blank-field-400.json", "/post-response-profile-blank-field-400.json")
        );
    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, httpHeader);
    }

    private String baseUrl() {
        return "http://localhost:%d".formatted(port);
    }
}
