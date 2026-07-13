package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {
    private static final String URL = "/v1/users";
    @Autowired
    private MockMvc mockMvc;
    @MockitoSpyBean
    private UserRepository repository;
    @MockitoBean
    private UserData userData;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserUtils userUtils;
    private List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("GET v1/users returns all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessFul() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-all-users-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/filterName returns all users when name is equal to parametrized")
    @Order(2)
    void listUserName_ReturnsUsers_WhenNameExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-list-name-carlos-user-200.json");
        var name = userList.getFirst().getFirstName();

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/filterName?name=x returns empty list when name is not exists")
    @Order(3)
    void listUserName_ReturnsEmptyList_WhenNameNotExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-list-name-x-user-200.json");
        var name = "Paulo";

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/filterName").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 returns user by id = 1")
    @Order(4)
    void findById_ReturnsUser_WhenIdExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/get-find-by-id-1-200.json");
        var id = userList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/90 returns throw ThrowNotFound when id not exists")
    @Order(5)
    void findById_ReturnsThrowNotFound_WhenIdNotExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-find-by-id-404.json");

        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/users saving user when success ful")
    @Order(6)
    void save_SavesUser_WhenSuccessFul() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");
        var user = User.builder().id(3L).firstName("Paulo").lastName("Silva").email("paulo@gmail.com").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/users/1 delete user by id when id exists")
    @Order(7)
    void deleteById_ReturnsUser_WhenIdExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users/90 delete user by id when id not exists")
    @Order(8)
    void deleteById_ReturnsThrowNotFound_WhenIdNotExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/delete-by-id-404.json");

        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/users update user when id exists")
    @Order(9)
    void update_UpdateUser_WhenIdExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users update user when id not exists")
    @Order(10)
    void update_UpdateUser_WhenIdNotExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var request = fileUtils.readResourceFile("user/put-request-throw-user-200.json");
        var response = fileUtils.readResourceFile("user/put-response-user-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    // Testando o beanValidation para verificar se os campos estao nulos ou nao
    @ParameterizedTest
    // Todas as vezes que eu executar esse teste, ele chama esse metodo
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are empty")
    @Order(11)
    // Passo um fileName (no caso meu request) e uma lista de erros
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        // Pego o retorno do meu mvc e qual foi a excecao resolvida dele
        Exception resolvedException = mvcResult.getResolvedException();
        // Verifico se realemente há excecoes dentro
        Assertions.assertThat(resolvedException).isNotNull();

        // Verifico se o meu resolvedException contem as minhas mensagens de erros definidas no bean validation
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when field are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    // No metodo, nos dizemos que vamos retornar dois streams arguments
    // O retorno é justamente o que queremos testar, toda vez que o test 11 ser executado ira chamar esses dois argumentos para testar
    // Dessa forma, eu nao preciso fazer varios testes repetindo codigo
    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", allRequiredErrors()),
                Arguments.of("post-request-user-blank-fields-400.json", allRequiredErrors()),
                Arguments.of("post-request-user-email-invalid-field-400.json", errorEmailInvalid())
        );
    }

    // Cada metodo tem sua responsabilidade, por isso nao acoplei esse codigo em um metodo apenas para o put e post
    private static Stream<Arguments> putBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-user-empty-field-400.json", allRequiredErrors()),
                Arguments.of("put-request-user-blank-field-400.json", allRequiredErrors()),
                Arguments.of("put-request-user-email-invalid-400.json", errorEmailInvalid()),
                Arguments.of("put-request-user-id-null-field-400.json", errorIdCannotNull())
        );
    }

    private static List<String> allRequiredErrors() {
        var firstNameRequiredError = "The field 'firstName' is required";
        var lastNameRequiredError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";

        return List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError);
    }

    private static List<String> errorEmailInvalid() {
        var emailInvalidError = "e-mail is not valid";
        return List.of(emailInvalidError);
    }

    private static List<String> errorIdCannotNull() {
        var idCannotNullError = "The field 'id' cannot be null";
        return List.of(idCannotNullError);
    }

//    @Test
//    @DisplayName("POST v1/users returns bad request when fields are blank")
//    @Order(12)
//    void save_ReturnsBadRequest_WhenFieldsAreBlank() throws Exception {
//        var request = fileUtils.readResourceFile("user/post-request-user-blank-fields-400.json");
//
//        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andReturn();
//
//        // Pego o retorno do meu mvc e qual foi a excecao resolvida dele
//        Exception resolvedException = mvcResult.getResolvedException();
//        // Verifico se realemente há excecoes dentro
//        Assertions.assertThat(resolvedException).isNotNull();
//
//        // Defino as mensagens de erros
//        var firstNameError = "The field 'firstName' is required";
//        var lastNameError = "The field 'lastName' is required";
//        var emailError = "The field 'email' is required";
//
//        // Verifico se o meu resolvedException contem as minhas mensagens de erros definidas no bean validation
//        Assertions.assertThat(resolvedException.getMessage()).contains(firstNameError, lastNameError, emailError);
//    }


}