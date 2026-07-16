package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

// Essa anotação possibilita que todos os meus controllers tenham essa excecao ativa
@RestControllerAdvice
public class GlobalErrorHandlerAdvice {
    // Qual excecao eu quero manusear
    // Sempre é boa prática exibir apenas o essencial para o usuário
    // Nunca exibir informacoes irrelevantes ou que sejam alem do que ele precisa
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handleNotFoundException(NotFoundException e) {
        var error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getReason());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Todas as vezes que atualizar algo no core, preciso dar um mvn clean install no terminal

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<DefaultErrorMessage> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicated entry for one of the unique fields");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
