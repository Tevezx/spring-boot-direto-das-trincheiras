package academy.devdojo.config;

import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationProperties configurationProperties;

//    @Value("${database.url}")
//    private String url;
//    @Value("${database.username}")
//    private String username;
//    @Value("${database.password}")
//    private String password;

    // @Profile("mysql") estou criando um profile que só existe se for mysql e no application.yaml eu posso ativa-lo para dizer que estou utilizando ele
    @Bean
    // Eu preciso ter um primário, para dizer aos testes que existe um assim que iniciar o programa
    @Primary
    public Connection connectionMySql() {
        // usa os valores injetados pelo configurationProperties (url, username, password do yaml)
        return new Connection(configurationProperties.url(), configurationProperties.username(), configurationProperties.password());
    }

    // Posso declarar um nome para o bean para poder chama-lo
    @Bean("connectionMongo")
    @Profile("mongo")
    public Connection connectionMongo() {
        // conexão Mongo separada
        return new Connection(configurationProperties.url(), configurationProperties.username(), configurationProperties.password());
    }
}
