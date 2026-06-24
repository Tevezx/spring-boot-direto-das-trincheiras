package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController

@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {
    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping()
    public ResponseEntity<List<ProducerGetResponse>> listAll() {
        log.debug("Finding all producers");
        var producers = Producer.getProducers();
        List<ProducerGetResponse> producerGetResponses = MAPPER.toProducerGetResponseList(producers);
        return ResponseEntity.ok().body(producerGetResponses);
    }

    @GetMapping("filterName")
    public ResponseEntity<List<ProducerGetResponse>> listAllAnimeName(@RequestParam(required = false) String name) {
        log.debug("List all producers name: {}", name);

        var producers = Producer.getProducers();
        var producerGetResponse = MAPPER.toProducerGetResponseList(producers);
        if (name == null) return ResponseEntity.ok().body(producerGetResponse);

        List<ProducerGetResponse> list = Producer.getProducers()
                .stream()
                .filter(producer -> producer.getName().equalsIgnoreCase(name))
                .map(MAPPER::toProducerGetResponse)
                .toList();

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Find by producer id: {}", id);

        var producerGetResponse = Producer.getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .map(MAPPER::toProducerGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        return ResponseEntity.ok().body(producerGetResponse);
    }

    // consumes e produces -> significa o que esse postMapping espera receber e passar como valor, posso definir entre varios tipos (normalmente é apenas json)
    // Não é obrigatório utilizar, pois o padrão ja utiliza json
    // headers -> normalmente significa a chave da api que estamos criando, ele so mapeia a requisicao se esse header for encontrado (api-key)
    // @RequestHeader -> Consigo visualizar os headers que estão vindo da requisicao
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);

        var producer = MAPPER.toProducer(producerPostRequest);
        var producerPostResponse = MAPPER.toProducerPostResponse(producer);

        Producer.getProducers().add(producer);

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
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(producerPostResponse);
    }

    // deletando baseado no id
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);

        Producer producerDelete = Producer.getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        Producer.getProducers().remove(producerDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest) {
        log.debug("Request to updated producer: {}", producerPutRequest.getName());

        // Filtrando o producerDelete para o mesmo id do producerPutRequest
        var producerToDelete = Producer.getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(producerPutRequest.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        // Transformando o producerPutRequest em um producer
        var producerUpdated = MAPPER.toProducerUpdated(producerPutRequest, producerToDelete.getCreatedAt());

        // Removendo o producer que encontrei do mesmo id do producerPutRequest
        Producer.getProducers().remove(producerToDelete);

        // Adicionando o producer novo que passei no producerPutRequest
        Producer.getProducers().add(producerUpdated);
        return ResponseEntity.noContent().build();
    }

}
