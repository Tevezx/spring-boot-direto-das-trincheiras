package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Versionamento -> significa a versao 1 do animeController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    // De acordo com os testes do postman, esta demorando 2ms para a requisicao entrar e sair com 100 usuarios testando
//    @GetMapping()
//    public List<String> listAll() throws InterruptedException {
//        log.info(Thread.currentThread().getName());
//        TimeUnit.SECONDS.sleep(1);
//        return List.of("Naruto", "Boruto", "Death Note");
//    }


    // Error 500 -> erro no codigo, algo que o desenvolvedor deixou passar
    @GetMapping()
    public List<Anime> listAll() {
        return Anime.animeList();
    }

    // required false -> quebrando funcionalidades existentes para quem esta consumindo a api
    @GetMapping("filterName")
    public List<Anime> listAllAnimeName(@RequestParam(required = false) String name) {
        var animes = Anime.animeList();
        if (name == null) return animes;

        return animes.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    @GetMapping("{id}")
    public Anime findById(@PathVariable Long id) {
        var animes = Anime.animeList();
        // dessa forma, encontro na lista o anime e ja mando logo o primeiro
        return animes.stream().filter(anime -> anime.getId().equals(id)).findFirst().orElse(null);
    }
}
