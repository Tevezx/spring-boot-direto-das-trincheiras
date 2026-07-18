package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

// Normalmente nao se testa o repository, só em casos que existam uma complexa regra de negocio
@DataJpaTest
// Tenho que colocar essa auto config para que os meus testes utilizam o banco em memoria
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// Com essa anotação do transcational, os testes vao rodar e o que acontecer no anterior vai ser mantido para o seguinte
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(UserUtils.class)
class UserRespositoryTest {
    @Autowired
    private UserRespository respository;
    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("Saving user when success ful")
    @Order(1)
    void save_SavesUser_WhenSuccessFul() {
        var user = userUtils.newUserToSave();
        var userSave = respository.save(user);

        Assertions.assertThat(userSave).hasNoNullFieldsOrProperties();
        Assertions.assertThat(userSave.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Finding all users")
    @Order(2)
    @Sql("/sql/init_one_user.sql")
    void findAll_ReturnsUsers_WhenSuccessFul() {
        var users = respository.findAll();
        Assertions.assertThat(users).isNotEmpty();
    }
}