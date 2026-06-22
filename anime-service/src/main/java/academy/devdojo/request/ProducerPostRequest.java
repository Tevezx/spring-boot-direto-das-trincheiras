package academy.devdojo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerPostRequest {
    // request -> serve para saber o que eu quero quando for criar um producer,
    // ou seja, nao preciso passar a data de cricacao do producer
    // No json, eu apenas passo o name, pois o id e data de criacao sao gerados automaticamente pelo sistema na logica
    private String name;
}
