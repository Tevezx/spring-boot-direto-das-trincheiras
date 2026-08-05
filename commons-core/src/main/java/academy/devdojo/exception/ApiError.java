package academy.devdojo.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApiError {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
