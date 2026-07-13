package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// É possivel fazer umea excecao customizada dessa forma, porem é mais aconselhado o outro metodo de excecao
// pois ja utilizamos algo que é direto do responseStatusException
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Anime not Found")
public class CustomNotFoundException extends RuntimeException{
    public CustomNotFoundException(String message) {
        super(message);
    }
}
