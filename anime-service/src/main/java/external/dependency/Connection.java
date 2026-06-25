package external.dependency;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor

// Eu quero o bean dessa classe, porem como ela esta fora do meu pacote academy.devdojo
// significa que eu nao consigo fazer um scan nela, por isso existe o Configuration
public class Connection {
    private String host;
    private String username;
    private String password;
}
