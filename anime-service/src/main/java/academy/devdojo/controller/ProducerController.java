package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {
    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping()
    public List<Producer> listAll() {
        return Producer.getProducers();
    }

    @GetMapping("filterName")
    public List<Producer> listAllAnimeName(@RequestParam(required = false) String name) {
        var producers = Producer.getProducers();
        if (name == null) return producers;

        return producers.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    @GetMapping("{id}")
    public Producer findById(@PathVariable Long id) {
        var producers = Producer.getProducers();
        return producers.stream().filter(producer -> producer.getId().equals(id)).findFirst().orElse(null);
    }

    // consumes e produces -> significa o que esse postMapping espera receber e passar como valor, posso definir entre varios tipos (normalmente é apenas json)
    // Não é obrigatório utilizar, pois o padrão ja utiliza json
    // headers -> normalmente significa a chave da api que estamos criando, ele so mapeia a requisicao se esse header for encontrado (api-key)
    // @RequestHeader -> Consigo visualizar os headers que estão vindo da requisicao
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);

        var producer = MAPPER.toProducer(producerPostRequest);
        var producerGetResponse = MAPPER.toProducerGetResponse(producer);

        listAll().add(producer);

        // Na classe eu coloco ResponseEntity<Producer>, pois estou retornando um status de criacao
        // Retorno o status da requisicao, consigo retornar qualquer status http
        // return ResponseEntity.status(HttpStatus.CREATED).body(producer);

        // forma abreviada de retornar o http 200
        // return ResponseEntity.ok(producer);

        // Quando nao quero retornar nada, no caso de update (http 204, sem retorno mas feito com sucesso)
        // Deixo a classe para retornar -> ResponseEntity<Void>
        // return ResponseEntity.noContent().build();

        // Consigo adicionar headers
        var responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization", "my key");
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(producerGetResponse);
    }
}
