package academy.devdojo.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserProfileRepositoryTest {
    @Autowired
    private UserProfileRepository repository;

    @Test
    @DisplayName("Finding all user profile")
    @Order(1)
    @Sql("/sql/init_two_user_one_profile.sql")
    void findAll_ReturnsUsersProfiles_WhenSuccessFul() {
        var userProfileAll = repository.findAll();
        Assertions.assertThat(userProfileAll).isNotEmpty().hasSize(2).doesNotContainNull();

        // Verificando se meus users realmente existem
        userProfileAll.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("Finding all users by profile id")
    @Order(2)
    @Sql("/sql/init_one_user_one_profile.sql")
    void findAllUsersProfileById_WhenSuccessFul() {
        var userProfileUser = repository.findAllUsersByProfileId(1L);
        Assertions.assertThat(userProfileUser).isNotEmpty().hasSize(1);
    }
}