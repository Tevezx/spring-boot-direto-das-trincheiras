package academy.devdojo.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestPostUser {
    private String firstName;
    private String lastName;
    private String email;
}
