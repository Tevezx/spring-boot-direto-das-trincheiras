package academy.devdojo;

import academy.devdojo.config.ConnectionConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
// Habilitando o configuration properties da minha conexao com o banco de dados via application.yaml
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
public class AnimeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimeServiceApplication.class, args);
    }

}
