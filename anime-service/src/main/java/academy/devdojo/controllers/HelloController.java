package academy.devdojo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Todos os metodos vao retornar algo para o responseBody
@RestController
public class HelloController {

    // get mapping significa que quando eu estiver acessando um get para esse endpoint essa funcao ira ser executada
    // Como nao defini nenhuma rota, ele passa como /, ou seja: localhost:8080/
    // Mas ao definir dentro do mapping "hi", ele muda para: localhost:8080/hi

    // Dessa forma eu defino dois endpoints para uma requisicao
    // @Getmapping(value = {"hi", "hi/"})

    @GetMapping("hi")
    public String hi() {
        return "Hello World";
    }
}
