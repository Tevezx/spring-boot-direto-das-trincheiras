package academy.devdojo.repository;

import academy.devdojo.domain.User;
import academy.devdojo.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // @Query(nativeQuery = true, "select * from user_profile")
    // Dessa forma, eu economizo linhas de querys no meu banco de dados, pois faço tudo igual porem em uma linha só
    // Enquanto antes, eu estava utilizando 6 linhas para fazer uma query
    @Query("SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH up.profile p")
    List<UserProfile> retrieveAll();

    // Basicamente estamos dizendo que ao chamar findAll, preciso retornar essas duas classes
    // Dessa forma, a minha query fica apenas em uma linha, performando mais e sendo mais simples que utilizar a anotacao @Query
    // @EntityGraph(attributePaths = {"user", "profile"})
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();

    @Query("SELECT up.user FROM UserProfile up where up.profile.id=?1")
    List<User> findAllUsersByProfileId(Long id);
}
