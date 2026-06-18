package academy.devdojo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
// Versionamento -> significa a versao 1 do animeController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    // De acordo com os testes do postman, esta demorando 2ms para a requisicao entrar e sair com 100 usuarios testando
    @GetMapping()
    public List<String> listAll() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return List.of("Naruto", "Boruto", "Death Note");
    }
}
