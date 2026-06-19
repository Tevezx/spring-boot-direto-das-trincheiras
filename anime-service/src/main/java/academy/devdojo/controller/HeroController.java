package academy.devdojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {
    private static final List<String> HEROES = List.of("Super Man", "Spider Man", "Iron Man");

    @GetMapping()
    public List<String> listAllHeroes() {
        return HEROES;
    }

    // Filtrando o getMapping com requestParam dizendo que eu espero receber um name como filtro
    // required false -> significa que eu nao preciso exatamente retornar algo
    // defaultValue -> valor default que sera exibido caso nao haja nenhum filtro
    @GetMapping("filter")
    public List<String> listAllHeroesParam(@RequestParam (required = false) String name) {
        return HEROES
                .stream()
                .filter(h -> h.equalsIgnoreCase(name))
                .toList();
    }

    // Filtrando por list, ou seja, o usuario vai entrar com uma lista de nomes para filtrar
    @GetMapping("filterList")
    public List<String> listAllHeroesParamList(@RequestParam List<String> names) {
        return HEROES
                .stream()
                .filter(names::contains)
                .toList();
    }
}
