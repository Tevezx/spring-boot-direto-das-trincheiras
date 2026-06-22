package academy.devdojo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProducerGetResponse {
    // O request e response servem para desacoplar a classe de dominio
    // O response é justamente a resposta que eu quero dar para a api (o retorno)
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
