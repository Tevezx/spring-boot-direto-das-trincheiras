package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
// Versionamento -> significa a versao 1 do animeController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    // De acordo com os testes do postman, esta demorando 2ms para a requisicao entrar e sair com 100 usuarios testando
//    @GetMapping()
//    public List<String> listAll() throws InterruptedException {
//        log.info(Thread.currentThread().getName());
//        TimeUnit.SECONDS.sleep(1);
//        return List.of("Naruto", "Boruto", "Death Note");
//    }


    // Error 500 -> erro no codigo, algo que o desenvolvedor deixou passar
    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> listAll() {
        log.debug("Finding all animes");
        var animes = Anime.getAnimeList();
        List<AnimeGetResponse> animeGetResonseList = AnimeMapper.INSTANCE.toAnimeGetResonseList(animes);
        return ResponseEntity.ok().body(animeGetResonseList);
    }

    // required false -> quebrando funcionalidades existentes para quem esta consumindo a api
    @GetMapping("filterName")
    public ResponseEntity<List<AnimeGetResponse>> listAllAnimeName(@RequestParam(required = false) String name) {
        log.debug("List all animes for name: {}", name);
        var animes = Anime.getAnimeList();
        List<AnimeGetResponse> animeGetResonseList = AnimeMapper.INSTANCE.toAnimeGetResonseList(animes);
        if (name == null) return ResponseEntity.ok().body(animeGetResonseList);

        List<AnimeGetResponse> list = animeGetResonseList
                .stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to anime by id: {}", id);

        // dessa forma, encontro na lista o anime e ja mando logo o primeiro
        AnimeGetResponse animeGetResponse = Anime.getAnimeList()
                .stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .map(MAPPER::toAnimeGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
        return ResponseEntity.ok().body(animeGetResponse);
    }

    // Idempotente -> Só pode ser executado uma vez
    // O post nao é idempotente
    @PostMapping()
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
        log.debug("Request to save anime: {}", animePostRequest.getName());
        var animePostRequests = MAPPER.toAnimePostRequest(animePostRequest);
        var animePostResponse = MAPPER.toAnimePostResponse(animePostRequests);

        Anime.getAnimeList().add(animePostRequests);
        return ResponseEntity.status(201).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);
        var animeToDelete = Anime.getAnimeList()
                .stream()
                .filter(anime -> anime.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
        Anime.getAnimeList().remove(animeToDelete);

        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest) {
        log.debug("Request to updated anime: {}", animePutRequest.getName());

        var animeToDelete = Anime.getAnimeList()
                .stream()
                .filter(anime -> anime.getId().equals(animePutRequest.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));

        var animeUpdated = MAPPER.toAnime(animePutRequest);
        Anime.getAnimeList().remove(animeToDelete);
        Anime.getAnimeList().add(animeUpdated);

        return ResponseEntity.noContent().build();
    }
}
