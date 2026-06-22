package academy.devdojo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

// Todos os metodos vao retornar algo para o responseBody
@RestController

// Dessa forma definimos que todas as rotas dessa classe tem v1/greetings/
@RequestMapping("v1/greetings")
@Slf4j
public class HelloController {

    // get mapping significa que quando eu estiver acessando um get para esse endpoint essa funcao ira ser executada
    // Como nao defini nenhuma rota, ele passa como /, ou seja: localhost:8080/
    // Mas ao definir dentro do mapping "hi", ele muda para: localhost:8080/hi

    // Dessa forma eu defino dois endpoints para uma requisicao
    // @Getmapping(value = {"hi", "hi/"})

    // Antigamente se utilizava o RequestMapping(method = RequestMethord.GET, value = "hi")
    @GetMapping()
    public String hi() {
        return "Hello World";
    }

    // Pode colocar @PostMapping("save"), porem nao é boa pratica
    @PostMapping()
    // @RequestBody -> pega o que esta no corpo daquela requisicao
    public Long save(@RequestBody String name) {
        log.info("save {}", name);
        return ThreadLocalRandom.current().nextLong(1, 1000);
    }
}
