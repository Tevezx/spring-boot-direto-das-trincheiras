package academy.devdojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Todos os metodos vao retornar algo para o responseBody
@RestController

// Dessa forma definimos que todas as rotas dessa classe tem greetings/
@RequestMapping(value = "greetings")
public class HelloController {

    // get mapping significa que quando eu estiver acessando um get para esse endpoint essa funcao ira ser executada
    // Como nao defini nenhuma rota, ele passa como /, ou seja: localhost:8080/
    // Mas ao definir dentro do mapping "hi", ele muda para: localhost:8080/hi

    // Dessa forma eu defino dois endpoints para uma requisicao
    // @Getmapping(value = {"hi", "hi/"})

    // Antigamente se utilizava o RequestMapping(method = RequestMethord.GET, value = "hi")
    @GetMapping("hi")
    public String hi() {
        return "Hello World";
    }
}
