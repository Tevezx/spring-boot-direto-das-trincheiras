//package outside.devdojo;
//
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//// Todas as classes que utilizam @Component @Service ou @Repository é considerada um classe beans
//// O restcontroller herda esse beans, pois o restcontroller é um controller e o controller é um component
//// O beans é apenas o que esta dentro da pasta raiz do projeto, por isso essa classe nao identifica como beans quando dar run
//
//// Somente scaneando o pacote que é possivel executar de forma correta uma classe que esteja fora da pasta raiz do projeto
//// Porem, dessa forma eu digo que o pacote base de tudo é esse, entao o HiController nao ira mais funcionar, por isso precisamos passar a raiz
//@ComponentScan(basePackages = {"outside.devdojo", "academy.devdojo"})
//@RestController
//public class OutSideController {
//    @GetMapping("test")
//    public String outside(){
//        return "OutSideController";
//    }
//}
