package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.repository.UserRespository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRespository repository;
    @InjectMocks
    private UserUtils userUtils;
    private List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("Finding all users when name is null")
    @Order(1)
    void findAll_ReturnsUsers_WHenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);
        var users = service.findAll(null);

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("List all users when name exists")
    @Order(2)
    void listAllName_ReturnsUsersName_WhenNameExists() {
        var user = userList.getFirst();
        List<User> userExpected = userList.stream().filter(u -> u.getFirstName().equalsIgnoreCase(user.getFirstName())).toList();

        BDDMockito.when(repository.findByfirstNameIgnoreCase(user.getFirstName())).thenReturn(userExpected);
        var users = service.findAll(user.getFirstName());

        Assertions.assertThat(users).isNotNull().isNotEmpty().containsExactlyElementsOf(userExpected);
    }

    @Test
    @DisplayName("Finding user by id exists")
    @Order(3)
    void findById_ReturnsUser_WhenIdExists() {
        var user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        var userResponse = service.findById(user.getId());

        Assertions.assertThat(userResponse).isNotNull().isEqualTo(user);
    }

    @Test
    @DisplayName("Finding user by id not exists - Throw ResponseStatusException")
    @Order(4)
    void findById_ReturnsThrowResponseStatus_WhenIdNotExists() {
        var user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findById(user.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Saving user when success ful")
    @Order(5)
    void save_SavesUser_WhenSuccessFul() {
        var user = User.builder().id(3L).firstName("Paulo").lastName("Silva").email("paulo@gmail.com").build();
        BDDMockito.when(repository.save(user)).thenReturn(user);
        BDDMockito.when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        var userSave = service.save(user);
        Assertions.assertThat(userSave).hasNoNullFieldsOrProperties().isEqualTo(user);
    }

    @Test
    @DisplayName("Delete by id when id exists")
    @Order(6)
    void deleteById_RemovesUser_WhenIdExists() {
        var user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        service.deleteById(user.getId());
        Assertions.assertThatNoException().isThrownBy(() -> service.deleteById(user.getId()));
    }

    @Test
    @DisplayName("Delete by id when id not exists - Throw ResponseStatusException")
    @Order(7)
    void deleteById_RemovesUser_WhenIdNotExists() {
        var user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.deleteById(user.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Update user when id exists")
    @Order(8)
    void update_UpdateUser_WhenIdExists() {
        var user = userList.getFirst().withFirstName("Carlos");
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        BDDMockito.when(repository.findByEmailAndIdNot(user.getEmail(), user.getId())).thenReturn(Optional.empty());

        service.update(user);
        Assertions.assertThatNoException().isThrownBy(() -> service.update(user));
    }

    @Test
    @DisplayName("Update user when id not exists - Throw ResponseStatusException")
    @Order(9)
    void update_UpdateUser_WhenIdNotExists() {
        var user = userList.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(user)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Saving throws EmailAlreadyException when email belongs to another user")
    @Order(10)
    void save_ThrowsEmailAlreadyException_WhenEmailBelongsAnotherUser() {
        var userEmailExists = userList.getFirst();
        var user = UserUtils.newUserList().getFirst().withEmail(userEmailExists.getEmail());

        BDDMockito.when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEmailExists));

        Assertions.assertThatException().isThrownBy(() -> service.save(user)).isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Update throws EmailAlreadyException when email belongs to another user")
    @Order(11)
    void update_ThrowsEmailAlreadyException_WhenEmailBelongsAnotherUser() {
        var userToUpdate = userList.getFirst().withFirstName("Carlos");
        var userEmailExists = userList.getLast();

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(userToUpdate.getEmail(), userToUpdate.getId())).thenReturn(Optional.of(userEmailExists));

        Assertions.assertThatException().isThrownBy(() -> service.update(userToUpdate)).isInstanceOf(EmailAlreadyExistsException.class);
    }

}