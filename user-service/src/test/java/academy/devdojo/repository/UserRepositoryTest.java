package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(value = MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
    @InjectMocks
    private UserRepository repository;

    @Mock
    private UserData userData;
    @InjectMocks
    private UserUtils userUtils;
    private List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("Find all users successfully")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotNull().isNotEmpty().hasSize(userList.size());
    }

    @Test
    @DisplayName("List all users by first name")
    @Order(2)
    void listAllNames_ReturnsUsersByName_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var userResponse = repository.listAllNames("Carlos");

        Assertions.assertThat(userResponse).isNotEmpty().hasSize(1);
        // Verifico se o meu usuario retornado é da match com o meu nome passado
        Assertions.assertThat(userResponse).allMatch(u -> u.getFirstName().equalsIgnoreCase("Carlos"));
    }

    @Test
    @DisplayName("Return empty list when name is null")
    @Order(3)
    void listAllNames_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var userResponse = repository.listAllNames(null);

        Assertions.assertThat(userResponse).isEmpty();
    }

    @Test
    @DisplayName("Find by id when user id exists")
    @Order(4)
    void findById_ReturnsUser_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var userRequest = userList.getFirst();
        var userResponse = repository.findById(userRequest.getId());

        Assertions.assertThat(userResponse).isPresent().contains(userRequest);
    }

    @Test
    @DisplayName("Find by id when user id does not exist")
    @Order(5)
    void findById_ReturnsEmpty_WhenIdDoesNotExist() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var userResponse = repository.findById(4L);

        Assertions.assertThat(userResponse).isNotPresent();
    }

    @Test
    @DisplayName("Save user successfully")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var user = User.builder().id(3L).firstName("Paulo").lastName("Silva").email("paulo@gmail.com").build();
        var userResponse = repository.save(user);

        Assertions.assertThat(userResponse).isNotNull().isEqualTo(user);
    }

    @Test
    @DisplayName("Delete user by id successfully")
    @Order(7)
    void deleteById_RemovesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var userRequest = userList.getFirst();
        repository.deleteById(userRequest.getId());
        var usersFindAll = repository.findAll();

        Assertions.assertThat(usersFindAll).doesNotContain(userRequest);
    }

    @Test
    @DisplayName("Delete user by id when id is null")
    @Order(8)
    void deleteById_DoesNotRemoveUsers_WhenIdIsNull() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        // Pego o size da lista antes
        var userSizeBefore = userList.size();

        repository.deleteById(null);

        // Pego o size da lista depois
        var userSizeAfter = repository.findAll();

        // Verifico se os tamanhos das listas antes e depois são iguais
        Assertions.assertThat(userSizeAfter).hasSize(userSizeBefore);
    }

    @Test
    @DisplayName("Update user successfully")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var userOriginal = userList.getFirst();
        var userUpdate = User
                .builder()
                .id(userOriginal.getId())
                .firstName("Lucas")
                .lastName(userOriginal.getFirstName())
                .email(userOriginal.getEmail())
                .build();

        repository.update(userUpdate);
        var userListAfter = repository.findAll();

        Assertions.assertThat(userListAfter).hasSize(2).contains(userUpdate);
    }
}