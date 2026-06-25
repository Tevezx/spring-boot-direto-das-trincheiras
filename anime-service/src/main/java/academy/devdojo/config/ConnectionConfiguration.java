package academy.devdojo.config;

import external.dependency.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfiguration {

    // Agora ele pega um objeto valida e injeta dentro da minha classe conection para que meu repository pegue
    @Bean
    // @Primary Se tiver dois, utiliza esse como padrao
    public Connection connectionMySql() {
        return new Connection("LocalHost", "Carlos", "Carlos123");
    }

    @Bean(name = "connectionMongo") // Posso declarar um nome para o bean para poder chama-lo
    public Connection connectionMongo() {
        return new Connection("LocalHost", "Tevez", "Carlos123");
    }
}
