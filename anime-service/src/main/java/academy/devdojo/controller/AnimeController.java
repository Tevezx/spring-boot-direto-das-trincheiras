package academy.devdojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

// Versionamento -> significa a versao 1 do animeController
@RequestMapping("v1/animes")
public class AnimeController {
    @GetMapping()
    public List<String> listAll() {
        return List.of("Naruto", "Boruto", "Death Note");
    }
}
