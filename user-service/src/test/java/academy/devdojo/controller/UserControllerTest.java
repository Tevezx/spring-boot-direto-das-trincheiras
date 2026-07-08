package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserRepository;
import org.junit.jupiter.api.*;
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
    @DisplayName("GET v1/users/90 returns throw ResponseStatusException when id not exists")
    @Order(5)
    void findById_ReturnsThrowResponseStatusException_WhenIdNotExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not Found"));
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
    void deleteById_ReturnsThrowResponseStatusException_WhenIdNotExists() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var id = 90L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not Found"));
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

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("User not Found"));
    }
}