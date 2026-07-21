package academy.devdojo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserProfileGetResponse {
    // Dois records que dizem o que eu quero pegar da minha entidade
    public record User(Long id, String firstName) {
    }

    public record Profile(Long id, String name) {
    }

    private Long id;
    private User user;
    private Profile profile;
}
