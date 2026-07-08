package academy.devdojo.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPostResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
