package academy.devdojo.controller;

import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Versionamento -> significa a versao 1 do animeController
@RequestMapping("v1/animes")
@RequiredArgsConstructor
@Slf4j
public class AnimeController {
    private final AnimeMapper mapper;

    private final AnimeService animeService;

    // De acordo com os testes do postman, esta demorando 2ms para a requisicao entrar e sair com 100 usuarios testando
//    @GetMapping()
//    public List<String> listAll() throws InterruptedException {
//        log.info(Thread.currentThread().getName());
//        TimeUnit.SECONDS.sleep(1);
//        return List.of("Naruto", "Boruto", "Death Note");
//    }


    // Error 500 -> erro no codigo, algo que o desenvolvedor deixou passar
    // required false -> quebrando funcionalidades existentes para quem esta consumindo a api
    @GetMapping("filterName")
    public ResponseEntity<List<AnimeGetResponse>> listAllAnimeName(@RequestParam(required = false) String name) {
        log.debug("List all animes for name: {}", name);

        var animes = animeService.listAll(name);
        List<AnimeGetResponse> animeGetResonseList = mapper.toAnimeGetResonseList(animes);

        return ResponseEntity.ok().body(animeGetResonseList);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to anime by id: {}", id);

        var anime = animeService.findByIdOrThrowNotFound(id);
        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok().body(animeGetResponse);
    }

    // Idempotente -> Só pode ser executado uma vez
    // O post nao é idempotente
    @PostMapping()
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
        log.debug("Request to save anime: {}", animePostRequest.getName());

        var anime = mapper.toAnimePostRequest(animePostRequest);
        var animeSaved = animeService.save(anime);

        var animePostResponse = mapper.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(201).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        animeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest) {
        log.debug("Request to updated anime: {}", animePutRequest.getName());

        var anime = mapper.toAnimePutRequest(animePutRequest);
        animeService.update(anime);

        return ResponseEntity.noContent().build();
    }
}
