package academy.devdojo.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestPutUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
